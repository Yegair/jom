@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.alpha1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class alpha1 {

    private val parser = alpha1()

    @Test
    fun `should parse single matching char`() {
        assertThatParseResult(parser.parse(Input.of("a;")))
            .isOk("a")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse multiple matching chars`() {
        assertThatParseResult(parser.parse(Input.of("azAZ;")))
            .isOk("azAZ")
            .hasRemainingInput(";")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse(Input.of("1a")))
            .isError(ParseError.Alpha)
            .hasRemainingInput("1a")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(parser.parse(Input.of("")))
            .isError(ParseError.Alpha)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(Input.of(byteArrayOf(0xc3.toByte()))))
            .isError(ParseError.Alpha)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
