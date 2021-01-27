package io.yegair.jom.examples

import io.yegair.jom.Combinators.alt
import io.yegair.jom.Combinators.delimited
import io.yegair.jom.Combinators.foldMany0
import io.yegair.jom.Combinators.foldMany1
import io.yegair.jom.Combinators.map
import io.yegair.jom.Combinators.mapOpt
import io.yegair.jom.Combinators.preceded
import io.yegair.jom.Combinators.separatedList0
import io.yegair.jom.Combinators.separatedPair
import io.yegair.jom.Combinators.value
import io.yegair.jom.Combinators.verify
import io.yegair.jom.Input
import io.yegair.jom.ParseResult
import io.yegair.jom.Parser
import io.yegair.jom.Parsers.anyCodePoint
import io.yegair.jom.Parsers.codePoint
import io.yegair.jom.Parsers.multiSpace0
import io.yegair.jom.Parsers.noneOf
import io.yegair.jom.Parsers.oneOf
import io.yegair.jom.Parsers.tag
import io.yegair.jom.Parsers.takeBytes
import io.yegair.jom.Utf8CodePoint
import io.yegair.jom.Utf8CodePoints.toUtf8CodePoint
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

private sealed class JsonValue
private object Null : JsonValue()
private data class Bool(private val value: Boolean) : JsonValue()
private data class Str(private val value: String) : JsonValue()
private data class Num(private val value: Double) : JsonValue()
private data class Arr(private val values: List<JsonValue>) : JsonValue()
private data class Obj(private val values: Map<String, JsonValue>) : JsonValue()

private fun bool(input: Input) =
    alt(
        value(false, tag("false")),
        value(true, tag("true")),
    ).parse(input)

private fun u16Hex(input: Input) =
    mapOpt(takeBytes(4)) { bytes ->
        bytes.toString(Charsets.UTF_8).toIntOrNull(16)
    }.parse(input)

private fun unicodeEscape(input: Input) =
    alt(
        // not a surrogate
        verify(::u16Hex) { !(0xD800..0xE000).contains(it) },
        // See https://en.wikipedia.org/wiki/UTF-16#Code_points_from_U+010000_to_U+10FFFF for details
        map(
            verify(separatedPair(::u16Hex, tag("\\u"), ::u16Hex)) { (high, low) ->
                (0xD800..0xDC00).contains(high) && (0xDC00..0xE000).contains(low)
            }
        ) { (high, low) ->
            val highTen = high - 0x0000D800
            val lowTen = low - 0x0000DC00
            (highTen shl 10) + lowTen + 0x00010000
        }
    ).parse(input)

private fun character(input: Input): ParseResult<Utf8CodePoint> {
    val res = noneOf("\"").parse(input)

    if (!res.ok) {
        return ParseResult.error(res)
    }

    return when (res.output) {
        '\\'.toUtf8CodePoint() ->
            alt(
                mapOpt(anyCodePoint()) { c ->
                    when (c) {
                        '"'.toUtf8CodePoint() -> c
                        '\\'.toUtf8CodePoint() -> c
                        '/'.toUtf8CodePoint() -> c
                        'b'.toUtf8CodePoint() -> '\u0008'.toUtf8CodePoint()
                        'f'.toUtf8CodePoint() -> '\u000C'.toUtf8CodePoint()
                        'n'.toUtf8CodePoint() -> '\n'.toUtf8CodePoint()
                        'r'.toUtf8CodePoint() -> '\r'.toUtf8CodePoint()
                        't'.toUtf8CodePoint() -> '\t'.toUtf8CodePoint()
                        else -> null
                    }
                },
                preceded(codePoint('u'), ::unicodeEscape)
            ).parse(res.remaining)
        else -> {
            ParseResult.ok(res.remaining, res.output)
        }
    }
}

private fun str(input: Input) =
    delimited(
        codePoint('"'),
        foldMany0(
            ::character,
            ::StringBuilder,
            { str, c -> str.appendCodePoint(c) },
            { it.toString() }
        ),
        codePoint('"')
    ).parse(input)

private fun num(input: Input): ParseResult<Double> =
    mapOpt(
        foldMany1(
            // TODO: replace naive and erroneous JSON number parsing.
            //  Depends on implementing the number literal parsers
            //  see: https://docs.rs/nom/6.1.0/nom/number/complete/index.html
            oneOf("-0123456789.eE"),
            ::StringBuilder,
            { sb, cp -> sb.appendCodePoint(cp) },
            { it.toString() }
        )
    ) {
        it.toDoubleOrNull()
    }.parse(input)

private fun <O> ws(f: Parser<O>) = delimited(multiSpace0(), f, multiSpace0())

