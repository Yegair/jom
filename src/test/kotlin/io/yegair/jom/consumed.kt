@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.consumed
import io.yegair.jom.Combinators.separatedPair
import io.yegair.jom.Combinators.value
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.Parsers.chr
import io.yegair.jom.Parsers.tag
import io.yegair.jom.Parsers.tagNoCase
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets

class consumed {

    @Test
    fun `should capture parsed input`() {
        val parser = consumed(
            value(true, separatedPair(alpha1(), chr(','), tagNoCase("END"))),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("abcd,end1"))
            .isOk(Pair(true, "abcd,end"))
            .hasRemainingInput("1")
    }

    @Test
    fun `should capture parsed 2-byte utf8 code points`() {
        val parser = consumed(
            value(42, tag("Καλημέρα")),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("Καλημέρα κόσμε"))
            .isOk(Pair(42, "Καλημέρα"))
            .hasRemainingInput(" κόσμε")
    }

    @Test
    fun `should capture parsed 3-byte utf8 code points`() {
        val parser = consumed(
            value(1337, tag("こんにちは")),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("こんにちは 世界"))
            .isOk(Pair(1337, "こんにちは"))
            .hasRemainingInput(" 世界")
    }

    @Test
    fun `should capture parsed 4-byte utf8 code points`() {
        val parser = consumed(
            value(false, tag("👆👏")),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("👆👏 🌍"))
            .isOk(Pair(false, "👆👏"))
            .hasRemainingInput(" 🌍")
    }

    @Test
    fun `should fail if embedded parser fails`() {
        val parser = consumed(
            value(true, separatedPair(alpha1(), chr(','), tagNoCase("END"))),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("abcd,foo"))
            .isError(ParseError.Tag)
            .hasRemainingInput("abcd,foo")
    }

    @Test
    fun `should accept empty input`() {
        val parser = consumed(alpha0(), StandardCharsets.UTF_8)

        assertThatParseResult(parser.parse(""))
            .isOk(Pair("", ""))
            .hasRemainingInput("")
    }
}
