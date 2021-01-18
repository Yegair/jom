@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.notLineEnding
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class notLineEnding {

    val parser = notLineEnding()

    @Test
    fun `should parse single char before CRLF`() {
        assertThatParseResult(parser.parse(Input.of("a\r\nc")))
            .isOk("a")
            .hasRemainingInput("\r\nc")
    }

    @Test
    fun `should parse multiple chars before CRLF`() {
        assertThatParseResult(parser.parse(Input.of("ab\r\nc")))
            .isOk("ab")
            .hasRemainingInput("\r\nc")
    }

    @Test
    fun `should parse no chars before CRLF`() {
        assertThatParseResult(parser.parse(Input.of("\r\nc")))
            .isOk("")
            .hasRemainingInput("\r\nc")
    }

    @Test
    fun `should parse single char before LF`() {
        assertThatParseResult(parser.parse(Input.of("a\nc")))
            .isOk("a")
            .hasRemainingInput("\nc")
    }

    @Test
    fun `should parse multiple chars before LF`() {
        assertThatParseResult(parser.parse(Input.of("ab\nc")))
            .isOk("ab")
            .hasRemainingInput("\nc")
    }

    @Test
    fun `should parse no chars before LF`() {
        assertThatParseResult(parser.parse(Input.of("\nc")))
            .isOk("")
            .hasRemainingInput("\nc")
    }

    @Test
    fun `should parse single char before CR`() {
        assertThatParseResult(parser.parse(Input.of("a\rc")))
            .isOk("a")
            .hasRemainingInput("\rc")
    }

    @Test
    fun `should parse multiple chars before CR`() {
        assertThatParseResult(parser.parse(Input.of("ab\rc")))
            .isOk("ab")
            .hasRemainingInput("\rc")
    }

    @Test
    fun `should parse no chars before CR`() {
        assertThatParseResult(parser.parse(Input.of("\rc")))
            .isOk("")
            .hasRemainingInput("\rc")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(parser.parse(Input.of("")))
            .isOk("")
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(Input.of(byteArrayOf(0xc3.toByte()))))
            .isOk("")
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
