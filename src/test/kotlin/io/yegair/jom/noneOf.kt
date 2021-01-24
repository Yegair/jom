@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.noneOf
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class noneOf {

    @Test
    fun `should parse matching 1-byte utf-8 codepoint`() {
        assertThatParseResult(noneOf("bc").parse("ab"))
            .isOk("a")
            .hasRemainingInput("b")
    }

    @Test
    fun `should parse matching 2-byte utf-8 codepoint`() {
        assertThatParseResult(noneOf("æȻ").parse("ǿȻ"))
            .isOk("ǿ")
            .hasRemainingInput("Ȼ")
    }

    @Test
    fun `should parse matching 3-byte utf-8 codepoint`() {
        assertThatParseResult(noneOf("₤₿").parse("₵₿"))
            .isOk("₵")
            .hasRemainingInput("₿")
    }

    @Test
    fun `should parse matching 4-byte utf-8 codepoint`() {
        assertThatParseResult(noneOf("𓀠𓀡").parse("𓀓𓁏"))
            .isOk("𓀓")
            .hasRemainingInput("𓁏")
    }

    @Test
    fun `should not parse excluded 1-byte utf-8 codepoint`() {
        assertThatParseResult(noneOf("bc").parse("ba"))
            .isError(ParseError.NoneOf)
            .hasRemainingInput("ba")
    }

    @Test
    fun `should not parse excluded 2-byte utf-8 codepoint`() {
        assertThatParseResult(noneOf("æȻ").parse("æǿ"))
            .isError(ParseError.NoneOf)
            .hasRemainingInput("æǿ")
    }

    @Test
    fun `should not parse excluded 3-byte utf-8 codepoint`() {
        assertThatParseResult(noneOf("₤₿").parse("₤₵"))
            .isError(ParseError.NoneOf)
            .hasRemainingInput("₤₵")
    }

    @Test
    fun `should not parse excluded 4-byte utf-8 codepoint`() {
        assertThatParseResult(noneOf("𓀠𓀡").parse("𓀠𓁏"))
            .isError(ParseError.NoneOf)
            .hasRemainingInput("𓀠𓁏")
    }

    @Test
    fun `should fail for empty input`() {
        assertThatParseResult(noneOf("æ").parse(""))
            .isError(ParseError.NoneOf)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail for incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(noneOf("æ").parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.NoneOf)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
