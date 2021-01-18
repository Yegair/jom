@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.alphaNumeric0
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class alphaNumeric0 {

    private val parser = alphaNumeric0()

    @Test
    fun `should parse single matching alphabetic char`() {
        assertThatParseResult(parser.parse(Input.of("a;")))
            .isOk("a")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single matching numeric char`() {
        assertThatParseResult(parser.parse(Input.of("6;")))
            .isOk("6")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse multiple matching alphabetic chars`() {
        assertThatParseResult(parser.parse(Input.of("azAZ;")))
            .isOk("azAZ")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse multiple matching numeric chars`() {
        assertThatParseResult(parser.parse(Input.of("0123456789;")))
            .isOk("0123456789")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse multiple matching alphanumeric chars`() {
        assertThatParseResult(parser.parse(Input.of("012345azAZ6789;")))
            .isOk("012345azAZ6789")
            .hasRemainingInput(";")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse(Input.of("&0f")))
            .isOk("")
            .hasRemainingInput("&0f")
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
