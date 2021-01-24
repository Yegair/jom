package io.yegair.jom;

import static io.yegair.jom.test.ParseResultAssert.assertThatParseResult;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import kotlin.Pair;
import kotlin.Unit;
import org.junit.jupiter.api.Test;

class CombinatorsTest {
    @Test
    void allConsuming() {
        final Parser<String> parser = Combinators.allConsuming(Parsers.alpha1());

        assertThatParseResult(parser.parse("abcd")).isOk("abcd").hasRemainingInput("");
    }

    @Test
    void cond() {
        final Parser<String> enabled = Combinators.cond(true, Parsers.alpha1());
        final Parser<String> disabled = Combinators.cond(false, Parsers.alpha1());

        assertThatParseResult(enabled.parse("abcd;")).isOk("abcd").hasRemainingInput(";");

        assertThatParseResult(disabled.parse("abcd;")).isOk(null).hasRemainingInput("abcd;");
    }

    @Test
    void consumed() {
        final Parser<Pair<Boolean, String>> parser =
                Combinators.consumed(
                        Combinators.value(
                                true,
                                Combinators.separatedPair(
                                        Parsers.alpha1(), Parsers.chr(','), Parsers.alpha1())),
                        StandardCharsets.UTF_8);

        assertThatParseResult(parser.parse("abcd,efgh1"))
                .isOk(new Pair<>(true, "abcd,efgh"))
                .hasRemainingInput("1");
    }

    @Test
    void count() {
        final Parser<List<String>> parser = Combinators.count(Parsers.tag("abc"), 2);

        assertThatParseResult(parser.parse("abcabc"))
                .isOk(Arrays.asList("abc", "abc"))
                .hasRemainingInput("");
    }

    @Test
    void many0() {
        final Parser<List<String>> parser = Combinators.many0(Parsers.tag("abc"));

        assertThatParseResult(parser.parse("abcabc"))
                .isOk(Arrays.asList("abc", "abc"))
                .hasRemainingInput("");
    }

    @Test
    void many1() {
        final Parser<List<String>> parser = Combinators.many1(Parsers.tag("abc"));

        assertThatParseResult(parser.parse("abcabc"))
                .isOk(Arrays.asList("abc", "abc"))
                .hasRemainingInput("");
    }

    @Test
    void map() {
        final Parser<Integer> parser = Combinators.map(Parsers.digit1(), String::length);

        assertThatParseResult(parser.parse("123456")).isOk(6).hasRemainingInput("");
    }

    @Test
    void not() {
        final Parser<Unit> parser = Combinators.not(Parsers.alpha1());

        assertThatParseResult(parser.parse("123")).isOk(Unit.INSTANCE).hasRemainingInput("123");

        assertThatParseResult(parser.parse("abcd"))
                .isError(ParseError.Not)
                .hasRemainingInput("abcd");
    }

    @Test
    void opt() {
        final Parser<String> parser = Combinators.opt(Parsers.alpha1());

        assertThatParseResult(parser.parse("abcd;")).isOk("abcd").hasRemainingInput(";");

        assertThatParseResult(parser.parse("123;")).isOk(null).hasRemainingInput("123;");
    }

    @Test
    void peek() {
        final Parser<String> parser = Combinators.peek(Parsers.alpha1());

        assertThatParseResult(parser.parse("abcd;")).isOk("abcd").hasRemainingInput("abcd;");
    }

    @Test
    void recognize() {
        final Parser<String> parser =
                Combinators.recognize(
                        Combinators.separatedPair(
                                Parsers.alpha1(), Parsers.chr(','), Parsers.alpha1()),
                        StandardCharsets.UTF_8);

        assertThatParseResult(parser.parse("abcd,efgh")).isOk("abcd,efgh").hasRemainingInput("");

        assertThatParseResult(parser.parse("abcd;"))
                .isError(ParseError.Char)
                .hasRemainingInput("abcd;");
    }

    @Test
    void success() {
        final Parser<Integer> parser = Combinators.success(10);

        assertThatParseResult(parser.parse("xyz")).isOk(10).hasRemainingInput("xyz");
    }

    @Test
    void value() {
        final Parser<Integer> parser = Combinators.value(1234, Parsers.alpha1());

        assertThatParseResult(parser.parse("abcd")).isOk(1234).hasRemainingInput("");
    }

    @Test
    void verify() {
        final Parser<String> parser =
                Combinators.verify(Parsers.alpha1(), output -> output.length() == 4);

        assertThatParseResult(parser.parse("abcd")).isOk("abcd").hasRemainingInput("");

        assertThatParseResult(parser.parse("abcde"))
                .isError(ParseError.Verify)
                .hasRemainingInput("abcde");
    }
}
