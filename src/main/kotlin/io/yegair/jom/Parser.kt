package io.yegair.jom

fun interface Parser<O> {

    /**
     * Parses the given input and produces a parse result.
     */
    fun parse(input: Input): ParseResult<O>

    /**
     * Parses the given string and produces a parse result.
     */
    @JvmDefault
    fun parse(input: String): ParseResult<O> = parse(Input.of(input))

    companion object {

        /**
         * Creates a parser which uses the given parser to parse input.
         * If the parser returns a successful result, it is returned as is.
         * If the parser returns an erroneous result, it's remaining input is reverted to where the parser started.
         */
        @JvmStatic
        fun <O> peeking(parser: Parser<O>): Parser<O> = Parser { input ->
            parser
                .parse(input.peek())
                .mapError { _, err -> ParseResult.error<O>(input, err) }
        }
    }
}
