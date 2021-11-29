package ru.bnfnal

import org.kotlinmath.Complex
import org.kotlinmath.DefaultComplex
import org.kotlinmath.I
import org.kotlinmath.complex
import ru.bnfnal.math.complex.mod2
import ru.bnfnal.ui.MainFrame

fun main(args: Array<String>) {
    //https://mvnrepository.com
//    val z = I * 3.0 + 2.0
//    val z1 = DefaultComplex(2.0, 3.0)
//    val z2 = complex(2.0, 3.0)
//    var z3 = z2 * z1 + z
//    z3.mod
//    z3.mod2
    // нужно будет перебрать все пиксели на панели, если каждую прокрутить по 200 раз,
    // мы потратим очень много времени на подсчет квадратного корня
    // обычно берем R = 2, будем сравнивать квадраты величин, чтобы не тратить время на корень и ускорить вычислительный процесс
    // напишем функцию/свойство расширения для вычисления квадрата модуля

    val wnd = MainFrame()
    wnd.isVisible = true

}

