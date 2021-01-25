package io.yegair.jom

import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import okio.utf8Size

object Parsers {

    @JvmStatic
    fun alpha0(): Parser<String> {
        return satisfy0Internal { c -> c.isAlpha() }
    }

    @JvmStatic
    fun alpha1(): Parser<String> {
        return satisfy1Internal(ParseError.Alpha) { c -> c.isAlpha() }
    }

    @JvmStatic
    fun alphaNumeric0(): Parser<String> {
        return satisfy0Internal { c -> c.isAlphaNumeric() }
    }

    @JvmStatic
    fun alphaNumeric1(): Parser<String> {
        return satisfy1Internal(ParseError.AlphaNumeric) { c -> c.isAlphaNumeric() }
    }

    @JvmStatic
    fun anyChar(): Parser<Char> {
        return Parser.peeking { input: Input ->
            val chr = input.readUtf8CodePoint()
            when {
                Input.isEof(chr) -> ParseResult.error(input, ParseError.Eof)
                else -> ParseResult.ok(input, chr.toChar())
            }
        }
    }

    @JvmStatic
    fun chr(expected: Char): Parser<Char> {
        return Combinators.map(codePoint(expected.toUtf8CodePoint())) { codePoint -> codePoint.toChar() }
    }

    @JvmStatic
    fun codePoint(expected: Utf8CodePoint): Parser<Utf8CodePoint> {
        return Parser.peeking(satisfyInternal(ParseError.Char) { codePoint -> codePoint == expected })
    }

    @JvmStatic
    fun crlf(): Parser<String> {
        return Parser.peeking(tagInternal("\r\n", ParseError.CrLf) { c1, c2 -> c1 == c2 })
    }

    @JvmStatic
    fun digit0(): Parser<String> {
        return satisfy0Internal { c -> c.isDigit() }
    }

    @JvmStatic
    fun digit1(): Parser<String> {
        return satisfy1Internal(ParseError.Digit) { c -> c.isDigit() }
    }

    @JvmStatic
    fun hexDigit0(): Parser<String> {
        return satisfy0Internal { c -> c.isHexDigit() }
    }

    @JvmStatic
    fun hexDigit1(): Parser<String> {
        return satisfy1Internal(ParseError.HexDigit) { c -> c.isHexDigit() }
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
        return satisfy0Internal { c -> c.isMultiSpace() }
    }

    @JvmStatic
    fun multiSpace1(): Parser<String> {
        return satisfy1Internal(ParseError.MultiSpace) { c -> c.isMultiSpace() }
    }

    @JvmStatic
    fun newline(): Parser<Char> {
        return chr('\n')
    }

    @JvmStatic
    fun noneOf(codePoints: String): Parser<String> {
        val codePointsSet = codePoints.toUtf8CodePoints().toSet()
        val parser = Parser.peeking(satisfyInternal(ParseError.NoneOf) { cp -> !codePointsSet.contains(cp) })
        return Combinators.map(parser) { it.utf8() }
    }

    @JvmStatic
    fun notLineEnding(): Parser<String> {
        return satisfy0Internal { cp -> cp.toChar().let { chr -> chr != '\n' && chr != '\r' } }
    }

    @JvmStatic
    fun octDigit0(): Parser<String> {
        return satisfy0Internal { cp -> cp.isOctDigit() }
    }

    @JvmStatic
    fun octDigit1(): Parser<String> {
        return satisfy1Internal(ParseError.OctDigit) { cp -> cp.isOctDigit() }
    }

    @JvmStatic
    fun oneOf(codePoints: String): Parser<String> {
        val codePointsSet = codePoints.toUtf8CodePoints().toSet()
        val parser = Parser.peeking(satisfyInternal(ParseError.OneOf) { chr -> codePointsSet.contains(chr) })
        return Combinators.map(parser) { it.utf8() }
    }

    @JvmStatic
    fun satisfy(predicate: (Utf8CodePoint) -> Boolean): Parser<Utf8CodePoint> {
        return Parser.peeking(satisfyInternal(ParseError.Satisfy, predicate))
    }

    @JvmStatic
    fun space0(): Parser<String> {
        return satisfy0Internal { cp -> cp.isSpace() }
    }

    @JvmStatic
    fun space1(): Parser<String> {
        return satisfy1Internal(ParseError.Space) { cp -> cp.isSpace() }
    }

    @JvmStatic
    fun tab(): Parser<Char> = chr('\t')

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

private fun satisfy0Internal(predicate: (Utf8CodePoint) -> Boolean): Parser<String> {
    return Parser { input ->

        val output = StringBuilder()
        val peek = input.peek()
        var chr: Int

        while (!Input.isEof(peek.readUtf8CodePoint().also { chr = it })) {

            if (!predicate(chr)) {
                val result = output.toString()
                input.skip(result.utf8Size())
                return@Parser ParseResult.ok(input, result)
            }

            output.append(chr.toChar())
        }

        val result = output.toString()
        input.skip(result.utf8Size())
        ParseResult.ok(input, result)
    }
}

private fun satisfy1Internal(error: ParseError, predicate: (Utf8CodePoint) -> Boolean): Parser<String> {
    return Parser { input ->
        val peek = input.peek()
        var chr = peek.readUtf8CodePoint()

        if (Input.isEof(chr)) {
            return@Parser ParseResult.error<String>(input, error)
        }

        if (!predicate(chr)) {
            return@Parser ParseResult.error<String>(input, error)
        }

        val output = StringBuilder().append(chr.toChar())

        while (!Input.isEof(peek.readUtf8CodePoint().also { chr = it })) {
            if (!predicate(chr)) {
                if (output.isEmpty()) {
                    return@Parser ParseResult.error<String>(input, error)
                }
                val result = output.toString()
                input.skip(result.utf8Size())
                return@Parser ParseResult.ok(input, result)
            }
            output.append(chr.toChar())
        }

        if (output.isEmpty()) {
            return@Parser ParseResult.error<String>(input, error)
        }

        val result = output.toString()
        input.skip(result.utf8Size())
        ParseResult.ok(input, result)
    }
}
