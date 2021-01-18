package io.yegair.jom

import java.util.Optional

/**
 * The result of an invocation to [Parser.parse].
 *
 * @param O The output/result type of the parser.
 */
class ParseResult<O> private constructor(

    /**
     * Indicates whether the parser was able to recognize the input.
     */
    val ok: Boolean,

    /**
     * The input that remains to be processed by the next parser.
     */
    @get:JvmName("remaining")
    val remaining: Input,

    /**
     * The result that was parsed by the parser or `null` if the parser did not produce a result.
     */
    @get:JvmSynthetic
    val output: O?,

    /**
     * The error that was returned by the parser if it was not able to recognize the input.
     */
    @get:JvmSynthetic
    val error: ParseError?
) {

    init {
        if (!ok && output != null) {
            throw IllegalArgumentException("a parse result which is not OK may not contain any output")
        }

        if (ok && error != null) {
            throw IllegalArgumentException("a parse result which is OK may not contain any error")
        }
    }

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

    fun <R> mapOutput(mapping: (Input, O) -> ParseResult<R>): ParseResult<R> {
        return when {
            ok -> when {
                output != null -> mapping(remaining, output)
                else -> ok(remaining, null)
            }
            else -> error(remaining, error!!)
        }
    }

    fun mapError(mapping: (Input, ParseError) -> ParseResult<O>): ParseResult<O> {
        return when {
            ok -> this
            else -> when {
                error != null -> mapping(remaining, error)
                // due to how ParseResult is created this can only happen due to internal bugs
                else -> throw IllegalStateException("can not apply error mapping, no error is present: this is a jom library bug!")
            }
        }
    }

    internal fun <R> map(mapping: (Boolean, Input, O?, ParseError?) -> ParseResult<R>): ParseResult<R> {
        return mapping(ok, remaining, output, error)
    }

    fun <R> castError(mapping: (Input, ParseError) -> ParseResult<R>): ParseResult<R> {
        return when {
            ok -> throw IllegalStateException("can not cast a parse result which is OK")
            else -> when {
                error != null -> mapping(remaining, error)
                // due to how ParseResult is created this can only happen due to internal bugs
                else -> throw IllegalStateException("can not apply error cast, no error is present: this is a jom library bug!")
            }
        }
    }

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
            output: O? = null
        ): ParseResult<O> {
            return ParseResult(
                ok = true,
                remaining = remaining,
                output = output,
                error = null
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
            return ParseResult(
                ok = false,
                remaining = remaining,
                output = null,
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
