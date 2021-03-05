package com.marcoeckstein.klib.kotlin.ranges

fun IntRange.binarySearch(comparison: (Int) -> Int): Int? =
    (0L..last.toLong() - first.toLong())
        .binarySearch { comparison((it + first).toInt()) }
        ?.let { it + first }
        ?.toInt()

// Adapted from List<T>.binarySearch(fromIndex: Int = 0, toIndex: Int = size, comparison: (T) -> Int)
private fun LongRange.binarySearch(comparison: (Long) -> Int): Long? {
    require(first == 0L)
    require(last >= 0L)
    var low = first
    var high = last
    while (low <= high) {
        if (low < first || high > last)
            return null
        val mid = (low + high).ushr(1) // safe from overflows
        val cmp = comparison(mid)
        when {
            cmp < 0 -> low = mid + 1
            cmp > 0 -> high = mid - 1
            else -> return mid
        }
    }
    return null
}
