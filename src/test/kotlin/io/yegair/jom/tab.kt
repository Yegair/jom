@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.tab
import io.yegair.jom.Utf8CodePoints.toUtf8CodePoint
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class tab {

    private val parser = tab()

    @Test
    fun `should parse single tab`() {
        assertThatParseResult(parser.parse("\t;"))
            .isOk('\t'.toUtf8CodePoint())
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse first of multiple tabs`() {
        assertThatParseResult(parser.parse("\t\t\t;"))
            .isOk('\t'.toUtf8CodePoint())
            .hasRemainingInput("\t\t;")
    }

    @Test
    fun `should not parse single space`() {
        assertThatParseResult(parser.parse(" ;"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput(" ;")
    }

    @Test
    fun `should not parse single LF`() {
        assertThatParseResult(parser.parse("\n;"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("\n;")
    }

    @Test
    fun `should not parse single CR`() {
        assertThatParseResult(parser.parse("\r;"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("\r;")
    }

    @Test
    fun `should not parse single CRLF`() {
        assertThatParseResult(parser.parse("\r\n;"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("\r\n;")
    }

    @Test
    fun `should not parse consecutive whitespace chars`() {
        assertThatParseResult(parser.parse("\t \n\r"))
            .isOk('\t'.toUtf8CodePoint())
            .hasRemainingInput(" \n\r")
    }

    @Test
    fun `should not parse BACK SPACE`() {
        assertThatParseResult(parser.parse("\b;"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("\b;")
    }

    @Test
    fun `should not parse FORM FEED (FF)`() {
        assertThatParseResult(parser.parse("\u000C;"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("\u000C;")
    }

    @Test
    fun `should not parse VERTICAL TAB`() {
        assertThatParseResult(parser.parse("\u2B7F;"))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("\u2B7F;")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse("1 "))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("1 ")
    }

    @Test
    fun `should accept empty input`() {
        assertThatParseResult(parser.parse(""))
            .isError(ParseError.CodePoint)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.CodePoint)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
