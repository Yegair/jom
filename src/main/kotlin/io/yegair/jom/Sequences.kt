package io.yegair.jom

import java.lang.IllegalStateException
import kotlin.Pair

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
        first: Parser<*>,
        separator: Parser<O>,
        second: Parser<*>
    ): Parser<O> {
        return Parser.peeking { input: Input ->
            val res1 = first.parse(input)
            if (res1.isError()) {
                return@peeking ParseResult.error(res1)
            }

            val resSep = separator.parse(res1.remaining)
            if (resSep.isError()) {
                return@peeking ParseResult.error(resSep)
            }

            val res2 = second.parse(resSep.remaining)
            if (res2.isError()) {
                return@peeking ParseResult.error(res2)
            }

            val output = resSep
                .output()
                .orElseThrow(::IllegalStateException)

            ParseResult.ok(
                res2.remaining,
                output
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
            val res1 = first.parse(input)
            if (res1.isError()) {
                return@peeking ParseResult.error(res1)
            }

            val res2 = second.parse(res1.remaining)
            if (res2.isError()) {
                return@peeking ParseResult.error(res2)
            }

            val output1 = res1
                .output()
                .orElseThrow(::IllegalStateException)

            val output2 = res2
                .output()
                .orElseThrow(::IllegalStateException)

            ParseResult.ok(
                res2.remaining,
                Pair(
                    output1,
                    output2
                )
            )
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
        return Parser.peeking { input: Input ->
            val res1 = first.parse(input)
            if (res1.isError()) {
                return@peeking ParseResult.error(res1)
            }

            val res2 = second.parse(res1.remaining)
            if (res2.isError()) {
                return@peeking ParseResult.error(res2)
            }

            val output = res2
                .output()
                .orElseThrow(::IllegalStateException)

            ParseResult.ok(
                res2.remaining,
                output
            )
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
        return Parser.peeking { input: Input ->
            val res1 = first.parse(input)
            if (res1.isError()) {
                return@peeking ParseResult.error(res1)
            }

            val resSep = separator.parse(res1.remaining)
            if (resSep.isError()) {
                return@peeking ParseResult.error(resSep)
            }

            val res2 = second.parse(resSep.remaining)
            if (res2.isError()) {
                return@peeking ParseResult.error(res2)
            }

            val output1 = res1
                .output()
                .orElseThrow(::IllegalStateException)

            val output2 = res2
                .output()
                .orElseThrow(::IllegalStateException)

            ParseResult.ok(
                res2.remaining,
                Pair(
                    output1,
                    output2
                )
            )
        }
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
        return Parser.peeking { input: Input ->
            val res1 = first.parse(input)
            if (res1.isError()) {
                return@peeking ParseResult.error(res1)
            }

            val res2 = second.parse(res1.remaining)
            if (res2.isError()) {
                return@peeking ParseResult.error(res2)
            }

            val output = res1
                .output()
                .orElseThrow(::IllegalStateException)

            ParseResult.ok(
                res2.remaining,
                output
            )
        }
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
        return Parser.peeking { input: Input ->
            val res1 = first.parse(input)
            if (res1.isError()) {
                return@peeking ParseResult.error(res1)
            }

            val res2 = second.parse(res1.remaining)
            if (res2.isError()) {
                return@peeking ParseResult.error(res2)
            }

            val res3 = third.parse(res2.remaining)
            if (res3.isError()) {
                return@peeking ParseResult.error(res3)
            }

            val output1 = res1
                .output()
                .orElseThrow(::IllegalStateException)

            val output2 = res2
                .output()
                .orElseThrow(::IllegalStateException)

            val output3 = res3
                .output()
                .orElseThrow(::IllegalStateException)

            ParseResult.ok(
                res3.remaining,
                Triple(
                    output1,
                    output2,
                    output3
                )
            )
        }
    }
}
