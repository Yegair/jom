package io.yegair.jom

import io.yegair.jom.Utf8CodePoints.toUtf8CodePoint
import io.yegair.jom.Utf8CodePoints.toUtf8CodePoints
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import okio.utf8Size

object Parsers {

    @JvmStatic
    fun alpha0(): Parser<String> {
        return satisfy0Internal { c, _ -> c.isAlpha() }
    }

    @JvmStatic
    fun alpha1(): Parser<String> {
        return satisfy1Internal(ParseError.Alpha) { c, _ -> c.isAlpha() }
    }

    @JvmStatic
    fun alphaNumeric0(): Parser<String> {
        return satisfy0Internal { c, _ -> c.isAlphaNumeric() }
    }

    @JvmStatic
    fun alphaNumeric1(): Parser<String> {
        return satisfy1Internal(ParseError.AlphaNumeric) { c, _ -> c.isAlphaNumeric() }
    }

    @JvmStatic
    fun anyCodePoint(): Parser<Utf8CodePoint> {
        return Parser.peeking { input: Input ->
            val cp = input.readUtf8CodePoint()
            when {
                Input.isEof(cp) -> ParseResult.error(input, ParseError.Eof)
                else -> ParseResult.ok(input, cp)
            }
        }
    }

    @JvmStatic
    fun codePoint(expected: Char): Parser<Utf8CodePoint> {
        return codePoint(expected.toUtf8CodePoint())
    }

    @JvmStatic
    fun codePoint(expected: Utf8CodePoint): Parser<Utf8CodePoint> {
        return Parser.peeking(satisfyInternal(ParseError.CodePoint) { codePoint -> codePoint == expected })
    }

    @JvmStatic
    fun crlf(): Parser<String> {
        return Parser.peeking(tagInternal("\r\n", ParseError.CrLf) { c1, c2 -> c1 == c2 })
    }

    @JvmStatic
    fun digit0(): Parser<String> {
        return satisfy0Internal { c, _ -> c.isDigit() }
    }

    @JvmStatic
    fun digit1(): Parser<String> {
        return satisfy1Internal(ParseError.Digit) { c, _ -> c.isDigit() }
    }

    @JvmStatic
    fun hexDigit0(): Parser<String> {
        return satisfy0Internal { c, _ -> c.isHexDigit() }
    }

    @JvmStatic
    fun hexDigit1(): Parser<String> {
        return satisfy1Internal(ParseError.HexDigit) { c, _ -> c.isHexDigit() }
    }

    @JvmStatic
    fun lineEnding(): Parser<String> {
        return Parser.peeking { input ->
            val first = input.readUtf8CodePoint()
            when {
                Input.isEof(first) -> ParseResult.error(input, ParseError.CrLf)
                first.isChar('\n') -> ParseResult.ok(input, "\n")
                first.isChar('\r') -> {
                    val second = input.readUtf8CodePoint()
                    when {
                        Input.isEof(second) -> ParseResult.error(input, ParseError.CrLf)
                        second.isChar('\n') -> ParseResult.ok(input, "\r\n")
                        else -> ParseResult.error(input, ParseError.CrLf)
                    }
                }
                else -> ParseResult.error(input, ParseError.CrLf)
            }
        }
    }

    @JvmStatic
    fun multiSpace0(): Parser<String> {
        return satisfy0Internal { c, _ -> c.isMultiSpace() }
    }

    @JvmStatic
    fun multiSpace1(): Parser<String> {
        return satisfy1Internal(ParseError.MultiSpace) { c, _ -> c.isMultiSpace() }
    }

    @JvmStatic
    fun newline(): Parser<Utf8CodePoint> {
        return codePoint('\n')
    }

    @JvmStatic
    fun noneOf(codePoints: String): Parser<Utf8CodePoint> {
        val codePointsSet = codePoints.toUtf8CodePoints().toSet()
        return Parser.peeking(
            satisfyInternal(ParseError.NoneOf) {
                cp ->
                !codePointsSet.contains(cp)
            }
        )
    }

    @JvmStatic
    fun notLineEnding(): Parser<String> {
        return satisfy0Internal { cp, _ -> cp.toChar().let { chr -> chr != '\n' && chr != '\r' } }
    }

    @JvmStatic
    fun octDigit0(): Parser<String> {
        return satisfy0Internal { cp, _ -> cp.isOctDigit() }
    }

    @JvmStatic
    fun octDigit1(): Parser<String> {
        return satisfy1Internal(ParseError.OctDigit) { cp, _ -> cp.isOctDigit() }
    }

    @JvmStatic
    fun oneOf(codePoints: String): Parser<Utf8CodePoint> {
        val codePointsSet = codePoints.toUtf8CodePoints().toSet()
        return Parser.peeking(
            satisfyInternal(ParseError.OneOf) { chr ->
                codePointsSet.contains(chr)
            }
        )
    }

