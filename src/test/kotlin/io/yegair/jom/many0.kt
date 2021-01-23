@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.many0
import io.yegair.jom.Input.Companion.of
import io.yegair.jom.Sequences.pair
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.digit0
import io.yegair.jom.TextParsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class many0 {

    @Test
    fun `should succeed if embedded parser can be applied once`() {
        val parser = many0(tag("abc"))

        assertThatParseResult(parser.parse(of("abc1")))
            .isOk(listOf("abc"))
            .hasRemainingInput("1")
    }

    @Test
    fun `should succeed if embedded parser can be applied more than once`() {
        val parser = many0(tag("abc"))

        assertThatParseResult(parser.parse(of("abcabc;")))
            .isOk(listOf("abc", "abc"))
            .hasRemainingInput(";")
    }

    @Test
    fun `should succeed if embedded parser fails after succeeding once`() {
        val parser = many0(tag("abc"))

        assertThatParseResult(parser.parse(of("abcab3")))
            .isOk(listOf("abc"))
            .hasRemainingInput("ab3")
    }

    @Test
    fun `should succeed if embedded parser can not be applied`() {
        val parser = many0(tag("abc"))

        assertThatParseResult(parser.parse(of("123123")))
            .isOk(emptyList())
            .hasRemainingInput("123123")
    }

    @Test
    fun `should parse empty input`() {
        val parser = many0(tag("abc"))

        assertThatParseResult(parser.parse(of("")))
            .isOk(emptyList())
            .hasRemainingInput("")
    }

    @Test
    fun `should detect non terminating parser for empty input`() {
        val parser = many0(alpha0())

        assertThatParseResult(parser.parse(of("")))
            .isError(ParseError.Many)
            .hasRemainingInput("")
    }

    @Test
    fun `should detect non terminating parser`() {
        val parser = many0(pair(alpha0(), digit0()))

        assertThatParseResult(parser.parse(of("R2D")))
            .isError(ParseError.Many)
            .hasRemainingInput("R2D")
    }
}
