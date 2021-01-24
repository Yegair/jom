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

        assertThatParseResult(parser.parse("abc|efg")).isOk("|").hasRemainingInput("");
    }

    @Test
    void pair() {
        final Parser<Pair<String, String>> parser =
                Sequences.pair(TextParsers.tag("abc"), TextParsers.tag("efg"));

        assertThatParseResult(parser.parse("abcefg"))
                .isOk(new Pair<>("abc", "efg"))
                .hasRemainingInput("");
    }

    @Test
    void preceded() {
        final Parser<String> parser =
                Sequences.preceded(TextParsers.tag("abc"), TextParsers.tag("efg"));

        assertThatParseResult(parser.parse("abcefg")).isOk("efg").hasRemainingInput("");
    }

    @Test
    void separatedPair() {
        final Parser<Pair<String, String>> parser =
                Sequences.separatedPair(
                        TextParsers.tag("abc"), TextParsers.tag("|"), TextParsers.tag("efg"));

        assertThatParseResult(parser.parse("abc|efg"))
                .isOk(new Pair<>("abc", "efg"))
                .hasRemainingInput("");
    }

    @Test
    void terminated() {
        final Parser<String> parser =
                Sequences.terminated(TextParsers.tag("abc"), TextParsers.tag("efg"));

        assertThatParseResult(parser.parse("abcefg")).isOk("abc").hasRemainingInput("");
    }

    @Test
    void triple() {
        final Parser<Triple<String, String, String>> parser =
                Sequences.triple(TextParsers.alpha1(), TextParsers.digit1(), TextParsers.alpha1());

        assertThatParseResult(parser.parse("abc123def"))
                .isOk(new Triple<>("abc", "123", "def"))
                .hasRemainingInput("");

        assertThatParseResult(parser.parse("123def"))
                .isError(ParseError.Alpha)
                .hasRemainingInput("123def");
    }
}
