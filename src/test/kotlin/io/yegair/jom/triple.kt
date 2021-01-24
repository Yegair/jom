@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.opt
import io.yegair.jom.Combinators.triple
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.Parsers.chr
import io.yegair.jom.Parsers.digit0
import io.yegair.jom.Parsers.digit1
import io.yegair.jom.Parsers.lineEnding
import io.yegair.jom.Parsers.newline
import io.yegair.jom.Parsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class triple {

    @Test
    fun `should parse matching input`() {
        val parser = triple(chr('λ'), digit1(), tag("fu"))

        assertThatParseResult(parser.parse("λ1337fu;"))
            .isOk(Triple('λ', "1337", "fu"))
            .hasRemainingInput(";")
    }

    @Test
    fun `should fail if first parser fails`() {
        val parser = triple(digit1(), newline(), alpha1())

        assertThatParseResult(parser.parse("Foobar"))
            .isError(ParseError.Digit)
            .hasRemainingInput("Foobar")
    }

    @Test
    fun `should fail if second parser fails`() {
        val parser = triple(digit1(), lineEnding(), alpha1())

        assertThatParseResult(parser.parse("42Foo"))
            .isError(ParseError.CrLf)
            .hasRemainingInput("42Foo")
    }

    @Test
    fun `should fail if third parser fails`() {
        val parser = triple(digit1(), newline(), alpha1())

        assertThatParseResult(parser.parse("42\n13"))
            .isError(ParseError.Alpha)
            .hasRemainingInput("42\n13")
    }

    @Test
    fun `should parse empty input`() {
        val parser = triple(alpha0(), digit0(), opt(chr('!')))

        assertThatParseResult(parser.parse(""))
            .isOk(Triple("", "", null))
            .hasRemainingInput("")
    }
}
