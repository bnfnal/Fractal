package ru.bnfnal.ui.painting.fractals

import java.awt.Color

// когда все оттенки одинаковые, мы получаем разные оттенки серого
// чтобы получить красивые оттенки нужно исп нелинейность

fun bwFractal(x:Double) =
    Color(
        (1-x.coerceIn(0.0, 1.0)).toFloat(),   // red
        (1-x.coerceIn(0.0, 1.0)).toFloat(),  // green
        (1-x.coerceIn(0.0, 1.0)).toFloat()    // blue
    )

fun pinkFractal(x:Double) : Color {
    if (x == 1.0) return Color.BLACK
    else {
        return Color(
            Math.abs(Math.cos(6*x)).toFloat(),   // red
            Math.abs(Math.cos(12*x)).toFloat(),  // green
            Math.abs(Math.cos(7 - 7*x)).toFloat()    // blue
        )
        // когда все оттенки одинаковые, мы получаем разные оттенки серого
        // чтобы получить красивые оттенки нужно исп нелинейность
    }
}

fun yellowFractal(x:Double) : Color {
    if (x == 1.0) return Color.BLACK
    else {
        return Color(
            Math.abs(Math.cos(7 - 7*x)).toFloat(),   // red
            Math.abs(Math.cos(6*x)).toFloat(),  // green
            Math.abs(Math.cos(12*x)).toFloat()    // blue
        )
        // когда все оттенки одинаковые, мы получаем разные оттенки серого
        // чтобы получить красивые оттенки нужно исп нелинейность
    }
}

fun greenFractal(x:Double) : Color {
    if (x == 1.0) return Color.BLACK
    else {
        return Color(
            Math.abs(Math.cos(12*x)).toFloat(),   // red
            Math.abs(Math.cos(7 - 7*x)).toFloat(),  // green
            Math.abs(Math.cos(10*x)).toFloat()    // blue
        )
        // когда все оттенки одинаковые, мы получаем разные оттенки серого
        // чтобы получить красивые оттенки нужно исп нелинейность
    }
}

fun blueFractal(x:Double) : Color {
    if (x == 1.0) return Color.BLACK
    else {
        return Color(
            Math.abs(Math.cos(9*x)).toFloat(),   // red
            Math.abs(Math.cos(2 - 2*x)).toFloat(),  // green
            Math.abs(Math.cos(6*x)).toFloat()    // blue
        )
        // когда все оттенки одинаковые, мы получаем разные оттенки серого
        // чтобы получить красивые оттенки нужно исп нелинейность
    }
}