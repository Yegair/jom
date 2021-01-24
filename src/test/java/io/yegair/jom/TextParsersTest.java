package io.yegair.jom;

import static io.yegair.jom.test.ParseResultAssert.assertThatParseResult;

import org.junit.jupiter.api.Test;

/**
 * Tests the Java interoperability of text parsers. For more extensive tests of the parsers
 * functionality have a look at the Kotlin tests.
 */
class TextParsersTest {
    @Test
    void alpha0() {
        final Parser<String> parser = TextParsers.alpha0();

        assertThatParseResult(parser.parse("ab1c")).isOk("ab").hasRemainingInput("1c");
    }

    @Test
    void alpha1() {
        final Parser<String> parser = TextParsers.alpha1();

        assertThatParseResult(parser.parse("aB1c")).isOk("aB").hasRemainingInput("1c");
    }

    @Test
    void alphaNumeric0() {
        final Parser<String> parser = TextParsers.alphaNumeric0();

        assertThatParseResult(parser.parse("21cZ%1")).isOk("21cZ").hasRemainingInput("%1");
    }

    @Test
    void alphaNumeric1() {
        final Parser<String> parser = TextParsers.alphaNumeric1();

        assertThatParseResult(parser.parse("21cZ%1")).isOk("21cZ").hasRemainingInput("%1");
    }

    @Test
    void anyChar() {
        final Parser<Character> parser = TextParsers.anyChar();

        assertThatParseResult(parser.parse("abc")).isOk('a').hasRemainingInput("bc");
    }

    @Test
    void chr() {
        final Parser<Character> parser = TextParsers.chr('a');

        assertThatParseResult(parser.parse("abc")).isOk('a').hasRemainingInput("bc");
    }

    @Test
    void crlf() {
        final Parser<String> parser = TextParsers.crlf();

        assertThatParseResult(parser.parse("\r\nc")).isOk("\r\n").hasRemainingInput("c");
    }

    @Test
    void digit0() {
        final Parser<String> parser = TextParsers.digit0();

        assertThatParseResult(parser.parse("21c")).isOk("21").hasRemainingInput("c");
    }

    @Test
    void digit1() {
        final Parser<String> parser = TextParsers.digit1();

        assertThatParseResult(parser.parse("21c")).isOk("21").hasRemainingInput("c");
    }

    @Test
    void hexDigit0() {
        final Parser<String> parser = TextParsers.hexDigit0();

        assertThatParseResult(parser.parse("CAFEBABE;")).isOk("CAFEBABE").hasRemainingInput(";");
    }

    @Test
    void hexDigit1() {
        final Parser<String> parser = TextParsers.hexDigit1();

        assertThatParseResult(parser.parse("CAFEBABE;")).isOk("CAFEBABE").hasRemainingInput(";");
    }

    @Test
    void lineEnding() {
        final Parser<String> parser = TextParsers.lineEnding();

        assertThatParseResult(parser.parse("\nc")).isOk("\n").hasRemainingInput("c");

        assertThatParseResult(parser.parse("\r\nc")).isOk("\r\n").hasRemainingInput("c");
    }

    @Test
    void multiSpace0() {
        final Parser<String> parser = TextParsers.multiSpace0();

        assertThatParseResult(parser.parse(" \t\n\r21c")).isOk(" \t\n\r").hasRemainingInput("21c");
    }

    @Test
    void multiSpace1() {
        final Parser<String> parser = TextParsers.multiSpace1();

        assertThatParseResult(parser.parse(" \t\n\r21c")).isOk(" \t\n\r").hasRemainingInput("21c");
    }

    @Test
    void newline() {
        final Parser<Character> parser = TextParsers.newline();

        assertThatParseResult(parser.parse("\n")).isOk('\n').hasRemainingInput("");
    }

    @Test
    void noneOf() {
        final Parser<String> parser = TextParsers.noneOf("abc");

        assertThatParseResult(parser.parse("z")).isOk("z").hasRemainingInput("");
    }

    @Test
    void notLineEnding() {
        final Parser<String> parser = TextParsers.notLineEnding();

        assertThatParseResult(parser.parse("ab\r\nc")).isOk("ab").hasRemainingInput("\r\nc");
    }

    @Test
    void octDigit0() {
        final Parser<String> parser = TextParsers.octDigit0();

        assertThatParseResult(parser.parse("678")).isOk("67").hasRemainingInput("8");
    }

    @Test
    void octDigit1() {
        final Parser<String> parser = TextParsers.octDigit1();

        assertThatParseResult(parser.parse("678")).isOk("67").hasRemainingInput("8");
    }

    @Test
    void oneOf() {
        final Parser<String> parser = TextParsers.oneOf("abc");

        assertThatParseResult(parser.parse("abc")).isOk("a").hasRemainingInput("bc");
    }

    @Test
    void satisfy() {
        final Parser<Integer> parser = TextParsers.satisfy(c -> c == (int) 'a' || c == (int) 'b');

        assertThatParseResult(parser.parse("abc")).isOk((int) 'a').hasRemainingInput("bc");
    }

    @Test
    void space0() {
        final Parser<String> parser = TextParsers.space0();

        assertThatParseResult(parser.parse(" \t\n\r21c")).isOk(" \t").hasRemainingInput("\n\r21c");
    }

    @Test
    void space1() {
        final Parser<String> parser = TextParsers.space1();

        assertThatParseResult(parser.parse(" \t\n\r21c")).isOk(" \t").hasRemainingInput("\n\r21c");
    }

    @Test
    void tab() {
        final Parser<Character> parser = TextParsers.tab();

        assertThatParseResult(parser.parse("\t c")).isOk('\t').hasRemainingInput(" c");
    }

    @Test
    void tag() {
        final Parser<String> parser = TextParsers.tag("Hello");

        assertThatParseResult(parser.parse("Hello, World!"))
                .isOk("Hello")
                .hasRemainingInput(", World!");
    }

    @Test
    void tagNoCase() {
        final Parser<String> parser = TextParsers.tagNoCase("hello");

        assertThatParseResult(parser.parse("Hello, World!"))
                .isOk("Hello")
                .hasRemainingInput(", World!");
    }
}
