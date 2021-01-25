@file:Suppress("ClassName")

package io.yegair.jom

import io.yegair.jom.Parsers.takeBytes
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

class takeBytes {

    @Test
    fun `should take 1-byte utf8 code points`() {
        val parser = takeBytes(6)

        assertThatParseResult(parser.parse("1234567"))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk("123456".toByteArray())
            .hasRemainingInput("7")
    }

    @Test
    fun `should take 2-byte utf8 code points`() {
        val parser = takeBytes(6)

        assertThatParseResult(parser.parse("Καλημέρα"))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk("Καλ".toByteArray())
            .hasRemainingInput("ημέρα")
    }

    @Test
    fun `should take partial 2-byte utf8 code points`() {
        val parser = takeBytes(3)

        assertThatParseResult(parser.parse("Καλ"))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk(byteArrayOf(0xce.toByte(), 0x9a.toByte(), 0xce.toByte()))
            .hasRemainingInput(byteArrayOf(0xb1.toByte(), 0xce.toByte(), 0xbb.toByte()))
    }

    @Test
    fun `should take 3-byte utf8 code points`() {
        val parser = takeBytes(6)

        assertThatParseResult(parser.parse("こんにちは"))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk("こん".toByteArray())
            .hasRemainingInput("にちは")
    }

    @Test
    fun `should take partial 3-byte utf8 code points`() {
        val parser = takeBytes(4)

        assertThatParseResult(parser.parse("こん"))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk(byteArrayOf(0xe3.toByte(), 0x81.toByte(), 0x93.toByte(), 0xe3.toByte()))
            .hasRemainingInput(byteArrayOf(0x82.toByte(), 0x93.toByte()))
    }

    @Test
    fun `should take 4-byte utf8 code points`() {
        val parser = takeBytes(4)

        assertThatParseResult(parser.parse("👆👏"))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk("👆".toByteArray())
            .hasRemainingInput("👏")
    }

    @Test
    fun `should take partial 4-byte utf8 code points`() {
        val parser = takeBytes(7)

        assertThatParseResult(parser.parse("👆👏"))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk(
                byteArrayOf(
                    0xf0.toByte(),
                    0x9f.toByte(),
                    0x91.toByte(),
                    0x86.toByte(),
                    0xf0.toByte(),
                    0x9f.toByte(),
                    0x91.toByte()
                )
            )
            .hasRemainingInput(byteArrayOf(0x8f.toByte()))
    }

    @Test
    fun `should succeed if there are exactly enough bytes available`() {
        val parser = takeBytes(3)

        assertThatParseResult(parser.parse(byteArrayOf(0x01, 0x02, 0x03)))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk(byteArrayOf(0x01, 0x02, 0x03))
            .hasRemainingInput(byteArrayOf())
    }

    @Test
    fun `should fail if there are not enough bytes available`() {
        val parser = takeBytes(4)

        assertThatParseResult(parser.parse(byteArrayOf(0x01, 0x02, 0x03)))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isError(ParseError.Eof)
            .hasRemainingInput(byteArrayOf(0x01, 0x02, 0x03))
    }

    @Test
    fun `should fail when input is empty`() {
        val parser = takeBytes(1)

        assertThatParseResult(parser.parse(byteArrayOf()))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isError(ParseError.Eof)
            .hasRemainingInput(byteArrayOf())
    }

    @Test
    fun `should succeed when parsing zero bytes`() {
        val parser = takeBytes(0)

        assertThatParseResult(parser.parse(byteArrayOf(0x01, 0x02)))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk(byteArrayOf())
            .hasRemainingInput(byteArrayOf(0x01, 0x02))
    }

    @Test
    fun `should succeed when parsing zero bytes from empty input`() {
        val parser = takeBytes(0)

        assertThatParseResult(parser.parse(byteArrayOf()))
            .usingOutputComparator(ByteArray?::contentEquals)
            .isOk(byteArrayOf())
            .hasRemainingInput(byteArrayOf())
    }
}
