package com.marcoeckstein.klib.kotlin

import kotlin.js.JsExport

@JsExport
fun <T> T?.stringify(toString: (T) -> String = { it.toString() }): String =
    if (this == null) "" else toString(this)
