package com.marcoeckstein.klib.kotlin.ranges

import com.marcoeckstein.klib.TestData
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class IntRangeExtensionsTests {

    @Test
    fun binarySearch_finds_number() {
        TestData.integers.forAll { number ->
            (Int.MIN_VALUE..Int.MAX_VALUE).binarySearch { it.compareTo(number) } shouldBe number
        }
    }

    @Test
    fun binarySearch_works_for_various_ranges() {
        (0..10).binarySearch { it.compareTo(5) } shouldBe 5
        (0..10).binarySearch { it.compareTo(50) } shouldBe null
        (10..100).binarySearch { it.compareTo(50) } shouldBe 50
        (-100..-10).binarySearch { it.compareTo(-50) } shouldBe -50
        (0..0).binarySearch { it.compareTo(0) } shouldBe 0
        (0..0).binarySearch { it.compareTo(5) } shouldBe null
    }

    @Test
    fun binarySearch_returns_null_when_there_is_no_number() {
        (Int.MIN_VALUE..Int.MAX_VALUE).binarySearch { 1 } shouldBe null
    }
}
