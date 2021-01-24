@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.notLineEnding
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class notLineEnding {

    val parser = notLineEnding()

    @Test
    fun `should parse single char before CRLF`() {
        assertThatParseResult(parser.parse("a\r\nc"))
            .isOk("a")
            .hasRemainingInput("\r\nc")
    }

    @Test
    fun `should parse multiple chars before CRLF`() {
        assertThatParseResult(parser.parse("ab\r\nc"))
            .isOk("ab")
            .hasRemainingInput("\r\nc")
    }

    @Test
    fun `should parse no chars before CRLF`() {
        assertThatParseResult(parser.parse("\r\nc"))
            .isOk("")
            .hasRemainingInput("\r\nc")
    }

    @Test
    fun `should parse single char before LF`() {
        assertThatParseResult(parser.parse("a\nc"))
            .isOk("a")
            .hasRemainingInput("\nc")
    }

    @Test
    fun `should parse multiple chars before LF`() {
        assertThatParseResult(parser.parse("ab\nc"))
            .isOk("ab")
            .hasRemainingInput("\nc")
    }

    @Test
    fun `should parse no chars before LF`() {
        assertThatParseResult(parser.parse("\nc"))
            .isOk("")
            .hasRemainingInput("\nc")
    }

    @Test
    fun `should parse single char before CR`() {
        assertThatParseResult(parser.parse("a\rc"))
            .isOk("a")
            .hasRemainingInput("\rc")
    }

    @Test
    fun `should parse multiple chars before CR`() {
        assertThatParseResult(parser.parse("ab\rc"))
            .isOk("ab")
            .hasRemainingInput("\rc")
    }

    @Test
    fun `should parse no chars before CR`() {
        assertThatParseResult(parser.parse("\rc"))
            .isOk("")
            .hasRemainingInput("\rc")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(parser.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isOk("")
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
