package com.aryk.covid.extensions

@SuppressWarnings("MagicNumber")
fun Int.toShorterFormatString(): String {
    return when {
        this > 999999999 -> {
            (this / 1000000000).toString() + "B"
        }
        this > 999999 -> {
            (this / 1000000).toString() + "M"
        }
        this > 999 -> {
            (this / 1000).toString() + " k"
        }
        else -> {
            this.toString()
        }
    }
}
