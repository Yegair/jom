@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.anyChar
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class anyChar {

    private val parser = anyChar()

    @Test
    fun `should parse single one byte char`() {
        assertThatParseResult(parser.parse("?;"))
            .isOk('?')
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single two byte char`() {
        assertThatParseResult(parser.parse("æ;"))
            .isOk('æ')
            .hasRemainingInput(";")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(parser.parse(""))
            .isError(ParseError.Eof)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.Eof)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
