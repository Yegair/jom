package io.yegair.jom

import io.yegair.jom.Combinators.map
import io.yegair.jom.Sequences.pair
import io.yegair.jom.Sequences.triple

/**
 * Branching combinators.
 */
object Branches {

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
}
