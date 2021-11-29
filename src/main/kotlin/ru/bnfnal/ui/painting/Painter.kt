package ru.bnfnal.ui.painting

import java.awt.Dimension
import java.awt.Graphics

interface Painter {
//    val plane: CartesianPlane    это очень общий класс, не факт, что нам понадобится декартова система координат (плоскость)
//    var width: Int
//    var height: Int
    var size: Dimension
    // var size: Dimension
    fun paint(g: Graphics)
}