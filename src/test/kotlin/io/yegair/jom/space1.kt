@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.space1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class space1 {

    private val parser = space1()

    @Test
    fun `should parse single space`() {
        assertThatParseResult(parser.parse(Input.of(" ;")))
            .isOk(" ")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single tab`() {
        assertThatParseResult(parser.parse(Input.of("\t;")))
            .isOk("\t")
            .hasRemainingInput(";")
    }

    @Test
    fun `should not parse single LF`() {
        assertThatParseResult(parser.parse(Input.of("\n;")))
            .isError(ParseError.Space)
            .hasRemainingInput("\n;")
    }

    @Test
    fun `should not parse single CR`() {
        assertThatParseResult(parser.parse(Input.of("\r;")))
            .isError(ParseError.Space)
            .hasRemainingInput("\r;")
    }

    @Test
    fun `should not parse single CRLF`() {
        assertThatParseResult(parser.parse(Input.of("\r\n;")))
            .isError(ParseError.Space)
            .hasRemainingInput("\r\n;")
    }

    @Test
    fun `should not parse consecutive control chars`() {
        assertThatParseResult(parser.parse(Input.of(" \t\n\r")))
            .isOk(" \t")
            .hasRemainingInput("\n\r")
    }

    @Test
    fun `should not parse BACK SPACE`() {
        assertThatParseResult(parser.parse(Input.of("\b;")))
            .isError(ParseError.Space)
            .hasRemainingInput("\b;")
    }

    @Test
    fun `should not parse FORM FEED (FF)`() {
        assertThatParseResult(parser.parse(Input.of("\u000C;")))
            .isError(ParseError.Space)
            .hasRemainingInput("\u000C;")
    }

    @Test
    fun `should not parse VERTICAL TAB`() {
        assertThatParseResult(parser.parse(Input.of("\u2B7F;")))
            .isError(ParseError.Space)
            .hasRemainingInput("\u2B7F;")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse(Input.of("1 ")))
            .isError(ParseError.Space)
            .hasRemainingInput("1 ")
    }

    @Test
    fun `should accept empty input`() {
        assertThatParseResult(parser.parse(Input.of("")))
            .isError(ParseError.Space)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(Input.of(byteArrayOf(0xc3.toByte()))))
            .isError(ParseError.Space)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
