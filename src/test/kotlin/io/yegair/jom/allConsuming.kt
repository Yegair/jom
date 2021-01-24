@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.allConsuming
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class allConsuming {

    @Test
    fun `should succeed if text input is fully parsed`() {
        val parser = allConsuming(alpha1())

        assertThatParseResult(parser.parse("abcd"))
            .isOk("abcd")
            .hasRemainingInput("")
    }

    @Test
    fun `should fail if text input is not fully parsed`() {
        val parser = allConsuming(alpha1())

        assertThatParseResult(parser.parse("abcd;"))
            .isError(ParseError.Eof)
            .hasRemainingInput("abcd;")
    }

    @Test
    fun `should fail if embedded parser fails`() {
        val parser = allConsuming(alpha1())

        assertThatParseResult(parser.parse("123;"))
            .isError(ParseError.Alpha)
            .hasRemainingInput("123;")
    }

    @Test
    fun `should accept empty input`() {
        val parser = allConsuming(alpha0())

        assertThatParseResult(parser.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }
}
