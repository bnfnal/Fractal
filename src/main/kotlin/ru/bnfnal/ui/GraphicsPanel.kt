package ru.bnfnal.ui

import ru.bnfnal.ui.painting.Painter
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.JPanel

open class GraphicsPanel(val painters: List<Painter>): JPanel(){

    init{

        painters.forEach{
            it.size = size
        }

        addComponentListener(object : ComponentAdapter(){
            override fun componentResized(e: ComponentEvent?) {
                painters.forEach{
                    it.size = size
                }
                repaint()
            }
        })
    }

    final override fun addComponentListener(l: ComponentListener?) {
        super.addComponentListener(l)
    }

    override  fun paint(g: Graphics?){
        super.paint(g)
        g?.let{
            painters.forEach { p->
                p.paint(it)
            }
        }
    }
}

