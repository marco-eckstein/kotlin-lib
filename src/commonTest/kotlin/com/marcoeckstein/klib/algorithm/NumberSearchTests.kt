package com.marcoeckstein.klib.algorithm

import com.marcoeckstein.klib.TestData
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlin.test.assertNotNull

class NumberSearchTests {

    @Test
    fun findNumberBounds_finds_bounds() {
        TestData.integers.forAll { number ->
            val bounds = findNumberBounds { it.contains(number) }
            assertNotNull(bounds) // "bounds shouldNotBe null" causes heap space issues.
            bounds.first shouldBeLessThanOrEqual number
            bounds.last shouldBeGreaterThanOrEqual number
        }
    }

    @Test
    fun findNumberBounds_returns_null_when_there_are_no_bounds() {
        findNumberBounds { false } shouldBe null
    }

    @Test
    fun findNumber_finds_number() {
        TestData.integers.forAll { number ->
            findNumber { it.contains(number) } shouldBe number
        }
    }

    @Test
    fun findNumber_returns_null_when_there_is_no_number() {
        findNumber { false } shouldBe null
    }
}
