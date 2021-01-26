package io.yegair.jom.examples

import io.yegair.jom.Combinators.alt
import io.yegair.jom.Combinators.delimited
import io.yegair.jom.Combinators.foldMany0
import io.yegair.jom.Combinators.map
import io.yegair.jom.Combinators.pair
import io.yegair.jom.Input
import io.yegair.jom.ParseResult
import io.yegair.jom.Parsers.codePoint
import io.yegair.jom.Parsers.digit1
import io.yegair.jom.Parsers.space0
import io.yegair.jom.Parsers.tag
import io.yegair.jom.Utf8CodePoints.toUtf8CodePoint
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

private fun parens(input: Input) = delimited(space0(), delimited(tag("("), ::expr, tag(")")), space0()).parse(input)

private fun factor(input: Input) =
    alt(
        map(delimited(space0(), digit1(), space0())) { it.toLong() },
        ::parens
    ).parse(input)

private fun term(input: Input): ParseResult<Long> {

    val res1 = factor(input)
    if (!res1.ok) {
        return res1
    }

    return foldMany0(
        pair(alt(codePoint('*'), codePoint('/')), ::factor),
        res1.output,
        { acc, (op, value) ->
            when (op) {
                '*'.toUtf8CodePoint() -> acc * value
                else -> acc / value
            }
        }
    ).parse(res1.remaining)
}

private fun expr(input: Input): ParseResult<Long> {

    val res = term(input)
    if (!res.ok) {
        return res
    }

    return foldMany0(
        pair(alt(codePoint('+'), codePoint('-')), ::term),
        res.output,
        { acc, (op, value) ->
            when (op) {
                '+'.toUtf8CodePoint() -> acc + value
                else -> acc - value
            }
        }
    ).parse(res.remaining)
}

class Arithmetic {

    @Test
    fun factorTest() {
        assertThatParseResult(factor(Input.of("3")))
            .isOk(3)
            .hasRemainingInput("")

        assertThatParseResult(factor(Input.of(" 12")))
            .isOk(12)
            .hasRemainingInput("")

        assertThatParseResult(factor(Input.of("537  ")))
            .isOk(537)
            .hasRemainingInput("")

        assertThatParseResult(factor(Input.of("  24    ")))
            .isOk(24)
            .hasRemainingInput("")
    }

    @Test
    fun termTest() {
        assertThatParseResult(term(Input.of(" 12 *2 /  3")))
            .isOk(8)
            .hasRemainingInput("")

        assertThatParseResult(term(Input.of(" 2* 3  *2 *2 /  3")))
            .isOk(8)
            .hasRemainingInput("")

        assertThatParseResult(term(Input.of(" 48 /  3/2")))
            .isOk(8)
            .hasRemainingInput("")
    }

    @Test
    fun exprTest() {
        assertThatParseResult(expr(Input.of(" 1 +  2 ")))
            .isOk(3)
            .hasRemainingInput("")

        assertThatParseResult(expr(Input.of(" 12 + 6 - 4+  3")))
            .isOk(17)
            .hasRemainingInput("")

        assertThatParseResult(expr(Input.of(" 1 + 2*3 + 4")))
            .isOk(11)
            .hasRemainingInput("")
    }

    @Test
    fun parensTest() {
        assertThatParseResult(expr(Input.of(" (  2 )")))
            .isOk(2)
            .hasRemainingInput("")

        assertThatParseResult(expr(Input.of(" 2* (  3 + 4 ) ")))
            .isOk(14)
            .hasRemainingInput("")

        assertThatParseResult(expr(Input.of("  2*2 / ( 5 - 1) + 3")))
            .isOk(4)
            .hasRemainingInput("")
    }
}
