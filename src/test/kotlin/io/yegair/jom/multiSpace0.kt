@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.multiSpace0
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class multiSpace0 {

    private val parser = multiSpace0()

    @Test
    fun `should parse single space`() {
        assertThatParseResult(parser.parse(" ;"))
            .isOk(" ")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single tab`() {
        assertThatParseResult(parser.parse("\t;"))
            .isOk("\t")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single LF`() {
        assertThatParseResult(parser.parse("\n;"))
            .isOk("\n")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single CR`() {
        assertThatParseResult(parser.parse("\r;"))
            .isOk("\r")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse single CRLF`() {
        assertThatParseResult(parser.parse("\r\n;"))
            .isOk("\r\n")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse consecutive whitespaces`() {
        assertThatParseResult(parser.parse(" \t\n\r\rFoobar"))
            .isOk(" \t\n\r\r")
            .hasRemainingInput("Foobar")
    }

    @Test
    fun `should not parse BACK SPACE`() {
        assertThatParseResult(parser.parse("\b;"))
            .isOk("")
            .hasRemainingInput("\b;")
    }

    @Test
    fun `should not parse FORM FEED (FF)`() {
        assertThatParseResult(parser.parse("\u000C;"))
            .isOk("")
            .hasRemainingInput("\u000C;")
    }

    @Test
    fun `should not parse VERTICAL TAB`() {
        assertThatParseResult(parser.parse("\u2B7F;"))
            .isOk("")
            .hasRemainingInput("\u2B7F;")
    }

    @Test
    fun `should not parse non matching char`() {
        assertThatParseResult(parser.parse("1 "))
            .isOk("")
            .hasRemainingInput("1 ")
    }

    @Test
    fun `should accept empty input`() {
        assertThatParseResult(parser.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(parser.parse(byteArrayOf(0xc3.toByte())))
            .isOk("")
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
