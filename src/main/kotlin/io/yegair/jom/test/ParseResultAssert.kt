package io.yegair.jom.test

import io.yegair.jom.ParseError
import io.yegair.jom.ParseResult
import java.util.Arrays
import java.util.Comparator

class ParseResultAssert<O> private constructor(
    private val actual: ParseResult<O>?,
    private val outputEquals: (O?, O?) -> Boolean = { a, b -> a == b }
) {

    val isNotNull: ParseResultAssert<O>
        get() {
            if (actual == null) {
                throw AssertionError("expected non null parse result")
            }
            return this
        }

    val isOk: ParseResultAssert<O>
        get() {
            isNotNull
            if (!actual!!.ok) {
                throw AssertionError("expected parse result to be OK, but was " + describe(actual))
            }
            return this
        }

    private fun describe(actual: ParseResult<O>?): String {

        val remainingInput = actual
            ?.remaining
            ?.readUtf8()
            ?: ""

        if (actual?.ok == true) {
            val output = actual
                .output()
                .orElse(null)
            return "OK[$output, \"$remainingInput\"]"
        }

        val error = actual
            ?.error()
            ?.orElse(null)

        return "Error[$error, \"$remainingInput\"]"
    }

    fun isOk(expectedOutput: O): ParseResultAssert<O> {
        return isOk.hasOutput(expectedOutput)
    }

    val isError: ParseResultAssert<O>
        get() {
            isNotNull
            if (!actual!!.isError()) {
                throw AssertionError("expected parse result to be an error, but was " + describe(actual))
            }
            return this
        }

    fun isError(expectedError: ParseError): ParseResultAssert<O> {
        return isError.hasError(expectedError)
    }

    fun hasOutput(expected: O): ParseResultAssert<O> {
        isNotNull

        val actualOutput = actual?.output

        if (!outputEquals(actualOutput, expected)) {
            throw AssertionError("output does not match expected: <$expected> but was: <$actualOutput>")
        }

        return this
    }

    fun hasError(expected: ParseError): ParseResultAssert<O> {
        isNotNull

        val actualError = actual
            ?.error
            ?: throw AssertionError("expected parse error to be equal to $expected but no parse error was present")

        if (actualError !== expected) {
            throw AssertionError("expected parse error to be $expected, but was $actualError")
        }

        return this
    }

    fun usingOutputComparator(comparator: Comparator<O?>): ParseResultAssert<O> {
        return ParseResultAssert(actual) { l, r -> comparator.compare(l, r) == 0 }
    }

    /**
     * Consumes the remaining input of the [ParseResult] under test and compares it to the given expected string.
     *
     * NOTE: This operation fully consumes the [ParseResult.remaining] input.
     */
    fun hasRemainingInput(expected: String): ParseResultAssert<O> {
        isNotNull

        val remainingInput = actual
            ?.remaining
            ?.readUtf8()
            ?: ""

        if (remainingInput != expected) {
            throw AssertionError(
                """
                remaining input does not match 
                expected: <$expected> but was: <$remainingInput>
                """.trimIndent()
            )
        }

        return this
    }

    /**
     * Consumes the remaining input of the [ParseResult] under test and compares it to the given expected string.
     *
     * NOTE: This operation fully consumes the [ParseResult.remaining] input.
     */
    fun hasRemainingInput(expected: ByteArray?): ParseResultAssert<O> {
        isNotNull

        val remainingInput = actual
            ?.remaining
            ?.readByteArray()

        if (!Arrays.equals(remainingInput, expected)) {
            throw AssertionError(
                """
                remaining input does not match
                expected: <${Arrays.toString(expected)}> but was: <${Arrays.toString(remainingInput)}>
                """.trimIndent()
            )
        }

        return this
    }

    companion object {

        /**
         * Start asserting for the given parse result.
         */
        @JvmStatic
        fun <O> assertThatParseResult(actual: ParseResult<O>?): ParseResultAssert<O> {
            return ParseResultAssert(actual)
        }
    }
}
