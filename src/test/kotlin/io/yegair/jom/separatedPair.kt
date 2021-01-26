@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.opt
import io.yegair.jom.Combinators.separatedPair
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.Parsers.codePoint
import io.yegair.jom.Parsers.digit0
import io.yegair.jom.Parsers.digit1
import io.yegair.jom.Parsers.lineEnding
import io.yegair.jom.Parsers.newline
import io.yegair.jom.Parsers.tag
import io.yegair.jom.Utf8CodePoints.toUtf8CodePoint
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class separatedPair {

    @Test
    fun `should parse matching input`() {
        val parser = separatedPair(codePoint('λ'), digit1(), tag("fu"))

        assertThatParseResult(parser.parse("λ1337fu;"))
            .isOk(Pair('λ'.toUtf8CodePoint(), "fu"))
            .hasRemainingInput(";")
    }

    @Test
    fun `should fail if first parser fails`() {
        val parser = separatedPair(digit1(), newline(), alpha1())

        assertThatParseResult(parser.parse("Foobar"))
            .isError(ParseError.Digit)
            .hasRemainingInput("Foobar")
    }

    @Test
    fun `should fail if second parser fails`() {
        val parser = separatedPair(digit1(), lineEnding(), alpha1())

        assertThatParseResult(parser.parse("42Foo"))
            .isError(ParseError.CrLf)
            .hasRemainingInput("42Foo")
    }

    @Test
    fun `should fail if third parser fails`() {
        val parser = separatedPair(digit1(), newline(), alpha1())

        assertThatParseResult(parser.parse("42\n13"))
            .isError(ParseError.Alpha)
            .hasRemainingInput("42\n13")
    }

    @Test
    fun `should parse empty input`() {
        val parser = separatedPair(alpha0(), digit0(), opt(codePoint('!')))

        assertThatParseResult(parser.parse(""))
            .isOk(Pair("", null))
            .hasRemainingInput("")
    }
}
