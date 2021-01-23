package io.yegair.jom

import okio.Buffer
import okio.BufferedSource
import okio.ByteString
import okio.ByteString.Companion.EMPTY
import okio.ByteString.Companion.encodeUtf8
import okio.EOFException

/**
 * A sequential source of symbols/elements that can be parsed.
 */
class Input private constructor(
    private val source: CountingBufferedSource
) {

    /**
     * Returns a new Input that can read symbols from this Input without consuming them.
     */
    fun peek(): Input = Input(source.peek())

    fun skip(byteCount: Long) {
        source.skip(byteCount)
    }

    val bytesProcessed: Long
        get() = source.bytesProcessed

    /**
     * Indicates whether the end of this input has been reached (aka. EOF).
     */
    fun exhausted(): Boolean = source.exhausted()

    fun readByteString(byteCount: Long): ByteString = try {
        source.readByteString(byteCount)
    } catch (e: EOFException) {
        EMPTY
    }

    /**
     * Reads and consumes a single UTF8 code point (1-4 bytes).
     */
    fun readUtf8CodePoint(): Utf8CodePoint = when {
        source.exhausted() -> -1
        else -> try {
            source.readUtf8CodePoint()
        } catch (e: EOFException) {
            -1
        }
    }

    fun readUtf8(): String = source.readUtf8()

    fun readByteArray(): ByteArray = source.readByteArray()

    override fun toString(): String {
        val peek = source.peek()

        if (peek.request(32)) {
            return peek.readByteString(32).toString()
        }

        return peek.readByteString().toString()
    }

    companion object {
        fun isEof(chr: Int): Boolean {
            return chr < 0
        }

        @JvmStatic
        fun of(source: BufferedSource): Input {
            return Input(CountingBufferedSource(source))
        }

        @JvmStatic
        fun of(byteString: ByteString): Input {
            return of(
                Buffer().let {
                    it.write(byteString)
                    it
                }
            )
        }

        @JvmStatic
        fun of(text: String): Input {
            return of(text.encodeUtf8())
        }

        @JvmStatic
        fun of(bytes: ByteArray): Input {
            return of(
                Buffer().let {
                    it.write(bytes)
                    it
                }
            )
        }
    }
}
