@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.count
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class count {

    @Test
    fun `should succeed if embedded parser can be applied exactly n times`() {
        val parser = count(tag("abc"), 2)

        assertThatParseResult(parser.parse("abcabc"))
            .isOk(listOf("abc", "abc"))
            .hasRemainingInput("")
    }

    @Test
    fun `should succeed if embedded parser could be applied more than n times`() {
        val parser = count(tag("abc"), 2)

        assertThatParseResult(parser.parse("abcabcabc"))
            .isOk(listOf("abc", "abc"))
            .hasRemainingInput("abc")
    }

    @Test
    fun `should succeed if embedded parser can be applied exactly n times with some input following`() {
        val parser = count(tag("abc"), 2)

        assertThatParseResult(parser.parse("abcabcab3"))
            .isOk(listOf("abc", "abc"))
            .hasRemainingInput("ab3")
    }

    @Test
    fun `should fail if embedded parser can only be applied less than n times`() {
        val parser = count(tag("abc"), 2)

        assertThatParseResult(parser.parse("abc123"))
            .isError(ParseError.Tag)
            .hasRemainingInput("abc123")
    }

    @Test
    fun `should fail if embedded parser fails at least once`() {
        val parser = count(tag("abc"), 2)

        assertThatParseResult(parser.parse("abcab3"))
            .isError(ParseError.Tag)
            .hasRemainingInput("abcab3")
    }

    @Test
    fun `should fail if embedded parser can not be applied at all`() {
        val parser = count(tag("abc"), 2)

        assertThatParseResult(parser.parse("123123"))
            .isError(ParseError.Tag)
            .hasRemainingInput("123123")
    }

    @Test
    fun `should fail if embedded parser can not be applied at all due to empty input`() {
        val parser = count(tag("abc"), 2)

        assertThatParseResult(parser.parse(""))
            .isError(ParseError.Tag)
            .hasRemainingInput("")
    }

    @Test
    fun `should parse empty input`() {
        val parser = count(alpha0(), 2)

        assertThatParseResult(parser.parse(""))
            .isOk(listOf("", ""))
            .hasRemainingInput("")
    }
}
