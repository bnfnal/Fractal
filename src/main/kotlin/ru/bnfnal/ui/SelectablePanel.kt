package ru.bnfnal.ui

import ru.bnfnal.ui.painting.Painter
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.*
import java.lang.Integer.min
import java.lang.Math.abs

class SelectablePanel(vararg painters: Painter): GraphicsPanel(painters.toMutableList()) {

    private var pt1: Point? = null
    private var pt2: Point? = null

    var rect: Rectangle? = null
        get(){
            pt1?.let { p1 ->
                pt2?.let { p2 ->
                    return Rectangle(
                        min(p1.x, p2.x),
                        min(p1.y, p2.y),
                        abs(p2.x - p1.x),
                        abs(p2.y - p1.y))
                }
            }
            return null
        }

    private  var selectListeners: MutableList<(Rectangle) -> Unit> = mutableListOf()

    fun addSelectListener(l: (Rectangle) -> Unit){
        selectListeners.add(l)
    }
    fun deleteSelectListener(l: (Rectangle) -> Unit){
        selectListeners.remove(l)
    }

    init{
        addMouseListener(object: MouseAdapter(){
            override fun mousePressed(e: MouseEvent?) {
                super.mousePressed(e)
                with(graphics){
                    setXORMode(Color.WHITE)
                    fillRect(2*width, 0, 1, 1)
                    setPaintMode()
                }
                pt1 = e?.point
            }

            override fun mouseReleased(e: MouseEvent?) {
                super.mouseReleased(e)
                drawSelectRect()
                rect?.let{ r->
                    selectListeners.forEach { it(r) }
                }
                pt1 = null
                pt2 = null
            }
        })


        addMouseMotionListener(object : MouseMotionAdapter(){
            override fun mouseDragged(e: MouseEvent?) {
                super.mouseDragged(e)
                repeat(2){
                    drawSelectRect()
                    pt2 = e?.point
                }
            }
        })

    }

    private fun drawSelectRect(){
        with(graphics) {
            setXORMode(java.awt.Color.WHITE)
            rect?.let { r ->
                drawRect(r.x, r.y, r.width, r.height)
                setPaintMode()
            }
        }
    }

}

