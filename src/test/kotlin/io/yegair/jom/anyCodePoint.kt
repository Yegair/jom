@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.anyCodePoint
import io.yegair.jom.Utf8CodePoints.toUtf8CodePoint
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class anyCodePoint {

    private val parser = anyCodePoint()

    @Test
    fun `should parse single 1-byte code point`() {
        assertThatParseResult(parser.parse("?;"))
            .isOk('?'.toUtf8CodePoint())
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single 2-byte code point`() {
        assertThatParseResult(parser.parse("æ;"))
            .isOk('æ'.toUtf8CodePoint())
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single 3-byte code point`() {
        assertThatParseResult(parser.parse("こ;"))
            .isOk('こ'.toUtf8CodePoint())
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single 4-byte code point`() {
        assertThatParseResult(parser.parse("🌍;"))
            .isOk("🌍".toUtf8CodePoint())
            .hasRemainingInput(";")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(parser.parse(""))
            .isError(ParseError.Eof)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.Eof)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
