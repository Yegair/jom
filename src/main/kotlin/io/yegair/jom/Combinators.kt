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
            parser
                .parse(input.peek())
                .map { remaining, output ->
                    when {
                        remaining.exhausted() -> ParseResult.ok(remaining, output)
                        else -> ParseResult.error(input, ParseError.Eof)
                    }
                }
        }
    }

    /**
     * Tests a list of parsers one by one until one succeeds.
     * If no parser succeeds the error from the last parser is returned.
     */
    @JvmStatic
    @SafeVarargs
    fun <O> alt(vararg parsers: Parser<O>): Parser<O> {

        if (parsers.isEmpty()) {
            throw IllegalArgumentException("alt requires at least one embedded parser")
        }

        return Parser { input ->

            var res: ParseResult<O>? = null

            for (parser in parsers) {
                res = parser.parse(input.peek())
                if (res.ok) {
                    return@Parser res
                }
            }

            if (res == null) {
                // there should be no code path to get here,
                // because the parsers can not be empty if we get here
                throw IllegalStateException("alt requires at least one embedded parser")
            }

            // this must be an error, so we can just return the result
            res
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
            parser
                .parse(input.peek())
                .map { remaining, output ->
                    val consumedLen = remaining.bytesProcessed - input.bytesProcessed
                    val consumed = input.peek().readByteString(consumedLen)

                    ParseResult.ok(
                        remaining,
                        Pair(output, consumed.toByteArray())
                    )
                }
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
     * Matches a result from the first parser,
     * then gets a result from the separator parser,
     * then matches another result from the second parser.
     */
    @JvmStatic
    fun <O> delimited(
        left: Parser<*>,
        middle: Parser<O>,
        right: Parser<*>
    ): Parser<O> {
        return map(triple(left, middle, right), Triple<*, O, *>::second)
    }

    /**
     * Applies a parser until it fails and accumulates the results using a given function and initial value.
     */
    fun <O, I, R> foldMany0(
        parser: Parser<O>,
        initial: () -> I,
        operation: (I, O) -> I,
        finalizer: (I) -> R
    ): Parser<R> {
        return Parser { originalInput ->
            var input = originalInput.peek()
            var intermediateResult = initial()

            while (true) {
                val res = parser.parse(input.peek())

                if (!res.ok) {
                    return@Parser ParseResult.ok(input, finalizer(intermediateResult))
                }

                if (input.bytesProcessed == res.remaining.bytesProcessed) {
                    // detected non terminating parser
                    return@Parser ParseResult.error(originalInput, ParseError.Many)
                }

                input = res.remaining
                intermediateResult = operation(intermediateResult, res.output)
            }

            @Suppress("UNREACHABLE_CODE")
            unreachable()
        }
    }

    /**
     * Applies a parser until it fails and accumulates the results using a given function and initial value.
     */
    fun <O, R> foldMany0(
        parser: Parser<O>,
        initial: () -> R,
        operation: (R, O) -> R
    ): Parser<R> {
        return foldMany0(parser, initial, operation, { it })
    }

    /**
     * Applies a parser until it fails and accumulates the results using a given function and initial value.
     * Fails if the embedded parser does not succeed at least once.
     *
     * Note: If the parser passed to many1 accepts empty inputs (like alpha0 or digit0), many1 will return an error,
     * to prevent going into an infinite loop.
     */
    @JvmStatic
    fun <O, I, R> foldMany1(
        parser: Parser<O>,
        initial: () -> I,
        operation: (I, O) -> I,
        finalizer: (I) -> R
    ): Parser<R> {
        return Parser { input ->
            var output = initial()
            var result: ParseResult<O> = parser.parse(input.peek())

            if (!result.ok) {
                return@Parser result.map(
                    ok = { _, _ -> throw IllegalStateException("unexpected OK result") },
                    error = { _, error -> ParseResult.error(input, error) }
                )
            }

            if (input.bytesProcessed == result.remaining.bytesProcessed) {
                // parser accepts empty input, many1 would run forever with this parser
                return@Parser ParseResult.error(input, ParseError.Many)
            }

            output = operation(output, result.output)

            while (true) {
                val res = parser.parse(result.remaining.peek())

                if (!res.ok) {
                    return@Parser ParseResult.ok(result.remaining, finalizer(output))
                }

                if (result.remaining.bytesProcessed == res.remaining.bytesProcessed) {
                    // detected non terminating parser
                    return@Parser ParseResult.error(input, ParseError.Many)
                }

                result = res
                output = operation(output, result.output)
            }

            @Suppress("UNREACHABLE_CODE")
            unreachable()
        }
    }

    /**
     * Applies a parser until it fails and accumulates the results using a given function and initial value.
     * Fails if the embedded parser does not succeed at least once.
     *
     * Note: If the parser passed to many1 accepts empty inputs (like alpha0 or digit0), many1 will return an error,
     * to prevent going into an infinite loop.
     */
    @JvmStatic
    fun <O, R> foldMany1(
        parser: Parser<O>,
        initial: () -> R,
        operation: (R, O) -> R
    ): Parser<R> {
        return foldMany1(parser, initial, operation, { it })
    }

    private fun unreachable(): Nothing {
        throw IllegalStateException("unreachable code")
    }

    /**
     * Repeats the embedded parser until it fails and returns the results in a [List].
     */
    @JvmStatic
    fun <O> many0(parser: Parser<O>): Parser<List<O>> {
        return foldMany0(
            parser,
            ::mutableListOf,
            { list, item ->
                list.add(item)
                list
            },
            MutableList<O>::toList
        )
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
        return foldMany1(
            parser,
            ::mutableListOf,
            { list, item ->
                list.add(item)
                list
            },
            MutableList<O>::toList
        )
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
     * Maps a function on the result of a parser.
     * Succeeds if the function returns a non-null result.
     * Fails otherwise with [ParseError.MapOpt].
     */
    fun <O, R> mapOpt(parser: Parser<O>, mapping: (O) -> R?): Parser<R> {
        return Parser { input ->
            parser
                .parse(input.peek())
                .map { remaining, output ->
                    mapping(output)
                        ?.let { mapped -> ParseResult.ok(remaining, mapped) }
                        ?: ParseResult.error(input, ParseError.MapOpt)
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
     * Gets a result from the first parser,
     * then gets another result from the second parser.
     */
    @JvmStatic
    fun <O1, O2> pair(
        first: Parser<O1>,
        second: Parser<O2>
    ): Parser<Pair<O1, O2>> {
        return Parser.peeking { input: Input ->
            first
                .parse(input)
                .map { remaining1, output1 ->
                    second
                        .parse(remaining1)
                        .map { remaining2, output2 ->
                            ParseResult.ok(remaining2, Pair(output1, output2))
                        }
                }
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

    @JvmStatic
    fun <O1, O2> permutation2(
        parser1: Parser<O1>,
        parser2: Parser<O2>
    ): Parser<Pair<O1, O2>> {
        return alt(
            pair(parser1, parser2),
            map(pair(parser2, parser1)) { (o2, o1) -> Pair(o1, o2) }
        )
    }

    @JvmStatic
    fun <O1, O2, O3> permutation3(
        parser1: Parser<O1>,
        parser2: Parser<O2>,
        parser3: Parser<O3>
    ): Parser<Triple<O1, O2, O3>> {
        return alt(
            triple(parser1, parser2, parser3),
            map(triple(parser1, parser3, parser2)) { (o1, o3, o2) -> Triple(o1, o2, o3) },
            map(triple(parser2, parser1, parser3)) { (o2, o1, o3) -> Triple(o1, o2, o3) },
            map(triple(parser2, parser3, parser1)) { (o2, o3, o1) -> Triple(o1, o2, o3) },
            map(triple(parser3, parser1, parser2)) { (o3, o1, o2) -> Triple(o1, o2, o3) },
            map(triple(parser3, parser2, parser1)) { (o3, o2, o1) -> Triple(o1, o2, o3) }
        )
    }

    /**
     * Matches a result from the first parser and discards it,
     * then gets a result from the second parser.
     */
    @JvmStatic
    fun <O> preceded(
        first: Parser<*>,
        second: Parser<O>
    ): Parser<O> {
        return map(pair(first, second), Pair<*, O>::second)
    }

    /**
     * If the child parser was successful, return the consumed input as produced value.
     */
    @JvmStatic
    fun recognize(parser: Parser<*>): Parser<ByteArray> {
        return Parser { input ->
            parser
                .parse(input.peek())
                .map { remaining, _ ->
                    val consumedLen = remaining.bytesProcessed - input.bytesProcessed
                    val consumed = input.peek().readByteString(consumedLen)

                    ParseResult.ok(
                        remaining,
                        consumed.toByteArray()
                    )
                }
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
     * Alternates between two parsers to produce a list of elements.
     */
    fun <O> separatedList0(
        separator: Parser<*>,
        parser: Parser<O>
    ): Parser<List<O>> {
        return Parser { input ->
            val first = parser.parse(input.peek())

            if (!first.ok) {
                return@Parser ParseResult.ok(input, emptyList())
            }

            val rest = foldMany0<O, MutableList<O>>(
                preceded(separator, parser),
                ::mutableListOf,
                { list, item ->
                    list.add(item)
                    list
                }
            ).parse(first.remaining)

            rest.map { remaining, result ->
                ParseResult.ok(remaining, listOf(first.output) + result)
            }
        }
    }

    /**
     * Gets a result from the first parser,
     * then matches a result from the sep_parser and discards it,
     * then gets another result from the second parser.
     */
    @JvmStatic
    fun <O1, O2> separatedPair(
        first: Parser<O1>,
        separator: Parser<*>,
        second: Parser<O2>
    ): Parser<Pair<O1, O2>> {
        return map(triple(first, separator, second)) { (result1, _, result2) -> Pair(result1, result2) }
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
     * Gets a result from the first parser,
     * then matches a result from the second parser and discards it.
     */
    @JvmStatic
    fun <O> terminated(
        first: Parser<O>,
        second: Parser<*>
    ): Parser<O> {
        return map(pair(first, second), Pair<O, *>::first)
    }

    /**
     * Applies three parsers one by one and returns their results as a [Triple].
     */
    @JvmStatic
    fun <O1, O2, O3> triple(
        first: Parser<O1>,
        second: Parser<O2>,
        third: Parser<O3>
    ): Parser<Triple<O1, O2, O3>> {
        return map(pair(pair(first, second), third)) {
            Triple(it.first.first, it.first.second, it.second)
        }
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
    fun <O> verify(parser: Parser<O>, predicate: (O) -> Boolean): Parser<O> {
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
