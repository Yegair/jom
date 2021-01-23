package io.yegair.jom

import io.yegair.jom.Combinators.map

/**
 * Combinators applying parsers in sequence.
 */
object Sequences {

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
}
