@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.lineEnding
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class lineEnding {

    val parser = lineEnding()

    @Test
    fun `should parse single CRLF`() {
        assertThatParseResult(parser.parse("\r\nc"))
            .isOk("\r\n")
            .hasRemainingInput("c")
    }

    @Test
    fun `should parse single LF`() {
        assertThatParseResult(parser.parse("\nc"))
            .isOk("\n")
            .hasRemainingInput("c")
    }

    @Test
    fun `should not parse simple chars`() {
        assertThatParseResult(parser.parse("ab\r\nc"))
            .isError(ParseError.CrLf)
            .hasRemainingInput("ab\r\nc")
    }

    @Test
    fun `should not parse single CR`() {
        assertThatParseResult(parser.parse("\rc"))
            .isError(ParseError.CrLf)
            .hasRemainingInput("\rc")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(parser.parse(""))
            .isError(ParseError.CrLf)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail for incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.CrLf)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
