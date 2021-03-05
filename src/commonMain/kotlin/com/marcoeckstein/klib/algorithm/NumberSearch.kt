package com.marcoeckstein.klib.algorithm

import com.marcoeckstein.klib.kotlin.ranges.binarySearch
import kotlin.math.absoluteValue
import kotlin.math.sqrt

fun findNumber(isNumberInRange: (IntRange) -> Boolean): Int? {
    val bounds = findNumberBounds(isNumberInRange) ?: return null
    return bounds.binarySearch { candidate ->
        if (isNumberInRange(candidate..bounds.last))
            if (candidate < Int.MAX_VALUE && isNumberInRange((candidate + 1)..bounds.last))
                -1 // candidate is too small
            else
                0 // found number
        else
            1 // candidate is too large
    }
}

fun findNumberBounds(isNumberInRange: (IntRange) -> Boolean): IntRange? =
    findNumberBounds(0..0, isNumberInRange)

private fun findNumberBounds(candidate: IntRange, isNumberInRange: (IntRange) -> Boolean): IntRange? {
    require(candidate.first <= 0) { "candidate.first must be <= 0, but was ${candidate.first}." }
    require(candidate.last >= 0) { "candidate.last must be >= 0, but was ${candidate.last}." }
    return if (isNumberInRange(candidate))
        candidate
    else {
        val nextCandidate = nextRangeInExponentialSequence(candidate) ?: return null
        findNumberBounds(nextCandidate, isNumberInRange)
    }
}

// Note that Int.MIN_VALUE == (Int.MAX_VALUE * -1) - 1
private fun nextRangeInExponentialSequence(range: IntRange): IntRange? {
    return when (range.first) {
        Int.MIN_VALUE -> null
        Int.MIN_VALUE + 1 -> {
            require(range.last == Int.MAX_VALUE)
            Int.MIN_VALUE..Int.MAX_VALUE
        }
        else -> {
            val nextFirst = nextNumberInExponentialSequence(range.first, sign = -1) ?: return null
            val nextLast = nextNumberInExponentialSequence(range.last, sign = 1) ?: return null
            nextFirst..nextLast
        }
    }
}

// Note that Int.MIN_VALUE.absoluteValue == Int.MIN_VALUE due to an overflow
private fun nextNumberInExponentialSequence(i: Int, sign: Int): Int? {
    require(sign in setOf(1, -1))
    require(i != Int.MIN_VALUE)
    val abs = i.absoluteValue
    return when {
        abs < 2 -> abs + 1
        abs <= sqrt(Int.MAX_VALUE.toDouble()) -> abs * abs
        abs < Int.MAX_VALUE -> Int.MAX_VALUE
        else -> null
    }?.let { it * sign }
}
