@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.preceded
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.Parsers.chr
import io.yegair.jom.Parsers.digit0
import io.yegair.jom.Parsers.digit1
import io.yegair.jom.Parsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class preceded {

    @Test
    fun `should parse matching input`() {
        val parser = preceded(chr('λ'), tag("fu"))

        assertThatParseResult(parser.parse("λfu;"))
            .isOk("fu")
            .hasRemainingInput(";")
    }

    @Test
    fun `should fail if first parser fails`() {
        val parser = preceded(digit1(), alpha0())

        assertThatParseResult(parser.parse("Foobar"))
            .isError(ParseError.Digit)
            .hasRemainingInput("Foobar")
    }

    @Test
    fun `should fail if second parser fails`() {
        val parser = preceded(digit1(), alpha1())

        assertThatParseResult(parser.parse("42"))
            .isError(ParseError.Alpha)
            .hasRemainingInput("42")
    }

    @Test
    fun `should parse empty input`() {
        val parser = preceded(alpha0(), digit0())

        assertThatParseResult(parser.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }
}
