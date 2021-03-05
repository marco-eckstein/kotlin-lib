package com.marcoeckstein.klib.java.math

import java.math.BigDecimal

infix fun BigDecimal.equalsComparingTo(other: BigDecimal) =
    compareTo(other) == 0

infix fun BigDecimal.notEqualsComparingTo(other: BigDecimal) =
    compareTo(other) != 0
