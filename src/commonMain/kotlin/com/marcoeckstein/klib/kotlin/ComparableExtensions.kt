package com.marcoeckstein.klib.kotlin

//#region Nullable Comparable comparison

// See: https://stackoverflow.com/questions/29223898/

infix fun <T : Comparable<T>> T?.isGreaterThan(other: T?): Boolean? =
    if (this != null && other != null) this > other else null

infix fun <T : Comparable<T>> T?.isGreaterThanOrEqual(other: T?): Boolean? =
    if (this != null && other != null) this >= other else null

infix fun <T : Comparable<T>> T?.isLessThan(other: T?): Boolean? =
    if (this != null && other != null) this < other else null

infix fun <T : Comparable<T>> T?.isLessThanOrEqual(other: T?): Boolean? =
    if (this != null && other != null) this <= other else null

//#endregion
