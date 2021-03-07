package com.marcoeckstein.klib.kotlin

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class AnyExtensionsTests {

    private val nullString: String? = null

    @Test
    fun stringify_works() {
        "a" + nullString.stringify() shouldBe "a"
        "a" + "b".stringify() shouldBe "ab"
        "a" + nullString.stringify { it.toUpperCase() } shouldBe "a"
        "a" + "b".stringify { it.toUpperCase() } shouldBe "aB"
    }
}
