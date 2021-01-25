@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.mapOpt
import io.yegair.jom.Parsers.digit1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class mapOpt {

    val parser = mapOpt(digit1()) { it.toByteOrNull(10) }

    @Test
    fun `should map result when embedded parser succeeds`() {
        assertThatParseResult(parser.parse("123"))
            .isOk(123)
            .hasRemainingInput("")
    }

    @Test
    fun `should fail if embedded parser fails`() {
        assertThatParseResult(parser.parse("abc"))
            .isError(ParseError.Digit)
            .hasRemainingInput("abc")
    }

    @Test
    fun `should forward error when embedded parser fails`() {
        assertThatParseResult(parser.parse("123456"))
            .isError(ParseError.MapOpt)
            .hasRemainingInput("123456")
    }
}
