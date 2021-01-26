@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.terminated
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.Parsers.codePoint
import io.yegair.jom.Parsers.digit0
import io.yegair.jom.Parsers.digit1
import io.yegair.jom.Parsers.tag
import io.yegair.jom.Utf8CodePoints.toUtf8CodePoint
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class terminated {

    @Test
    fun `should parse matching input`() {
        val parser = terminated(codePoint('λ'), tag("fu"))

        assertThatParseResult(parser.parse("λfu;"))
            .isOk('λ'.toUtf8CodePoint())
            .hasRemainingInput(";")
    }

    @Test
    fun `should fail if first parser fails`() {
        val parser = terminated(digit1(), alpha0())

        assertThatParseResult(parser.parse("Foobar"))
            .isError(ParseError.Digit)
            .hasRemainingInput("Foobar")
    }

    @Test
    fun `should fail if second parser fails`() {
        val parser = terminated(digit1(), alpha1())

        assertThatParseResult(parser.parse("42"))
            .isError(ParseError.Alpha)
            .hasRemainingInput("42")
    }

    @Test
    fun `should parse empty input`() {
        val parser = terminated(alpha0(), digit0())

        assertThatParseResult(parser.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }
}
