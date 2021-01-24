@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.many1
import io.yegair.jom.Combinators.pair
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.digit0
import io.yegair.jom.TextParsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class many1 {

    @Test
    fun `should succeed if embedded parser can be applied once`() {
        val parser = many1(tag("abc"))

        assertThatParseResult(parser.parse("abc1"))
            .isOk(listOf("abc"))
            .hasRemainingInput("1")
    }

    @Test
    fun `should succeed if embedded parser can be applied more than once`() {
        val parser = many1(tag("abc"))

        assertThatParseResult(parser.parse("abcabc;"))
            .isOk(listOf("abc", "abc"))
            .hasRemainingInput(";")
    }

    @Test
    fun `should succeed if embedded parser fails after succeeding once`() {
        val parser = many1(tag("abc"))

        assertThatParseResult(parser.parse("abcab3"))
            .isOk(listOf("abc"))
            .hasRemainingInput("ab3")
    }

    @Test
    fun `should fail if embedded parser can not be applied`() {
        val parser = many1(tag("abc"))

        assertThatParseResult(parser.parse("ab123"))
            .isError(ParseError.Tag)
            .hasRemainingInput("ab123")
    }

    @Test
    fun `should fail if embedded parser accepts empty input`() {
        val parser = many1(alpha0())

        assertThatParseResult(parser.parse(""))
            .isError(ParseError.Many)
            .hasRemainingInput("")
    }

    @Test
    fun `should detect non terminating parser`() {
        val parser = many1(pair(alpha0(), digit0()))

        assertThatParseResult(parser.parse("R2D"))
            .isError(ParseError.Many)
            .hasRemainingInput("R2D")
    }
}
