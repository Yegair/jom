@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.alphaNumeric1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class alphaNumeric1 {

    private val parser = alphaNumeric1()

    @Test
    fun `should parse single matching alphabetic char`() {
        assertThatParseResult(parser.parse("a;"))
            .isOk("a")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single matching numeric char`() {
        assertThatParseResult(parser.parse("6;"))
            .isOk("6")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse multiple matching alphabetic chars`() {
        assertThatParseResult(parser.parse("azAZ;"))
            .isOk("azAZ")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse multiple matching numeric chars`() {
        assertThatParseResult(parser.parse("0123456789;"))
            .isOk("0123456789")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse multiple matching alphanumeric chars`() {
        assertThatParseResult(parser.parse("012345azAZ6789;"))
            .isOk("012345azAZ6789")
            .hasRemainingInput(";")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse("&0f"))
            .isError(ParseError.AlphaNumeric)
            .hasRemainingInput("&0f")
    }

    @Test
    fun `should accept empty input`() {
        assertThatParseResult(parser.parse(""))
            .isError(ParseError.AlphaNumeric)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.AlphaNumeric)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
