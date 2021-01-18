@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.hexDigit0
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class hexDigit0 {

    private val parser = hexDigit0()

    @Test
    fun `should parse single matching char`() {
        assertThatParseResult(parser.parse(Input.of("7g")))
            .isOk("7")
            .hasRemainingInput("g")
    }

    @Test
    fun `should parse multiple matching chars`() {
        assertThatParseResult(parser.parse(Input.of("0123456789abcdefABCDEFg")))
            .isOk("0123456789abcdefABCDEF")
            .hasRemainingInput("g")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse(Input.of("xFF")))
            .isOk("")
            .hasRemainingInput("xFF")
    }

    @Test
    fun `should accept empty input`() {
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
