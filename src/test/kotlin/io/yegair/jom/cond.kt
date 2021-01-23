@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.cond
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.alpha1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class cond {

    @Test
    fun `should apply parser if condition is met`() {
        val parser = cond(true, alpha1())

        assertThatParseResult(parser.parse(Input.of("abcd")))
            .isOk("abcd")
            .hasRemainingInput("")
    }

    @Test
    fun `should not apply parser if condition is not met`() {
        val parser = cond(false, alpha1())

        assertThatParseResult(parser.parse(Input.of("abcd")))
            .isOk(null)
            .hasRemainingInput("abcd")
    }

    @Test
    fun `should fail if condition is met and embedded parser fails`() {
        val parser = cond(true, alpha1())

        assertThatParseResult(parser.parse(Input.of("42Fu")))
            .isError(ParseError.Alpha)
            .hasRemainingInput("42Fu")
    }

    @Test
    fun `should not fail if condition is not met and embedded parser fails`() {
        val parser = cond(false, alpha1())

        assertThatParseResult(parser.parse(Input.of("42Fu")))
            .isOk(null)
            .hasRemainingInput("42Fu")
    }

    @Test
    fun `should accept empty input if condition is met`() {
        val parser = cond(true, alpha0())

        assertThatParseResult(parser.parse(Input.of("")))
            .isOk("")
            .hasRemainingInput("")
    }

    @Test
    fun `should accept empty input if condition is not met`() {
        val parser = cond(false, alpha0())

        assertThatParseResult(parser.parse(Input.of("")))
            .isOk(null)
            .hasRemainingInput("")
    }
}
