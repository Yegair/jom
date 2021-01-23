@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.not
import io.yegair.jom.TextParsers.alpha1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class not {

    @Test
    fun `should succeed when embedded parser fails`() {
        val parser = not(alpha1())

        assertThatParseResult(parser.parse(Input.of("123")))
            .isOk(Unit)
            .hasRemainingInput("123")
    }

    @Test
    fun `should fail when embedded parser succeeds`() {
        val parser = not(alpha1())

        assertThatParseResult(parser.parse(Input.of("abcd")))
            .isError(ParseError.Not)
            .hasRemainingInput("abcd")
    }
}
