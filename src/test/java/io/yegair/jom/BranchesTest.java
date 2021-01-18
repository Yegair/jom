package io.yegair.jom;

import kotlin.Pair;
import org.junit.jupiter.api.Test;

import static io.yegair.jom.test.ParseResultAssert.assertThatParseResult;

class BranchesTest
{
    @Test
    void alt()
    {
        final Parser<String> parser = Branches.alt(
            TextParsers.alpha1(),
            TextParsers.digit1()
        );

        // the first parser, alpha1, recognizes the input
        assertThatParseResult(parser.parse(Input.of("abc")))
            .isOk("abc")
            .hasRemainingInput("");

        // the first parser returns an error, so alt tries the second one
        assertThatParseResult(parser.parse(Input.of("123456")))
            .isOk("123456")
            .hasRemainingInput("");

        // both parsers failed, and with the default error type, alt will return the last error
        assertThatParseResult(parser.parse(Input.of(" ")))
            .isError(ParseError.Digit)
            .hasRemainingInput(" ");

        assertThatParseResult(parser.parse(Input.of("")))
            .isError(ParseError.Digit)
            .hasRemainingInput("");
    }

    @Test
    void permutation2()
    {
        final Parser<Pair<String, String>> parser = Branches.permutation2(
            TextParsers.alpha1(),
            TextParsers.digit1()
        );

        assertThatParseResult(parser.parse(Input.of("abc123")))
            .isOk(new Pair<>("abc", "123"))
            .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("123abc")))
            .isOk(new Pair<>("abc", "123"))
            .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("abc;")))
            .isError(ParseError.Digit)
            .hasRemainingInput("abc;");

        assertThatParseResult(parser.parse(Input.of("")))
            .isError(ParseError.Digit)
            .hasRemainingInput("");
    }
}