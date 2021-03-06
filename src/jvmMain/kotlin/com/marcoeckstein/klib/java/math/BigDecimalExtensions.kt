package com.marcoeckstein.klib.java.math

import java.math.BigDecimal

infix fun BigDecimal.equalsComparing(other: BigDecimal) =
    compareTo(other) == 0

infix fun BigDecimal.notEqualsComparing(other: BigDecimal) =
    compareTo(other) != 0
