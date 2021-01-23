package io.yegair.jom;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Utf8CodePointsTest {

    private static Stream<Arguments> utf8SizeParams() {
        return Stream.of(
                Arguments.of(0x00000000, 1),
                Arguments.of(0x00000001, 1),
                Arguments.of(0x000000FF, 1),
                Arguments.of(0x0000C2A0, 2),
                Arguments.of(0x0000DFBF, 2),
                Arguments.of(0x00E0A080, 3),
                Arguments.of(0x00EFBFBF, 3),
                Arguments.of(0xF0928080, 4),
                Arguments.of(0xF09FA7BF, 4));
    }

    @ParameterizedTest(name = "[{index}]")
    @MethodSource("utf8SizeParams")
    void utf8Size(int codePoint, int expected) {
        assertThat(Utf8CodePoints.sizeOf(codePoint)).isEqualTo(expected);
    }
}
