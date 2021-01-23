@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Input.Companion.of
import io.yegair.jom.Sequences.pair
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.alpha1
import io.yegair.jom.TextParsers.chr
import io.yegair.jom.TextParsers.digit0
import io.yegair.jom.TextParsers.digit1
import io.yegair.jom.TextParsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class pair {

    @Test
    fun `should parse matching input`() {
        val parser = pair(chr('λ'), tag("fu"))

        assertThatParseResult(parser.parse(of("λfu;")))
            .isOk(Pair('λ', "fu"))
            .hasRemainingInput(";")
    }

    @Test
    fun `should fail if first parser fails`() {
        val parser = pair(digit1(), alpha0())

        assertThatParseResult(parser.parse(of("Foobar")))
            .isError(ParseError.Digit)
            .hasRemainingInput("Foobar")
    }

    @Test
    fun `should fail if second parser fails`() {
        val parser = pair(digit1(), alpha1())

        assertThatParseResult(parser.parse(of("42")))
            .isError(ParseError.Alpha)
            .hasRemainingInput("42")
    }

    @Test
    fun `should parse empty input`() {
        val parser = pair(alpha0(), digit0())

        assertThatParseResult(parser.parse(of("")))
            .isOk(Pair("", ""))
            .hasRemainingInput("")
    }
}
