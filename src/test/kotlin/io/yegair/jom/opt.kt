@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.opt
import io.yegair.jom.Parsers.alpha1
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class opt {

    @Test
    fun `should succeed when embedded parser succeeds`() {
        val parser = opt(alpha1())

        assertThatParseResult(parser.parse("abcd;"))
            .isOk("abcd")
            .hasRemainingInput(";")
    }

    @Test
    fun `should succeed with no result when embedded parser fails`() {
        val parser = opt(alpha1())

        assertThatParseResult(parser.parse("123;"))
            .isOk(null)
            .hasRemainingInput("123;")
    }
}
