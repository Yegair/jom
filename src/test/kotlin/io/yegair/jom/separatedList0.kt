@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.opt
import io.yegair.jom.Combinators.separatedList0
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.digit0
import io.yegair.jom.Parsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class separatedList0 {

    @Test
    fun `should succeed if embedded parser can be applied once`() {
        val parser = separatedList0(tag("|"), tag("abc"))

        assertThatParseResult(parser.parse("abc1"))
            .isOk(listOf("abc"))
            .hasRemainingInput("1")
    }

    @Test
    fun `should succeed if input starts with separator`() {
        val parser = separatedList0(tag("|"), tag("abc"))

        assertThatParseResult(parser.parse("|abc|abc1"))
            .isOk(emptyList())
            .hasRemainingInput("|abc|abc1")
    }

    @Test
    fun `should succeed if embedded parser can be applied more than once`() {
        val parser = separatedList0(tag("|"), tag("abc"))

        assertThatParseResult(parser.parse("abc|abc|abc;"))
            .isOk(listOf("abc", "abc", "abc"))
            .hasRemainingInput(";")
    }

    @Test
    fun `should succeed if embedded parser fails after succeeding once`() {
        val parser = separatedList0(tag("|"), tag("abc"))

        assertThatParseResult(parser.parse("abc|ab3"))
            .isOk(listOf("abc"))
            .hasRemainingInput("|ab3")
    }

    @Test
    fun `should succeed if embedded parser can not be applied`() {
        val parser = separatedList0(tag("|"), tag("abc"))

        assertThatParseResult(parser.parse("123|123"))
            .isOk(emptyList())
            .hasRemainingInput("123|123")
    }

    @Test
    fun `should parse empty input`() {
        val parser = separatedList0(tag("|"), tag("abc"))

        assertThatParseResult(parser.parse(""))
            .isOk(emptyList())
            .hasRemainingInput("")
    }

    @Test
    fun `should detect non terminating parser for empty input`() {
        val parser = separatedList0(digit0(), alpha0())

        assertThatParseResult(parser.parse(""))
            .isError(ParseError.Many)
            .hasRemainingInput("")
    }

    @Test
    fun `should detect non terminating parser`() {
        val parser = separatedList0(opt(tag("|")), opt(tag("abc")))

        assertThatParseResult(parser.parse("R2D"))
            .isError(ParseError.Many)
            .hasRemainingInput("R2D")
    }
}
