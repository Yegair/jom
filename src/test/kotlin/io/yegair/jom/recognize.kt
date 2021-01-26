@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.recognize
import io.yegair.jom.Combinators.separatedPair
import io.yegair.jom.Parsers.alpha0
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.Parsers.codePoint
import io.yegair.jom.Parsers.tag
import io.yegair.jom.Parsers.tagNoCase
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets

class recognize {

    @Test
    fun `should recognize parsed input`() {
        val parser = recognize(
            separatedPair(alpha1(), codePoint(','), tagNoCase("END")),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("abcd,end1"))
            .isOk("abcd,end")
            .hasRemainingInput("1")
    }

    @Test
    fun `should recognize parsed 2-byte utf8 code points`() {
        val parser = recognize(
            tag("Καλημέρα"),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("Καλημέρα κόσμε"))
            .isOk("Καλημέρα")
            .hasRemainingInput(" κόσμε")
    }

    @Test
    fun `should recognize parsed 3-byte utf8 code points`() {
        val parser = recognize(
            tag("こんにちは"),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("こんにちは 世界"))
            .isOk("こんにちは")
            .hasRemainingInput(" 世界")
    }

    @Test
    fun `should recognize parsed 4-byte utf8 code points`() {
        val parser = recognize(
            tag("👆👏"),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("👆👏 🌍"))
            .isOk("👆👏")
            .hasRemainingInput(" 🌍")
    }

    @Test
    fun `should fail if embedded parser fails`() {
        val parser = recognize(
            separatedPair(alpha1(), codePoint(','), tagNoCase("END")),
            StandardCharsets.UTF_8
        )

        assertThatParseResult(parser.parse("abcd,foo"))
            .isError(ParseError.Tag)
            .hasRemainingInput("abcd,foo")
    }

    @Test
    fun `should accept empty input`() {
        val parser = recognize(alpha0(), StandardCharsets.UTF_8)

        assertThatParseResult(parser.parse(""))
            .isOk("")
            .hasRemainingInput("")
    }
}
