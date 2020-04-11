package com.aryk.covid.extensions

import java.text.NumberFormat

fun Double?.toLocalFormattedString(): String {
    this?.let {
        return NumberFormat.getInstance().format(it)
    } ?: run {
        return "_"
    }
}
