package io.yegair.jom.examples

import io.yegair.jom.Combinators.many0
import io.yegair.jom.Combinators.map
import io.yegair.jom.Combinators.pair
import io.yegair.jom.Parsers.satisfy1
import io.yegair.jom.Utf8CodePoints.isLowerCase
import io.yegair.jom.Utf8CodePoints.isUpperCase
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

private val camelCaseParser =
    map(
        pair(
            // the first segment may contain lowercase letters only
            satisfy1 { cp, _ -> cp.isLowerCase() },
            // each following segment starts with an uppercase letter followed by lowercase letters
            many0(
                satisfy1 { cp, index ->
                    when (index) {
                        0 -> cp.isUpperCase()
                        else -> cp.isLowerCase()
                    }
                }
            )
        )
    ) { (first, rest) -> listOf(first) + rest }

class CamelCase {

    @Test
    fun test() {
        assertThatParseResult(camelCaseParser.parse(""))
            .isError
            .hasRemainingInput("")

        assertThatParseResult(camelCaseParser.parse("foo"))
            .isOk(listOf("foo"))
            .hasRemainingInput("")

        assertThatParseResult(camelCaseParser.parse("fooBar!"))
            .isOk(listOf("foo", "Bar"))
            .hasRemainingInput("!")

        assertThatParseResult(camelCaseParser.parse("fooBarBaz"))
            .isOk(listOf("foo", "Bar", "Baz"))
            .hasRemainingInput("")
    }
}
