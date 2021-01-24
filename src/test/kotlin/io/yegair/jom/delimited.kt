@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Sequences.delimited
import io.yegair.jom.TextParsers.alpha0
import io.yegair.jom.TextParsers.digit0
import io.yegair.jom.TextParsers.digit1
import io.yegair.jom.TextParsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class delimited {

    @Test
    fun `should parse matching input`() {
        val parser = delimited(tag("{{"), digit1(), tag("}}"))

        assertThatParseResult(parser.parse("{{42}}"))
            .isOk("42")
            .hasRemainingInput("")
    }

    @Test
    fun `should not parse non matching value`() {
        val parser = delimited(tag("{{"), digit1(), tag("}}"))

        assertThatParseResult(parser.parse("{{foobar}}"))
            .isError(ParseError.Digit)
            .hasRemainingInput("{{foobar}}")
    }

    @Test
    fun `should not parse non matching left delimiter value`() {
        val parser = delimited(tag("{{"), digit1(), tag("}}"))

        assertThatParseResult(parser.parse("{1337}}"))
            .isError(ParseError.Tag)
            .hasRemainingInput("{1337}}")
    }

    @Test
    fun `should not parse non matching right delimiter value`() {
        val parser = delimited(tag("{{"), digit1(), tag("}}"))

        assertThatParseResult(parser.parse("{{42}"))
            .isError(ParseError.Tag)
            .hasRemainingInput("{{42}")
    }

    @Test
    fun `should parse empty delimiters`() {
        val parser = delimited(alpha0(), digit1(), alpha0())

        assertThatParseResult(parser.parse("123;"))
            .isOk("123")
            .hasRemainingInput(";")
    }

    @Test
    fun `should parse empty input`() {
        val parser = delimited(alpha0(), digit0(), alpha0())

        assertThatParseResult(parser.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }
}
