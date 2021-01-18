package io.yegair.jom

internal fun Char.isAlpha(): Boolean = this in 'A'..'Z' || this in 'a'..'z'

internal fun Char.isAlphaNumeric(): Boolean = this.isAlpha() || this.isDigit()

internal fun Char.isHexDigit(): Boolean = this in 'A'..'F' || this in 'a'..'f' || this.isDigit()

internal fun Char.isOctDigit(): Boolean = this in '0'..'7'

internal fun Char.isMultiSpace(): Boolean = this == ' ' || this == '\t' || this == '\r' || this == '\n'

internal fun Char.isSpace(): Boolean = this == ' ' || this == '\t'

internal fun Char.equalsIgnoreCase(other: Char): Boolean {
    if (this == other) {
        return true
    }

    // If characters don't match try converting both characters to uppercase.
    // If the results match, then the comparison scan should
    // continue.
    val u1 = Character.toUpperCase(this)
    val u2 = Character.toUpperCase(other)

    if (u1 == u2) {
        return true
    }

    // Unfortunately, conversion to uppercase does not work properly
    // for the Georgian alphabet, which has strange rules about case
    // conversion.  So we need to make one last check before
    // exiting.
    return Character.toLowerCase(u1) == Character.toLowerCase(u2)
}
