@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Combinators.success
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class success {

    @Test
    fun `should not consume input`() {
        val parser = success(10)

        assertThatParseResult(parser.parse("xyz"))
            .isOk(10)
            .hasRemainingInput("xyz")
    }

    @Test
    fun `should accept empty input`() {
        val parser = success(42)

        assertThatParseResult(parser.parse(""))
            .isOk(42)
            .hasRemainingInput("")
    }
}
