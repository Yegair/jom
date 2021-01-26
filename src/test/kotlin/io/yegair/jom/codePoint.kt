@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.codePoint
import io.yegair.jom.Utf8CodePoints.toUtf8CodePoint
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class codePoint {

    @Test
    fun `should parse single 1-byte code point`() {
        val parser = codePoint('c')

        assertThatParseResult(parser.parse("ccc"))
            .isOk('c'.toUtf8CodePoint())
            .hasRemainingInput("cc")
    }

    @Test
    fun `should parse single 2-byte code point`() {
        val parser = codePoint('æ')

        assertThatParseResult(parser.parse("æææ"))
            .isOk('æ'.toUtf8CodePoint())
            .hasRemainingInput("ææ")
    }

    @Test
    fun `should parse single 3-byte code point`() {
        val parser = codePoint('こ')

        assertThatParseResult(parser.parse("こ;"))
            .isOk('こ'.toUtf8CodePoint())
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single 4-byte code point`() {
        val parser = codePoint("🌍".toUtf8CodePoint())

        assertThatParseResult(parser.parse("🌍;"))
            .isOk("🌍".toUtf8CodePoint())
            .hasRemainingInput(";")
    }

    @Test
    fun `should fail for invalid input`() {
        val parser = codePoint('c')

        assertThatParseResult(parser.parse("abc"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("abc")
    }

    @Test
    fun `should fail for empty input`() {
        val parser = codePoint('c')

        assertThatParseResult(parser.parse(""))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail for incomplete two byte utf8 codepoint`() {
        val parser = codePoint('c')

        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.CodePoint)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
