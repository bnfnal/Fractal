package ru.bnfnal.math.fractals

import org.kotlinmath.Complex

interface Fractal {
    fun isInSet(c: Complex): Double
}