private fun arr(input: Input): ParseResult<List<JsonValue>> =
    delimited(
        codePoint('['),
        ws(separatedList0(ws(codePoint(',')), ::jsonValue)),
        codePoint(']')
    ).parse(input)

private fun obj(input: Input): ParseResult<Map<String, JsonValue>> =
    map(
        delimited(
            codePoint('{'),
            ws(
                separatedList0(
                    ws(codePoint(',')),
                    separatedPair(::str, ws(codePoint(':')), ::jsonValue)
                )
            ),
            codePoint('}')
        )
    ) {
        it.toMap()
    }.parse(input)

private fun jsonValue(input: Input): ParseResult<JsonValue> =
    alt(
        value(Null, tag("null")),
        map(::bool, ::Bool),
        map(::str, ::Str),
        map(::num, ::Num),
        map(::arr, ::Arr),
        map(::obj, ::Obj)
    ).parse(input)

private fun json(input: Input) = ws(::jsonValue).parse(input)

class Json {

    @Test
    fun jsonString() {
        assertThatParseResult(str(Input.of("\"\"")))
            .isOk("")
            .hasRemainingInput("")

        assertThatParseResult(str(Input.of("\"abc\"")))
            .isOk("abc")
            .hasRemainingInput("")

        assertThatParseResult(str(Input.of("\"abc\\\"\\\\\\/\\b\\f\\n\\r\\t\\u0001\\u2014\u2014def\"")))
            .isOk("abc\"\\/\u0008\u000C\n\r\t\u0001——def")
            .hasRemainingInput("")

        assertThatParseResult(str(Input.of("\"\\uD83D\\uDE10\"")))
            .isOk("😐")
            .hasRemainingInput("")

        assertThatParseResult(str(Input.of("\"")))
            .isError
            .hasRemainingInput("\"")

        assertThatParseResult(str(Input.of("\"abc")))
            .isError
            .hasRemainingInput("\"abc")

        assertThatParseResult(str(Input.of("\"\\\"")))
            .isError
            .hasRemainingInput("\"\\\"")

        assertThatParseResult(str(Input.of("\"\\u123\"")))
            .isError
            .hasRemainingInput("\"\\u123\"")

        assertThatParseResult(str(Input.of("\"\\uD800\"")))
            .isError
            .hasRemainingInput("\"\\uD800\"")

        assertThatParseResult(str(Input.of("\"\\uD800\\uD800\"")))
            .isError
            .hasRemainingInput("\"\\uD800\\uD800\"")

        assertThatParseResult(str(Input.of("\"\\uDC00\"")))
            .isError
            .hasRemainingInput("\"\\uDC00\"")
    }

    @Test
    fun jsonNumber() {
        assertThatParseResult(num(Input.of("0")))
            .isOk(0.0)
            .hasRemainingInput("")

        assertThatParseResult(num(Input.of("42")))
            .isOk(42.0)
            .hasRemainingInput("")

        assertThatParseResult(num(Input.of("1.0")))
            .isOk(1.0)
            .hasRemainingInput("")

        assertThatParseResult(num(Input.of("123e4")))
            .isOk(123e4)
            .hasRemainingInput("")

        assertThatParseResult(num(Input.of("42E-12")))
            .isOk(42E-12)
            .hasRemainingInput("")
    }

    @Test
    fun jsonObj() {
        assertThatParseResult(json(Input.of("{}")))
            .isOk(Obj(mapOf()))
            .hasRemainingInput("")

        assertThatParseResult(json(Input.of("""{"a":42,"b":"x"}""")))
            .isOk(
                Obj(
                    mapOf(
                        "a" to Num(42.0),
                        "b" to Str("x")
                    )
                )
            )
            .hasRemainingInput("")

        assertThatParseResult(
            json(
                Input.of(
                    """
            {
              "null" : null,
              "true"  :true ,
              "false":  false  ,
              "number" : 123e4 ,
              "string" : " abc 123 " ,
              "array" : [ false , 1 , "two" ] ,
              "object" : { "a" : 1.0 , "b" : "c" } ,
              "empty_array" : [  ] ,
              "empty_object" : {   }
            } 
            """
                )
            )
        )
            .isOk(
                Obj(
                    mapOf(
                        "null" to Null,
                        "true" to Bool(true),
                        "false" to Bool(false),
                        "number" to Num(123e4),
                        "string" to Str(" abc 123 "),
                        "array" to Arr(
                            listOf(
                                Bool(false),
                                Num(1.0),
                                Str("two")
                            )
                        ),
                        "object" to Obj(
                            mapOf(
                                "a" to Num(1.0),
                                "b" to Str("c")
                            )
                        ),
                        "empty_array" to Arr(listOf()),
                        "empty_object" to Obj(mapOf())
                    )
                )
            )
            .hasRemainingInput("")
    }
}
