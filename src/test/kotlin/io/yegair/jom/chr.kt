@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.chr
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class chr {

    @Test
    fun `should parse single one byte char`() {
        val parser = chr('c')

        assertThatParseResult(parser.parse("ccc"))
            .isOk('c')
            .hasRemainingInput("cc")
    }

    @Test
    fun `should parse single two byte char`() {
        val parser = chr('æ')

        assertThatParseResult(parser.parse("æææ"))
            .isOk('æ')
            .hasRemainingInput("ææ")
    }

    @Test
    fun `should fail for invalid input`() {
        val parser = chr('c')

        assertThatParseResult(parser.parse("abc"))
            .isError(ParseError.Char)
            .hasRemainingInput("abc")
    }

    @Test
    fun `should fail for empty input`() {
        val parser = chr('c')

        assertThatParseResult(parser.parse(""))
            .isError(ParseError.Char)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail for incomplete two byte utf8 codepoint`() {
        val parser = chr('c')

        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.Char)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
