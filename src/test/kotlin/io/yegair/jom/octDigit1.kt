@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.octDigit1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class octDigit1 {

    private val parser = octDigit1()

    @Test
    fun `should parse single matching char`() {
        assertThatParseResult(parser.parse("7g"))
            .isOk("7")
            .hasRemainingInput("g")
    }

    @Test
    fun `should parse multiple matching chars`() {
        assertThatParseResult(parser.parse("0123456789"))
            .isOk("01234567")
            .hasRemainingInput("89")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse("f13"))
            .isError(ParseError.OctDigit)
            .hasRemainingInput("f13")
    }

    @Test
    fun `should accept empty input`() {
        assertThatParseResult(parser.parse(""))
            .isError(ParseError.OctDigit)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.OctDigit)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
