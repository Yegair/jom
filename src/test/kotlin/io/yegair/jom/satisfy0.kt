@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.satisfy0
import io.yegair.jom.Utf8CodePoints.isLowerCase
import io.yegair.jom.Utf8CodePoints.isUpperCase
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class satisfy0 {

    @Test
    fun `should parse matching 1-byte utf-8 codepoints`() {
        assertThatParseResult(satisfy0 { cp, _ -> cp.isLowerCase() }.parse("bc"))
            .isOk("bc")
            .hasRemainingInput("")
    }

    @Test
    fun `should parse matching 2-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy0 { cp, _ -> cp.isLowerCase() }.parse("κόσμε."))
            .isOk("κόσμε")
            .hasRemainingInput(".")
    }

    @Test
    fun `should parse matching 3-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy0 { cp, _ -> !cp.isSpace() }.parse("こんに ちは"))
            .isOk("こんに")
            .hasRemainingInput(" ちは")
    }

    @Test
    fun `should parse matching 4-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy0 { cp, _ -> !cp.isSpace() }.parse("👆👏 🌍"))
            .isOk("👆👏")
            .hasRemainingInput(" 🌍")
    }

    @Test
    fun `should not parse non matching 1-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy0 { cp, _ -> cp.isUpperCase() }.parse("cb"))
            .isOk("")
            .hasRemainingInput("cb")
    }

    @Test
    fun `should not parse non matching 2-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy0 { cp, _ -> cp.isUpperCase() }.parse("κόσμε."))
            .isOk("")
            .hasRemainingInput("κόσμε.")
    }

    @Test
    fun `should not parse non matching 3-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy0 { cp, _ -> cp.isUpperCase() }.parse("こんに ちは"))
            .isOk("")
            .hasRemainingInput("こんに ちは")
    }

    @Test
    fun `should not parse non matching 4-byte utf-8 codepoint`() {
        assertThatParseResult(satisfy0 { cp, _ -> cp.isLowerCase() }.parse("𓀡𓀠"))
            .isOk("")
            .hasRemainingInput("𓀡𓀠")
    }

    @Test
    fun `should parse matching 1-byte utf-8 codepoints with index`() {
        val str = "bc"
        assertThatParseResult(satisfy0 { cp, index -> str.codePointAt(index) == cp }.parse(str))
            .isOk(str)
            .hasRemainingInput("")
    }

    @Test
    fun `should parse matching 2-byte utf-8 codepoint with index`() {
        val str = "κόσμε"
        assertThatParseResult(satisfy0 { cp, index -> str.codePointAt(index) == cp }.parse(str))
            .isOk(str)
            .hasRemainingInput("")
    }

    @Test
    fun `should parse matching 3-byte utf-8 codepoint with index`() {
        val str = "こんにちは"
        assertThatParseResult(satisfy0 { cp, index -> str.codePointAt(index) == cp }.parse(str))
            .isOk(str)
            .hasRemainingInput("")
    }

    @Test
    fun `should parse matching 4-byte utf-8 codepoint with index`() {
        val str = "👆👏🌍"
        assertThatParseResult(satisfy0 { cp, index -> str.codePointAt(index * 2) == cp }.parse(str))
            .isOk(str)
            .hasRemainingInput("")
    }

    @Test
    fun `should parse empty input`() {
        assertThatParseResult(satisfy0 { cp, _ -> cp.isLowerCase() }.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse incomplete two byte utf8 codepoint`() {
        // submit the first byte of a two-byte utf-8 code point
        // For example: [0xc3, 0xa6] = æ
        assertThatParseResult(satisfy0 { cp, _ -> cp.isLowerCase() }.parse(byteArrayOf(0xc3.toByte())))
            .isOk("")
            .hasRemainingInput(byteArrayOf(0xc3.toByte()))
    }
}
