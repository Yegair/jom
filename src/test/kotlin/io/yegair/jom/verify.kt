@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.verify
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class verify {

    @Test
    fun `should succeed when result is valid`() {
        val parser = verify(alpha1()) { it.length == 4 }

        assertThatParseResult(parser.parse("abcd"))
            .isOk("abcd")
            .hasRemainingInput("")
    }

    @Test
    fun `should fail when result is invalid`() {
        val parser = verify(alpha1()) { it.length == 4 }

        assertThatParseResult(parser.parse("abcde"))
            .isError(ParseError.Verify)
            .hasRemainingInput("abcde")
    }

    @Test
    fun `should fail when embedded parser fails`() {
        val parser = verify(alpha1()) { it.length == 4 }

        assertThatParseResult(parser.parse("123abcd;"))
            .isError(ParseError.Alpha)
            .hasRemainingInput("123abcd;")
    }

    @Test
    fun `should accept empty input`() {
        val parser = verify(alpha0()) { it.length == 0 }

        assertThatParseResult(parser.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }
}
