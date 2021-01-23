package io.yegair.jom;

import static io.yegair.jom.test.ParseResultAssert.assertThatParseResult;

import org.junit.jupiter.api.Test;

/** Tests Java interoperability when writing custom parsers. */
class CustomParserTest {

    @Test
    void shouldCompile() {
        final Parser<Boolean> parser =
                input -> {
                    final Input peek = input.peek();
                    final int codePoint = peek.readUtf8CodePoint();

                    switch (codePoint) {
                        case (int) 'y':
                        case (int) 'Y':
                            return ParseResult.ok(peek, true);
                        case (int) 'n':
                        case (int) 'N':
                            return ParseResult.ok(peek, false);
                        default:
                            return ParseResult.error(input, ParseError.Char);
                    }
                };

        assertThatParseResult(parser.parse("y")).isOk(true).hasRemainingInput("");
        assertThatParseResult(parser.parse("Y")).isOk(true).hasRemainingInput("");
        assertThatParseResult(parser.parse("n")).isOk(false).hasRemainingInput("");
        assertThatParseResult(parser.parse("N")).isOk(false).hasRemainingInput("");
    }
}
