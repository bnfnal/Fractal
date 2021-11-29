package ru.bnfnal.ui

import ru.bnfnal.ui.painting.Painter
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.JPanel

open class GraphicsPanel(val painters: List<Painter>): JPanel(){
    //class GraphicsPanel(vararg val painters: Painter): JPanel(){

    init{

        // теперь мф можем не беспокоиться об изменении высоты и ширины в MainPanel
        painters.forEach{
            it.size = size
        }

        // нельзя исп открытые методы внутри открытых классов
        addComponentListener(object : ComponentAdapter(){
            override fun componentResized(e: ComponentEvent?) {
                painters.forEach{
                    it.size = size
                }
                repaint()
            }
        })
    }

    // переопределить этот метод в классах потомках будет нельзя, он финальный
    final override fun addComponentListener(l: ComponentListener?) {
        super.addComponentListener(l)
    }

    override  fun paint(g: Graphics?){
        super.paint(g)      // супер - ключ слово, обознач базовый класс (родитель) (как this)
        g?.let{
            painters.forEach { p->
                p.paint(it)
            }
        }
    }
}

