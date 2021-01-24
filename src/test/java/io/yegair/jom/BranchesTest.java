package io.yegair.jom;

import static io.yegair.jom.test.ParseResultAssert.assertThatParseResult;

import kotlin.Pair;
import kotlin.Triple;
import org.junit.jupiter.api.Test;

class BranchesTest {

    @Test
    void alt() {
        final Parser<String> parser = Combinators.alt(Parsers.alpha1(), Parsers.digit1());

        // the first parser returns an error, so alt tries the second one
        assertThatParseResult(parser.parse("123456")).isOk("123456").hasRemainingInput("");
    }

    @Test
    void permutation2() {
        final Parser<Pair<String, String>> parser =
                Combinators.permutation2(Parsers.alpha1(), Parsers.digit1());

        assertThatParseResult(parser.parse("123abc"))
                .isOk(new Pair<>("abc", "123"))
                .hasRemainingInput("");
    }

    @Test
    void permutation3() {
        final Parser<Triple<String, String, String>> parser =
                Combinators.permutation3(Parsers.alpha1(), Parsers.digit1(), Parsers.lineEnding());

        assertThatParseResult(parser.parse("123\nabc"))
                .isOk(new Triple<>("abc", "123", "\n"))
                .hasRemainingInput("");
    }
}
