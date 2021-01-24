@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.oneOf
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class oneOf {

    @Test
    fun `should parse matching 1-byte utf-8 codepoint`() {
        assertThatParseResult(oneOf("bc").parse("bc"))
            .isOk("b")
            .hasRemainingInput("c")
    }

    @Test
    fun `should parse matching 2-byte utf-8 codepoint`() {
        assertThatParseResult(oneOf("æȻ").parse("æȻ"))
            .isOk("æ")
            .hasRemainingInput("Ȼ")
    }

    @Test
    fun `should parse matching 3-byte utf-8 codepoint`() {
        assertThatParseResult(oneOf("₤₿").parse("₤₿"))
            .isOk("₤")
            .hasRemainingInput("₿")
    }

    @Test
    fun `should parse matching 4-byte utf-8 codepoint`() {
        assertThatParseResult(oneOf("𓀠𓀡").parse("𓀠𓀡"))
            .isOk("𓀠")
            .hasRemainingInput("𓀡")
    }

    @Test
    fun `should not parse non matching 1-byte utf-8 codepoint`() {
        assertThatParseResult(oneOf("bc").parse("ab"))
            .isError(ParseError.OneOf)
            .hasRemainingInput("ab")
    }

    @Test
    fun `should not parse non matching 2-byte utf-8 codepoint`() {
        assertThatParseResult(oneOf("æȻ").parse("ǿæ"))
            .isError(ParseError.OneOf)
            .hasRemainingInput("ǿæ")
    }

    @Test
    fun `should not parse non matching 3-byte utf-8 codepoint`() {
        assertThatParseResult(oneOf("₤₿").parse("₵₤"))
            .isError(ParseError.OneOf)
            .hasRemainingInput("₵₤")
    }

    @Test
    fun `should not parse non matching 4-byte utf-8 codepoint`() {
        assertThatParseResult(oneOf("𓀠𓀡").parse("𓁏𓀠"))
            .isError(ParseError.OneOf)
            .hasRemainingInput("𓁏𓀠")
    }

    @Test
    fun `should fail for empty input`() {
        assertThatParseResult(oneOf("æ").parse(""))
            .isError(ParseError.OneOf)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail for incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(oneOf("æ").parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.OneOf)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
