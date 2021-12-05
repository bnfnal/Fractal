package ru.bnfnal.ui

import ru.bnfnal.math.fractals.Mandelbrot
import ru.bnfnal.ui.painting.CartesianPlane
import ru.bnfnal.ui.painting.FractalPainter
import ru.bnfnal.ui.painting.Painter
import ru.bnfnal.ui.painting.fractals.*
import java.awt.Color
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.GroupLayout
import javax.swing.JFrame
import kotlin.random.Random

class MainFrame: JFrame() {

    val minSz = Dimension(600, 600)

    private val fractalPanel: SelectablePanel
    private val plane: CartesianPlane
    private val painters = mutableListOf<Painter>()

    init{
        minimumSize = minSz
        defaultCloseOperation = EXIT_ON_CLOSE

        setTitle("Можество Мандельброта")

        plane = CartesianPlane( -2.0, 1.0, -1.0, 1.0)

        val colors = listOf(::bwFractal, ::pinkFractal, ::yellowFractal, ::greenFractal, ::blueFractal)

        fractalPanel = SelectablePanel(
            FractalPainter(plane, Mandelbrot).apply {
            colorizer = colors[Random.nextInt(colors.size)]
            }
        ).apply {
            background = Color.WHITE
            addSelectListener { r ->
                with(plane){
                    xSegment = Pair(xScr2Crt(r.x), xScr2Crt(r.x + r.width))
                    ySegment = Pair(yScr2Crt(r.y), yScr2Crt(r.y + r.height))
                }
                repaint()
            }
        }


        layout = GroupLayout(contentPane).apply {
            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addComponent(fractalPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addGap(4)
            )


            setVerticalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addComponent(fractalPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addGap(4)
            )
        }

    }

}

