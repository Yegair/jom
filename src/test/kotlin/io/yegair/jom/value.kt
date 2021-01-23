@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.value
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.alpha1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class value {

    @Test
    fun `should return value when embedded parser succeeds`() {
        val parser = value(1234, alpha1())

        assertThatParseResult(parser.parse("abcd"))
            .isOk(1234)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail when embedded parser fails`() {
        val parser = value(1234, alpha1())

        assertThatParseResult(parser.parse("123abcd;"))
            .isError(ParseError.Alpha)
            .hasRemainingInput("123abcd;")
    }

    @Test
    fun `should accept empty input when embedded parser accepts it`() {
        val parser = value(1234, alpha0())

        assertThatParseResult(parser.parse(""))
            .isOk(1234)
            .hasRemainingInput("")
    }

    @Test
    fun `should not accept empty input when embedded parser does not`() {
        val parser = value(1234, alpha1())

        assertThatParseResult(parser.parse(""))
            .isError(ParseError.Alpha)
            .hasRemainingInput("")
    }
}
