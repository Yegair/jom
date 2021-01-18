@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Branches.permutation2
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.alpha1
import io.yegair.jom.TextParsers.digit0
import io.yegair.jom.TextParsers.digit1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class permutation2 {

    @Test
    fun `should parse valid input in parser order`() {
        val parser = permutation2(alpha1(), digit1())

        assertThatParseResult(parser.parse(Input.of("ab123")))
            .isOk(Pair("ab", "123"))
            .hasRemainingInput("")
    }

    @Test
    fun `should parse valid input in non parser order`() {
        val parser = permutation2(alpha1(), digit1())

        assertThatParseResult(parser.parse(Input.of("123ac")))
            .isOk(Pair("ac", "123"))
            .hasRemainingInput("")
    }

    @Test
    fun `should fail if first parser fails`() {
        val parser = permutation2(alpha1(), digit1())

        assertThatParseResult(parser.parse(Input.of(";123")))
            .isError
            .hasRemainingInput(";123")
    }

    @Test
    fun `should fail if second parser fails`() {
        val parser = permutation2(alpha1(), digit1())

        assertThatParseResult(parser.parse(Input.of("abc;")))
            .isError
            .hasRemainingInput("abc;")
    }

    @Test
    fun `should accept empty input`() {
        val parser = permutation2(alpha0(), digit0())

        assertThatParseResult(parser.parse(Input.of("")))
            .isOk(Pair("", ""))
            .hasRemainingInput("")
    }
}
