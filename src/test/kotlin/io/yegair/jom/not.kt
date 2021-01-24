@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.not
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class not {

    @Test
    fun `should succeed when embedded parser fails`() {
        val parser = not(alpha1())

        assertThatParseResult(parser.parse("123"))
            .isOk(Unit)
            .hasRemainingInput("123")
    }

    @Test
    fun `should fail when embedded parser succeeds`() {
        val parser = not(alpha1())

        assertThatParseResult(parser.parse("abcd"))
            .isError(ParseError.Not)
            .hasRemainingInput("abcd")
    }
}
