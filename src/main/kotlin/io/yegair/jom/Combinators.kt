package io.yegair.jom

import java.nio.charset.Charset
import java.util.LinkedList

/**
 * General purpose combinators.
 */
object Combinators {

    /**
     * Succeeds if all the input has been consumed by its child parser.
     */
    @JvmStatic
    fun <O> allConsuming(parser: Parser<O>): Parser<O> {
        return Parser { input ->
            val res = parser.parse(input.peek())

            when {
                res.isError() -> res
                res.ok && res.remaining.exhausted() -> res
                else -> ParseResult.error(input, ParseError.Eof)
            }
        }
    }

    /**
     * Calls the parser if the condition is met.
     */
    @JvmStatic
    fun <O> cond(condition: Boolean, parser: Parser<O>): Parser<O?> {
        @Suppress("UNCHECKED_CAST")
        return when (condition) {
            true -> parser as Parser<O?>
            else -> Parser { input -> ParseResult.ok(input, null) }
        }
    }

    /**
     * If the child parser was successful, return the consumed input with the output as a pair.
     * Functions similarly to recognize except it returns the parser output as well.
     *
     * This can be useful especially in cases where the output is not the same type as the input, or the input is a
     * user defined type.
     *
     * Returned pair is of the format (produced output, consumed input).
     */
    @JvmStatic
    fun <O> consumed(parser: Parser<O>): Parser<Pair<O, ByteArray>> {
        return Parser { input ->
            val (tee, replica) = input.peek().tee()

            parser
                .parse(tee)
                .map(
                    ok = { remaining, output ->
                        ParseResult.ok(
                            remaining,
                            Pair(output, replica.readByteArray())
                        )
                    },
                    error = { _, err -> ParseResult.error(input, err) }
                )
        }
    }

    /**
     * If the child parser was successful, return the consumed input with the output as a pair.
     * Functions similarly to recognize except it returns the parser output as well.
     *
     * This can be useful especially in cases where the output is not the same type as the input, or the input is a
     * user defined type.
     *
     * Returned pair is of the format (produced output, consumed input).
     */
    @JvmStatic
    fun <O> consumed(parser: Parser<O>, charset: Charset): Parser<Pair<O, String>> {
        return map(consumed(parser)) { (output, consumedInput) ->
            Pair(output, consumedInput.toString(charset))
        }
    }

    /**
     * Runs the embedded parser a specified number of times. Returns the results in a [List].
     */
    @JvmStatic
    fun <O> count(parser: Parser<O>, times: Int): Parser<List<O>> {
        return Parser { input ->
            var nextInput = input.peek()
            val output: MutableList<O> = LinkedList()

            for (count in 1..times) {
                val res = parser.parse(nextInput)

                if (!res.ok) {
                    return@Parser res.map(
                        ok = { _, _ -> throw IllegalStateException("unexpected OK result") },
                        error = { _, error -> ParseResult.error(input, error) }
                    )
                }

                nextInput = res.remaining
                res.output?.run(output::add)
            }

            ParseResult.ok(nextInput, output)
        }
    }

    /**
     * Repeats the embedded parser until it fails and returns the results in a [List].
     */
    @JvmStatic
    fun <O> many0(parser: Parser<O>): Parser<List<O>> {
        return Parser { input ->
            var nextInput = input.peek()
            var result: ParseResult<O>
            val output: MutableList<O> = LinkedList()

            while (parser.parse(nextInput).also { result = it }.ok) {
                nextInput = result.remaining
                val nextOutput = result.output

                if (nextOutput != null) {
                    output.add(nextOutput)
                }
            }

            ParseResult.ok(result.remaining, output)
        }
    }

