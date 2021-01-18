package io.yegair.jom

internal class ChoiceParser<O>(private val parsers: List<Parser<O>>) : Parser<O> {
    override fun parse(input: Input): ParseResult<O> {
        for (parser in parsers) {
            val result = parser.parse(input)
            if (result.ok) {
                return result
            }
        }
        return ParseResult.error(input, ParseError.Choice)
    }
}
