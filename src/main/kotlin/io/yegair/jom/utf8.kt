package io.yegair.jom

import okio.Buffer
import okio.EOFException
import java.nio.charset.StandardCharsets

typealias Utf8CodePoint = Int

internal fun Char.toUtf8CodePoint(): Utf8CodePoint = toInt()

internal fun Utf8CodePoint.isAlpha(): Boolean = toChar().isAlpha()

internal fun Utf8CodePoint.isAlphaNumeric(): Boolean = toChar().isAlphaNumeric()

internal fun Utf8CodePoint.isDigit(): Boolean = toChar().isDigit()

internal fun Utf8CodePoint.isHexDigit(): Boolean = toChar().isHexDigit()

internal fun Utf8CodePoint.isOctDigit(): Boolean = toChar().isOctDigit()

internal fun Utf8CodePoint.isMultiSpace(): Boolean = toChar().isMultiSpace()

internal fun Utf8CodePoint.isSpace(): Boolean = toChar().isSpace()

internal fun Utf8CodePoint.isChar(chr: Char): Boolean = toChar() == chr

internal fun String.toUtf8CodePoints(): List<Utf8CodePoint> {
    val buf = Buffer()
    buf.writeString(this, StandardCharsets.UTF_8)

    val codePoints = mutableListOf<Utf8CodePoint>()

    try {
        while (true) {
            codePoints.add(buf.readUtf8CodePoint())
        }
    } catch (ignored: EOFException) {
        // ignored
    }

    return codePoints
}

internal fun Utf8CodePoint.utf8(): String {
    return String(Character.toChars(this))
}

object Utf8CodePoints {

    @JvmStatic
    @JvmName("sizeOf")
    fun Utf8CodePoint.utf8Size(): Int {
        return when {
            this and 0xFF000000.toInt() != 0 -> 4
            this and 0x00FF0000 != 0 -> 3
            this and 0x0000FF00 != 0 -> 2
            else -> 1
        }
    }

    @JvmStatic
    fun Utf8CodePoint?.equalsIgnoreCase(other: Utf8CodePoint?): Boolean {
        return when {
            this == other -> true
            this == null -> false
            other == null -> false
            else -> {
                val first = String(Character.toChars(this))
                val second = String(Character.toChars(other))
                return first.equals(second, ignoreCase = true)
            }
        }
    }
}
