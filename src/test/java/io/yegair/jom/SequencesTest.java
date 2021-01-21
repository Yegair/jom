package io.yegair.jom;

import static io.yegair.jom.test.ParseResultAssert.assertThatParseResult;

import kotlin.Pair;
import kotlin.Triple;
import org.junit.jupiter.api.Test;

class SequencesTest {
    @Test
    void delimited() {
        final Parser<String> parser =
                Sequences.delimited(
                        TextParsers.tag("abc"), TextParsers.tag("|"), TextParsers.tag("efg"));

        assertThatParseResult(parser.parse(Input.of("abc|efg"))).isOk("|").hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("abc|efghij")))
                .isOk("|")
                .hasRemainingInput("hij");

        assertThatParseResult(parser.parse(Input.of("")))
                .isError(ParseError.Tag)
                .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("123")))
                .isError(ParseError.Tag)
                .hasRemainingInput("123");
    }

    @Test
    void pair() {
        final Parser<Pair<String, String>> parser =
                Sequences.pair(TextParsers.tag("abc"), TextParsers.tag("efg"));

        assertThatParseResult(parser.parse(Input.of("abcefg")))
                .isOk(new Pair<>("abc", "efg"))
                .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("abcefghij")))
                .isOk(new Pair<>("abc", "efg"))
                .hasRemainingInput("hij");

        assertThatParseResult(parser.parse(Input.of("")))
                .isError(ParseError.Tag)
                .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("123")))
                .isError(ParseError.Tag)
                .hasRemainingInput("123");
    }

    @Test
    void preceded() {
        final Parser<String> parser =
                Sequences.preceded(TextParsers.tag("abc"), TextParsers.tag("efg"));

        assertThatParseResult(parser.parse(Input.of("abcefg"))).isOk("efg").hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("abcefghij")))
                .isOk("efg")
                .hasRemainingInput("hij");

        assertThatParseResult(parser.parse(Input.of("")))
                .isError(ParseError.Tag)
                .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("123")))
                .isError(ParseError.Tag)
                .hasRemainingInput("123");
    }

    @Test
    void separatedPair() {
        final Parser<Pair<String, String>> parser =
                Sequences.separatedPair(
                        TextParsers.tag("abc"), TextParsers.tag("|"), TextParsers.tag("efg"));

        assertThatParseResult(parser.parse(Input.of("abc|efg")))
                .isOk(new Pair<>("abc", "efg"))
                .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("abc|efghij")))
                .isOk(new Pair<>("abc", "efg"))
                .hasRemainingInput("hij");

        assertThatParseResult(parser.parse(Input.of("abc|def")))
                .isError(ParseError.Tag)
                .hasRemainingInput("abc|def");

        assertThatParseResult(parser.parse(Input.of("")))
                .isError(ParseError.Tag)
                .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("123")))
                .isError(ParseError.Tag)
                .hasRemainingInput("123");
    }

    @Test
    void terminated() {
        final Parser<String> parser =
                Sequences.terminated(TextParsers.tag("abc"), TextParsers.tag("efg"));

        assertThatParseResult(parser.parse(Input.of("abcefg"))).isOk("abc").hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("abcefghij")))
                .isOk("abc")
                .hasRemainingInput("hij");

        assertThatParseResult(parser.parse(Input.of("")))
                .isError(ParseError.Tag)
                .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("123")))
                .isError(ParseError.Tag)
                .hasRemainingInput("123");
    }

    @Test
    void triple() {
        final Parser<Triple<String, String, String>> parser =
                Sequences.triple(TextParsers.alpha1(), TextParsers.digit1(), TextParsers.alpha1());

        assertThatParseResult(parser.parse(Input.of("abc123def")))
                .isOk(new Triple<>("abc", "123", "def"))
                .hasRemainingInput("");

        assertThatParseResult(parser.parse(Input.of("123def")))
                .isError(ParseError.Alpha)
                .hasRemainingInput("123def");
    }
}
