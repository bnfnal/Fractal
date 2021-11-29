package ru.bnfnal.math.fractals

import org.kotlinmath.Complex
import org.kotlinmath.complex
import ru.bnfnal.math.complex.mod2

// интересный факт
// существует единственное множество Мандельброта с точностью до R
// заменили функцию на объект, так как он существует в единственном виде

object Mandelbrot : Fractal {
    // определяет принадлежит ли тоска множеству Мандельброта
    private var R2 = 4.0
    var R = 2.0
        set(value) {
            field = Math.max(Math.abs(value), 2 * Double.MIN_VALUE)    // Double.MIN_VALUE = наим положит число, содерж в типе Double
            R2 = value * value
        }
    var maxIterations: Int = 200

    override fun isInSet(c: Complex): Double{
        var z = complex(0, 0)
        // повторение кода заданное кол-во раз
        for (i in 0 until maxIterations) {
            z = z * z + c
            if (z.mod2 >= R2) return (i.toDouble() / maxIterations)
            // z. mod можно исп для раскраски внутренности множества
            // для раскраски внешности будем исп i/maxIterations
        }
        return 1.0
    }

}