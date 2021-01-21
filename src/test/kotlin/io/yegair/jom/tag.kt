@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.TextParsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class tag {

    @Test
    fun `should parse 1-byte code points`() {
        assertThatParseResult(tag("Hello").parse(Input.of("Hello World")))
            .isOk("Hello")
            .hasRemainingInput(" World")
    }

    @Test
    fun `should not ignore case for 1-byte code points`() {
        assertThatParseResult(tag("Hello").parse(Input.of("hello world")))
            .isError(ParseError.Tag)
            .hasRemainingInput("hello world")
    }

    @Test
    fun `should parse 2-byte code points`() {
        assertThatParseResult(tag("Καλημέρα").parse(Input.of("Καλημέρα κόσμε")))
            .isOk("Καλημέρα")
            .hasRemainingInput(" κόσμε")
    }

    @Test
    fun `should not ignore case for 2-byte code points`() {
        assertThatParseResult(tag("Καλημέρα").parse(Input.of("καλημέρα ΚΌΣΜΕ")))
            .isError(ParseError.Tag)
            .hasRemainingInput("καλημέρα ΚΌΣΜΕ")
    }

    @Test
    fun `should parse 3-byte code points`() {
        assertThatParseResult(tag("こんにちは").parse(Input.of("こんにちは 世界")))
            .isOk("こんにちは")
            .hasRemainingInput(" 世界")
    }

    @Test
    fun `should parse 4-byte code points`() {
        assertThatParseResult(tag("👆👏").parse(Input.of("👆👏 🌍")))
            .isOk("👆👏")
            .hasRemainingInput(" 🌍")
    }

    @Test
    fun `should parse first of multiple tags`() {
        assertThatParseResult(tag("foo").parse(Input.of("foofoo")))
            .isOk("foo")
            .hasRemainingInput("foo")
    }

    @Test
    fun `should not parse incomplete tag`() {
        assertThatParseResult(tag("foo").parse(Input.of("fo")))
            .isError(ParseError.Tag)
            .hasRemainingInput("fo")
    }

    @Test
    fun `should not parse empty input`() {
        assertThatParseResult(tag("bar").parse(Input.of("")))
            .isError(ParseError.Tag)
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(tag("æ").parse(Input.of(byteArrayOf(0xc3.toByte()))))
            .isError(ParseError.Tag)
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
