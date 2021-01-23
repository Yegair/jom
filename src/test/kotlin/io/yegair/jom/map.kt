@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.map
import io.yegair.jom.TextParsers.digit1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class map {

    @Test
    fun `should map result when embedded parser succeeds`() {
        val parser = map(digit1()) { it.length }

        assertThatParseResult(parser.parse(Input.of("123456;")))
            .isOk(6)
            .hasRemainingInput(";")
    }

    @Test
    fun `should forward error when embedded parser fails`() {
        val parser = map(digit1()) { it.length }

        assertThatParseResult(parser.parse(Input.of("abc;")))
            .isError(ParseError.Digit)
            .hasRemainingInput("abc;")
    }
}
