package com.marcoeckstein.klib.kotlin

fun <T> T?.stringify(toString: (T) -> String = { it.toString() }): String =
    if (this == null) "" else toString(this)
