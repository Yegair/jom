@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.newline
import io.yegair.jom.Utf8CodePoints.toUtf8CodePoint
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class newline {

    val parser = newline()

    @Test
    fun `should parse single LF`() {
        assertThatParseResult(parser.parse("\nc"))
            .isOk('\n'.toUtf8CodePoint())
            .hasRemainingInput("c")
    }

    @Test
    fun `should not parse simple chars`() {
        assertThatParseResult(parser.parse("ab\nc"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("ab\nc")
    }

    @Test
    fun `should not parse single CRLF`() {
        assertThatParseResult(parser.parse("\r\nc"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("\r\nc")
    }

    @Test
    fun `should not parse single CR`() {
        assertThatParseResult(parser.parse("\rc"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("\rc")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(parser.parse(""))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail for incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.CodePoint)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
