@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.digit1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class digit1 {

    private val parser = digit1()

    @Test
    fun `should parse single matching char`() {
        assertThatParseResult(parser.parse(Input.of("9;")))
            .isOk("9")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse multiple matching chars`() {
        assertThatParseResult(parser.parse(Input.of("42;")))
            .isOk("42")
            .hasRemainingInput(";")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse(Input.of("M4")))
            .isError(ParseError.Digit)
            .hasRemainingInput("M4")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(parser.parse(Input.of("")))
            .isError(ParseError.Digit)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(Input.of(byteArrayOf(0xc3.toByte()))))
            .isError(ParseError.Digit)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
