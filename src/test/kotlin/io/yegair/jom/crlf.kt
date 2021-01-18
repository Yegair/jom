@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.crlf
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class crlf {

    val parser = crlf()

    @Test
    fun `should parse single CRLF`() {
        assertThatParseResult(parser.parse(Input.of("\r\nc")))
            .isOk("\r\n")
            .hasRemainingInput("c")
    }

    @Test
    fun `should not parse simple chars`() {
        assertThatParseResult(parser.parse(Input.of("ab\r\nc")))
            .isError(ParseError.CrLf)
            .hasRemainingInput("ab\r\nc")
    }

    @Test
    fun `should not parse LF`() {
        assertThatParseResult(parser.parse(Input.of("\nc")))
            .isError(ParseError.CrLf)
            .hasRemainingInput("\nc")
    }

    @Test
    fun `should not parse CR`() {
        assertThatParseResult(parser.parse(Input.of("\rc")))
            .isError(ParseError.CrLf)
            .hasRemainingInput("\rc")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(parser.parse(Input.of("")))
            .isError(ParseError.CrLf)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail for incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(Input.of(byteArrayOf(0xc3.toByte()))))
            .isError(ParseError.CrLf)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
