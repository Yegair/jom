package io.yegair.jom

import java.util.Optional

/**
 * The result of an invocation to [Parser.parse].
 *
 * @param O The output/result type of the parser.
 */
sealed class ParseResult<O>(

    /**
     * Indicates whether the parser was able to recognize the input.
     */
    val ok: Boolean,

    /**
     * The input that remains to be processed by the next parser.
     */
    @get:JvmName("remaining")
    val remaining: Input
) {
    /**
     * The result that was generated by the parser.
     */
    abstract val output: O

    /**
     * The error that was returned by the parser if it was not able to recognize the input.
     */
    abstract val error: ParseError?

    fun isError(): Boolean = !ok

    /**
     * The result that was parsed by the parser or nothing if the parser did not recognize the input.
     */
    fun output(): Optional<O> = Optional.ofNullable(output)

    /**
     * The error that was returned by the parser if it was not able to recognize the input.
     */
    fun error(): Optional<ParseError> = Optional.ofNullable(error)

    override fun toString(): String {
        return when {
            ok -> "Ok[$output]"
            else -> "Error[$error]"
        }
    }

    /**
     * Applies the given mapping if the result is successful.
     */
    fun <R> map(ok: (Input, O) -> ParseResult<R>): ParseResult<R> {
        return map(
            ok = ok,
            error = { remaining, err -> error(remaining, err) }
        )
    }

    /**
     * Applies the given `ok` mapping the the result is successful or the `error` mapping otherwise.
     */
    abstract fun <R> map(
        ok: (Input, O) -> ParseResult<R>,
        error: (Input, ParseError) -> ParseResult<R>
    ): ParseResult<R>

    /**
     * Applies the given mapping if the result is not successful.
     */
    abstract fun mapError(mapping: (Input, ParseError) -> ParseResult<O>): ParseResult<O>

    companion object {

        /**
         * Creates a new parse result indicating that the parser did recognize the input.
         *
         * @param remaining The remaining input (that should be processed by the next parser).
         * @param output    The result of the parser.
         */
        @JvmStatic
        fun <O> ok(
            remaining: Input,
            output: O
        ): ParseResult<O> {
            return ParseSuccess(
                remaining = remaining,
                output = output
            )
        }

        /**
         * Creates a new parse result indicating that the parser did not recognize the input.
         *
         * @param remaining The remaining input (that should be processed by the next parser).
         * @param error     Indicates why the input could not be recognized.
         */
        @JvmStatic
        fun <O> error(
            remaining: Input,
            error: ParseError
        ): ParseResult<O> {
            return ParseFailure(
                remaining = remaining,
                error = error
            )
        }

        @JvmStatic
        fun <O> error(error: ParseResult<*>): ParseResult<O> {
            @Suppress("UNCHECKED_CAST")
            return error as ParseResult<O>
        }
    }
}

private class ParseSuccess<O>(
    remaining: Input,
    override val output: O
) : ParseResult<O>(ok = true, remaining = remaining) {

    override val error: ParseError? = null

    override fun <R> map(
        ok: (Input, O) -> ParseResult<R>,
        error: (Input, ParseError) -> ParseResult<R>
    ): ParseResult<R> {
        return ok(remaining, output)
    }

    override fun mapError(mapping: (Input, ParseError) -> ParseResult<O>): ParseResult<O> {
        return this
    }
}

private class ParseFailure<O>(
    remaining: Input,
    override val error: ParseError
) : ParseResult<O>(ok = false, remaining = remaining) {

    override val output: O
        get() = throw IllegalStateException("can not obtain output from an erroneous parse result")

    override fun <R> map(
        ok: (Input, O) -> ParseResult<R>,
        error: (Input, ParseError) -> ParseResult<R>
    ): ParseResult<R> {
        return error(remaining, this.error)
    }

    override fun mapError(mapping: (Input, ParseError) -> ParseResult<O>): ParseResult<O> {
        return mapping(remaining, error)
    }
}