    /**
     * Runs the embedded parser until it fails and returns the results in a Vec.
     * Fails if the embedded parser does not produce at least one result.
     *
     * Note: If the parser passed to many1 accepts empty inputs (like alpha0 or digit0), many1 will return an error,
     * to prevent going into an infinite loop.
     */
    @JvmStatic
    fun <O> many1(parser: Parser<O>): Parser<List<O>> {
        return Parser { input ->
            val output: MutableList<O> = LinkedList()

            var nextInput = input.peek()
            var result: ParseResult<O> = parser.parse(nextInput)

            if (!result.ok) {
                return@Parser result.map(
                    ok = { _, _ -> throw IllegalStateException("unexpected OK result") },
                    error = { _, error -> ParseResult.error(input, error) }
                )
            }

            nextInput = result.remaining
            result.output?.run(output::add)

            while (parser.parse(nextInput).also { result = it }.ok) {
                nextInput = result.remaining
                val nextOutput = result.output

                if (nextOutput != null) {
                    output.add(nextOutput)
                }
            }

            ParseResult.ok(result.remaining, output)
        }
    }

    /**
     * Maps a function on the result of a parser.
     */
    @JvmStatic
    fun <O, R> map(parser: Parser<O>, mapping: (O) -> R): Parser<R> {
        return Parser { input ->
            parser
                .parse(input)
                .map { remaining, output ->
                    ParseResult.ok(remaining, mapping(output))
                }
        }
    }

    /**
     * Succeeds if the child parser returns an error.
     */
    @JvmStatic
    fun not(parser: Parser<*>): Parser<Unit> {
        return Parser { input ->
            parser
                .parse(input.peek())
                .map(
                    ok = { _, _ -> ParseResult.error(input, ParseError.Not) },
                    error = { remaining, _ -> ParseResult.ok(remaining, Unit) }
                )
        }
    }

    /**
     * Optional parser: Will return OK without a result if not successful.
     */
    @JvmStatic
    fun <O> opt(parser: Parser<O>): Parser<O?> {
        return Parser { input ->
            parser
                .parse(input)
                .map(
                    ok = { remaining, output -> ParseResult.ok(remaining, output) },
                    error = { remaining, _ -> ParseResult.ok(remaining, null) }
                )
        }
    }

    /**
     * Tries to apply its parser without consuming the input.
     */
    @JvmStatic
    fun <O> peek(parser: Parser<O>): Parser<O> {
        return Parser { input ->
            parser
                .parse(input.peek())
                .map(
                    ok = { _, output -> ParseResult.ok(input, output) },
                    error = { _, error -> ParseResult.error(input, error) }
                )
        }
    }

    /**
     * If the child parser was successful, return the consumed input as produced value.
     */
    @JvmStatic
    fun recognize(parser: Parser<*>): Parser<ByteArray> {
        return Parser { input ->
            val (tee, replica) = input.peek().tee()

            parser
                .parse(tee)
                .map(
                    ok = { remaining, _ -> ParseResult.ok(remaining, replica.readByteArray()) },
                    error = { _, err -> ParseResult.error(input, err) }
                )
        }
    }

    /**
     * If the child parser was successful, return the consumed input encoded with the given charset.
     */
    @JvmStatic
    fun recognize(parser: Parser<*>, charset: Charset): Parser<String> {
        return map(recognize(parser)) { output -> output.toString(charset) }
    }

    /**
     * A parser which always succeeds with given value without consuming any input.
     * It can be used for example as the last alternative in alt to specify the default case.
     */
    @JvmStatic
    fun <O> success(output: O): Parser<O> {
        return Parser { input -> ParseResult.ok(input, output) }
    }

    /**
     * Returns the provided value if the child parser succeeds.
     */
    @JvmStatic
    fun <O> value(output: O, parser: Parser<*>): Parser<O> {
        return Parser { input ->
            parser
                .parse(input)
                .map { remaining, _ -> ParseResult.ok(remaining, output) }
        }
    }

    /**
     * Returns the provided value if the child parser succeeds.
     */
    @JvmStatic
    fun <O> verify(parser: Parser<O>, predicate: (O?) -> Boolean): Parser<O> {
        return Parser { input ->
            parser
                .parse(input.peek())
                .map { remaining, output ->
                    when {
                        predicate(output) -> ParseResult.ok(remaining, output)
                        else -> ParseResult.error(input, ParseError.Verify)
                    }
                }
        }
    }
}
