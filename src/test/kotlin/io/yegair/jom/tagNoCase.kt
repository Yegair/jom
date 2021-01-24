@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.tagNoCase
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class tagNoCase {

    @Test
    fun `should parse 1-byte code points`() {
        assertThatParseResult(tagNoCase("Hello").parse("Hello World"))
            .isOk("Hello")
            .hasRemainingInput(" World")
    }

    @Test
    fun `should ignore case for 1-byte code points`() {
        assertThatParseResult(tagNoCase("Hello").parse("heLLo world"))
            .isOk("heLLo")
            .hasRemainingInput(" world")
    }

    @Test
    fun `should parse 2-byte code points`() {
        assertThatParseResult(tagNoCase("Καλημέρα").parse("Καλημέρα κόσμε"))
            .isOk("Καλημέρα")
            .hasRemainingInput(" κόσμε")
    }

    @Test
    fun `should not ignore case for 2-byte code points`() {
        assertThatParseResult(tagNoCase("Καλημέρα").parse("κΑλημΈρα ΚΌΣΜΕ"))
            .isOk("κΑλημΈρα")
            .hasRemainingInput(" ΚΌΣΜΕ")
    }

    @Test
    fun `should parse 3-byte code points`() {
        assertThatParseResult(tagNoCase("こんにちは").parse("こんにちは 世界"))
            .isOk("こんにちは")
            .hasRemainingInput(" 世界")
    }

    @Test
    fun `should parse 4-byte code points`() {
        assertThatParseResult(tagNoCase("👆👏").parse("👆👏 🌍"))
            .isOk("👆👏")
            .hasRemainingInput(" 🌍")
    }

    @Test
    fun `should parse first of multiple tags`() {
        assertThatParseResult(tagNoCase("foo").parse("FoOfoo"))
            .isOk("FoO")
            .hasRemainingInput("foo")
    }

    @Test
    fun `should not parse incomplete tag`() {
        assertThatParseResult(tagNoCase("foo").parse("fo"))
            .isError(ParseError.Tag)
            .hasRemainingInput("fo")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(tagNoCase("bar").parse(""))
            .isError(ParseError.Tag)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(tagNoCase("æ").parse(byteArrayOf(0xc3.toByte())))
            .isError(ParseError.Tag)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
