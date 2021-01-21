@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.digit0
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class digit0 {

    private val parser = digit0()

    @Test
    fun `should parse single matching char`() {
        assertThatParseResult(parser.parse(Input.of("3;")))
            .isOk("3")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse multiple matching chars`() {
        assertThatParseResult(parser.parse(Input.of("21c")))
            .isOk("21")
            .hasRemainingInput("c")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse(Input.of("a4")))
            .isOk("")
            .hasRemainingInput("a4")
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
