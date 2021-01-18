@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Branches.permutation3
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.alpha1
import io.yegair.jom.TextParsers.chr
import io.yegair.jom.TextParsers.digit0
import io.yegair.jom.TextParsers.digit1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class permutation3 {

    @Test
    fun `should parse valid input in parser order`() {
        val parser = permutation3(alpha1(), digit1(), chr('c'))

        assertThatParseResult(parser.parse(Input.of("ab123cd")))
            .isOk(Triple("ab", "123", 'c'))
            .hasRemainingInput("d")
    }

    @Test
    fun `should parse valid input in non parser order`() {
        val parser = permutation3(alpha1(), digit1(), chr('c'))

        assertThatParseResult(parser.parse(Input.of("c123abcd")))
            .isOk(Triple("abcd", "123", 'c'))
            .hasRemainingInput("")
    }

    @Test
    fun `should fail if first parser fails`() {
        val parser = permutation3(alpha1(), digit1(), chr('c'))

        assertThatParseResult(parser.parse(Input.of(";123")))
            .isError
            .hasRemainingInput(";123")
    }

    @Test
    fun `should fail if second parser fails`() {
        val parser = permutation3(alpha1(), digit1(), chr('c'))

        assertThatParseResult(parser.parse(Input.of("abc")))
            .isError
            .hasRemainingInput("abc")
    }

    @Test
    fun `should accept empty input`() {
        val parser = permutation3(alpha0(), digit0(), alpha0())

        assertThatParseResult(parser.parse(Input.of("")))
            .isOk(Triple("", "", ""))
            .hasRemainingInput("")
    }
}