    @JvmStatic
    fun satisfy(predicate: (Utf8CodePoint) -> Boolean): Parser<Utf8CodePoint> {
        return Parser.peeking(satisfyInternal(ParseError.Satisfy, predicate))
    }

    /**
     * Returns the longest input slice (if any) that matches the predicate.
     */
    @JvmStatic
    fun satisfy0(predicate: (Utf8CodePoint, Int) -> Boolean): Parser<String> {
        return satisfy0Internal(predicate)
    }

    /**
     * Returns the longest input slice (if any) that matches the predicate.
     */
    @JvmStatic
    fun satisfy1(predicate: (Utf8CodePoint, Int) -> Boolean): Parser<String> {
        return satisfy1Internal(ParseError.Satisfy, predicate)
    }

    @JvmStatic
    fun space0(): Parser<String> {
        return satisfy0Internal { cp, _ -> cp.isSpace() }
    }

    @JvmStatic
    fun space1(): Parser<String> {
        return satisfy1Internal(ParseError.Space) { cp, _ -> cp.isSpace() }
    }

    @JvmStatic
    fun success(): Parser<Unit> {
        return Parser { input -> ParseResult.ok(input, Unit) }
    }

    @JvmStatic
    fun tab(): Parser<Utf8CodePoint> = codePoint('\t')

    @JvmStatic
    fun tag(tag: String): Parser<String> {
        return Parser.peeking(tagInternal(tag, ParseError.Tag) { l, r -> l == r })
    }

    @JvmStatic
    fun tagNoCase(tag: String): Parser<String> {
        return Parser.peeking(tagInternal(tag, ParseError.Tag) { l, r -> l.utf8().equals(r.utf8(), ignoreCase = true) })
    }

    /**
     * Takes exactly the given number of bytes from the input and returns them as result.
     * Fails with [ParseError.Eof] in case there is not enough input.
     */
    @JvmStatic
    fun takeBytes(count: Long): Parser<ByteArray> {
        require(count >= 0) { "count must not be negative but was $count" }

        return Parser { input ->
            val peek = input.peek()
            val bytes = peek.readByteString(count)

            when {
                bytes.size < count -> ParseResult.error(input, ParseError.Eof)
                else -> ParseResult.ok(peek, bytes.toByteArray())
            }
        }
    }
}

private fun tagInternal(tag: String, error: ParseError, equality: (ByteString, ByteString) -> Boolean): Parser<String> {

    if (tag.isEmpty()) {
        throw IllegalArgumentException("empty tags are not supported")
    }

    val tagBytes = tag.encodeUtf8()

    return Parser { input ->

        val buf = input.readByteString(tagBytes.size.toLong())

        when {
            !equality(tagBytes, buf) -> ParseResult.error(input, error)
            else -> ParseResult.ok(input, buf.utf8())
        }
    }
}

private fun satisfyInternal(error: ParseError, predicate: (Utf8CodePoint) -> Boolean): Parser<Utf8CodePoint> {
    return Parser { input ->
        val codePoint = input.readUtf8CodePoint()
        when {
            Input.isEof(codePoint) -> ParseResult.error(input, error)
            !predicate(codePoint) -> ParseResult.error(input, error)
            else -> ParseResult.ok(input, codePoint)
        }
    }
}

private fun satisfy0Internal(predicate: (Utf8CodePoint, Int) -> Boolean): Parser<String> {
    return Parser { input ->

        val output = StringBuilder()
        val peek = input.peek()
        var codePoint: Utf8CodePoint
        var index = 0

        while (!Input.isEof(peek.readUtf8CodePoint().also { codePoint = it })) {

            if (!predicate(codePoint, index++)) {
                val result = output.toString()
                input.skip(result.utf8Size())
                return@Parser ParseResult.ok(input, result)
            }

            output.appendCodePoint(codePoint)
        }

        val result = output.toString()
        input.skip(result.utf8Size())
        ParseResult.ok(input, result)
    }
}

private fun satisfy1Internal(error: ParseError, predicate: (Utf8CodePoint, Int) -> Boolean): Parser<String> {
    return Parser { input ->
        val peek = input.peek()
        var codePoint = peek.readUtf8CodePoint()

        if (Input.isEof(codePoint)) {
            return@Parser ParseResult.error<String>(input, error)
        }

        if (!predicate(codePoint, 0)) {
            return@Parser ParseResult.error<String>(input, error)
        }

        val output = StringBuilder().appendCodePoint(codePoint)
        var index = 1

        while (!Input.isEof(peek.readUtf8CodePoint().also { codePoint = it })) {
            if (!predicate(codePoint, index++)) {
                if (output.isEmpty()) {
                    return@Parser ParseResult.error<String>(input, error)
                }
                val result = output.toString()
                input.skip(result.utf8Size())
                return@Parser ParseResult.ok(input, result)
            }
            output.appendCodePoint(codePoint)
        }

        if (output.isEmpty()) {
            return@Parser ParseResult.error<String>(input, error)
        }

        val result = output.toString()
        input.skip(result.utf8Size())
        ParseResult.ok(input, result)
    }
}
