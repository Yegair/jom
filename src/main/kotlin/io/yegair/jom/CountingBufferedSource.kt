package io.yegair.jom

import io.yegair.jom.Utf8CodePoints.utf8Size
import okio.Buffer
import okio.BufferedSource
import okio.ByteString
import okio.Options
import okio.Sink
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal class CountingBufferedSource(
    private val source: BufferedSource,
    private var count: Long = 0
) : BufferedSource by source {

    val bytesProcessed: Long
        get() = count

    private fun Int.count(): Int {
        count += this
        return this
    }

    private fun Long.count(): Long {
        count += this
        return this
    }

    override fun peek(): CountingBufferedSource =
        CountingBufferedSource(source.peek(), count)

    override fun read(sink: ByteArray): Int =
        source.read(sink).count()

    override fun read(sink: ByteArray, offset: Int, byteCount: Int): Int =
        source.read(sink, offset, byteCount).count()

    override fun read(sink: Buffer, byteCount: Long): Long =
        source.read(sink, byteCount).count()

    override fun read(dst: ByteBuffer?): Int =
        source.read(dst).count()

    override fun readAll(sink: Sink): Long =
        source.readAll(sink).count()

    override fun readByte(): Byte =
        source.readByte().also { 1.count() }

    override fun readByteArray(): ByteArray =
        source.readByteArray().also { it.size.count() }

    override fun readByteArray(byteCount: Long): ByteArray =
        source.readByteArray(byteCount).also { byteCount.count() }

    override fun readByteString(): ByteString =
        source.readByteString().also { it.size.count() }

    override fun readByteString(byteCount: Long): ByteString =
        source.readByteString(byteCount).also { byteCount.count() }

    override fun readDecimalLong(): Long =
        throw UnsupportedOperationException("byte counting for readDecimalLong is not supported")

    override fun readFully(sink: ByteArray) =
        source.readFully(sink).also { sink.size.count() }

    override fun readFully(sink: Buffer, byteCount: Long) =
        source.readFully(sink, byteCount).also { byteCount.count() }

    override fun readHexadecimalUnsignedLong(): Long =
        throw UnsupportedOperationException("byte counting for readHexadecimalUnsignedLong is not supported")

    override fun readInt(): Int =
        source.readInt().also { 4.count() }

    override fun readIntLe(): Int =
        source.readIntLe().also { 4.count() }

    override fun readLong(): Long =
        source.readLong().also { 8.count() }

    override fun readLongLe(): Long =
        source.readLongLe().also { 8.count() }

    override fun readShort(): Short =
        source.readShort().also { 2.count() }

    override fun readShortLe(): Short =
        source.readShortLe().also { 2.count() }

    override fun readString(charset: Charset): String =
        source.readString(charset).also {
            // TODO: find a more efficient solution as this is most likely very inefficient
            it.toByteArray(charset).size.count()
        }

    override fun readString(byteCount: Long, charset: Charset): String =
        source.readString(byteCount, charset).also { byteCount.count() }

    override fun readUtf8(): String =
        readString(Charsets.UTF_8)

    override fun readUtf8(byteCount: Long): String =
        readString(byteCount, Charsets.UTF_8)

    override fun readUtf8CodePoint(): Int =
        source.readUtf8CodePoint().also { it.utf8Size().count() }

    override fun readUtf8Line(): String =
        throw UnsupportedOperationException("byte counting for readUtf8Line is not supported")

    override fun readUtf8LineStrict(): String =
        throw UnsupportedOperationException("byte counting for readUtf8LineStrict is not supported")

    override fun readUtf8LineStrict(limit: Long): String =
        throw UnsupportedOperationException("byte counting for readUtf8LineStrict is not supported")

    override fun select(options: Options): Int =
        throw UnsupportedOperationException("byte counting for select is not supported")

    override fun skip(byteCount: Long) =
        source.skip(byteCount).also { byteCount.count() }
}
