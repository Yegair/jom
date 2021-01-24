@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.satisfy
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class satisfy {

    @Test
    fun `should parse matching 1-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy { it == 'b'.toInt() }.parse("bc"))
            .isOk('b'.toInt())
            .hasRemainingInput("c")
    }

    @Test
    fun `should parse matching 2-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy { it == 'æ'.toInt() }.parse("æȻ"))
            .isOk('æ'.toInt())
            .hasRemainingInput("Ȼ")
    }

    @Test
    fun `should parse matching 3-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy { it == '₤'.toInt() }.parse("₤₿"))
            .isOk('₤'.toInt())
            .hasRemainingInput("₿")
    }

    @Test
    fun `should parse matching 4-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy { it == Character.toCodePoint('\uD80C', '\uDC20') }.parse("𓀠𓀡"))
            .isOk(Character.toCodePoint('\uD80C', '\uDC20'))
            .hasRemainingInput("𓀡")
    }

    @Test
    fun `should not parse non matching 1-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy { it == 'b'.toInt() }.parse("cb"))
            .isError(ParseError.Satisfy)
            .hasRemainingInput("cb")
    }

    @Test
    fun `should not parse non matching 2-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy { it == 'æ'.toInt() }.parse("Ȼæ"))
            .isError(ParseError.Satisfy)
            .hasRemainingInput("Ȼæ")
    }

    @Test
    fun `should not parse non matching 3-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy { it == '₤'.toInt() }.parse("₿₤"))
            .isError(ParseError.Satisfy)
            .hasRemainingInput("₿₤")
    }

    @Test
    fun `should not parse non matching 4-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy { it == Character.toCodePoint('\uD80C', '\uDC20') }.parse("𓀡𓀠"))
            .isError(ParseError.Satisfy)
            .hasRemainingInput("𓀡𓀠")
    }

    @Test
    fun `should fail for empty input`() {
        assertThatParseResult(satisfy { it == 'æ'.toInt() }.parse(""))
            .isError(ParseError.Satisfy)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail for incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(satisfy { it == 'æ'.toInt() }.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.Satisfy)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
