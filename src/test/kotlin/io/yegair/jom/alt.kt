@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Branches.alt
import io.yegair.jom.Combinators.allConsuming
import io.yegair.jom.Input.Companion.of
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.alpha1
import io.yegair.jom.TextParsers.digit1
import io.yegair.jom.test.ParseResultAssert
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class alt {

    @Test
    fun `should succeed if first embedded parser succeeds`() {
        val parser = alt(alpha1(), digit1())

        assertThatParseResult(parser.parse("abc42"))
            .isOk("abc")
            .hasRemainingInput("42")
    }

    @Test
    fun `should succeed if second embedded parser succeeds`() {
        val parser = alt(alpha1(), digit1())

        assertThatParseResult(parser.parse("123456foo"))
            .isOk("123456")
            .hasRemainingInput("foo")
    }

    @Test
    fun `should fail if no embedded parser succeeds`() {
        val parser = alt(alpha1(), digit1())

        assertThatParseResult(parser.parse(" "))
            .isError(ParseError.Digit)
            .hasRemainingInput(" ")
    }
}
