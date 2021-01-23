@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.peek
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.alpha1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class peek {

    @Test
    fun `should not consume input when embedded parser succeeds`() {
        val parser = peek(alpha1())

        assertThatParseResult(parser.parse("abcd;"))
            .isOk("abcd")
            .hasRemainingInput("abcd;")
    }

    @Test
    fun `should not consume input when embedded parser fails`() {
        val parser = peek(alpha1())

        assertThatParseResult(parser.parse("123;"))
            .isError(ParseError.Alpha)
            .hasRemainingInput("123;")
    }

    @Test
    fun `should not consume input when embedded parser succeeds with empty input`() {
        val parser = peek(alpha0())

        assertThatParseResult(parser.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }

    @Test
    fun `should not consume input when embedded parser fails with empty input`() {
        val parser = peek(alpha1())

        assertThatParseResult(parser.parse(""))
            .isError(ParseError.Alpha)
            .hasRemainingInput("")
    }
}
