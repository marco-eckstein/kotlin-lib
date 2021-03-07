package com.marcoeckstein.klib.kotlin

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ComparableExtensionsTests {

    private val nullInt: Int? = null

    @Test
    fun isGreaterThan_works() {
        1 isGreaterThan 1 shouldBe false
        0 isGreaterThan 1 shouldBe false
        1 isGreaterThan 0 shouldBe true
        nullInt isGreaterThan 1 shouldBe null
        1 isGreaterThan nullInt shouldBe null
    }

    @Test
    fun isGreaterThanOrEqual_works() {
        1 isGreaterThanOrEqual 1 shouldBe true
        0 isGreaterThanOrEqual 1 shouldBe false
        1 isGreaterThanOrEqual 0 shouldBe true
        nullInt isGreaterThanOrEqual 1 shouldBe null
        1 isGreaterThanOrEqual nullInt shouldBe null
    }

    @Test
    fun isLessThan_works() {
        1 isLessThan 1 shouldBe false
        0 isLessThan 1 shouldBe true
        1 isLessThan 0 shouldBe false
        nullInt isLessThan 1 shouldBe null
        1 isLessThan nullInt shouldBe null
    }

    @Test
    fun isLessThanOrEqual_works() {
        1 isLessThanOrEqual 1 shouldBe true
        0 isLessThanOrEqual 1 shouldBe true
        1 isLessThanOrEqual 0 shouldBe false
        nullInt isLessThanOrEqual 1 shouldBe null
        1 isLessThanOrEqual nullInt shouldBe null
    }
}
