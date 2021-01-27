@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.foldMany1
import io.yegair.jom.Combinators.pair
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.digit0
import io.yegair.jom.Parsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class foldMany1 {

    @Test
    fun `should succeed if embedded parser can be applied once`() {
        val parser = foldMany1(tag("abc"), { listOf<String>() }) { list, item -> list + item }

        assertThatParseResult(parser.parse("abc1"))
            .isOk(listOf("abc"))
            .hasRemainingInput("1")
    }

    @Test
    fun `should succeed if embedded parser can be applied more than once`() {
        val parser = foldMany1(tag("abc"), { listOf<String>() }) { list, item -> list + item }

        assertThatParseResult(parser.parse("abcabc;"))
            .isOk(listOf("abc", "abc"))
            .hasRemainingInput(";")
    }

    @Test
    fun `should succeed if embedded parser fails after succeeding once`() {
        val parser = foldMany1(tag("abc"), { listOf<String>() }) { list, item -> list + item }

        assertThatParseResult(parser.parse("abcab3"))
            .isOk(listOf("abc"))
            .hasRemainingInput("ab3")
    }

    @Test
    fun `should fail if embedded parser can not be applied`() {
        val parser = foldMany1(tag("abc"), { listOf<String>() }) { list, item -> list + item }

        assertThatParseResult(parser.parse("ab123"))
            .isError(ParseError.Tag)
            .hasRemainingInput("ab123")
    }

    @Test
    fun `should fail if embedded parser accepts empty input`() {
        val parser = foldMany1(alpha0(), { listOf<String>() }) { list, item -> list + item }

        assertThatParseResult(parser.parse(""))
            .isError(ParseError.Many)
            .hasRemainingInput("")
    }

    @Test
    fun `should detect non terminating parser`() {
        val parser = foldMany1(
            pair(alpha0(), digit0()),
            { listOf<Pair<String, String>>() }
        ) { list, item -> list + item }

        assertThatParseResult(parser.parse("R2D"))
            .isError(ParseError.Many)
            .hasRemainingInput("R2D")
    }
}
