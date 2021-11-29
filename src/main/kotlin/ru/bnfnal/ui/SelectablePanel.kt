package ru.bnfnal.ui

import ru.bnfnal.ui.painting.Painter
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.*
import java.lang.Integer.min
import java.lang.Math.abs

//class SelectablePanel(vararg  painters: Painter): GraphicsPanel(*painters) {
//}
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

    // событие - переменная функц типа
    // если в разных классах мы обратились к одному и тому же обработчику, в первом классе оно слетит, значение е измеится, сбросится
    // для этого добавим список
    //var selectListener: ((Rectangle) -> Unit)? = null
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
                // если при запуске программы возникает какой-то прямоугольничек
                // это не наш баг
                // нарисовали что-то за границей видимости
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
//                pt1?.let{ p1->
//                    pt2?.let{ p2->
//                        //selectListener?.invoke(r)
//                        selectListeners.forEach { it(r) }
//                    }
//                }
                rect?.let{ r->
                    //selectListener?.invoke(r)
                    selectListeners.forEach { it(r) }
                }
                pt1 = null
                pt2 = null
                // при отпускании клавиши на панели должен отрисоваться выделенный кусок графика
            }
        })


        addMouseMotionListener(object : MouseMotionAdapter(){
            override fun mouseDragged(e: MouseEvent?) {
                super.mouseDragged(e)
                repeat(2){
                    drawSelectRect()
                    pt2 = e?.point
                }
                // это непосредственный графикс панели, а g - графикс двойной буферизации

//                var a: Point? = null
//                if (a != null){
//                    println(a.x)
//                }
                // ошибка, тк возможно в програмее несколько потоков, кот могут работать одновременно с этим свойством
//                if (pt1 != null) {
//                    graphics.drawRect(pt1.x, pt1.y)
//                }

//                with(graphics){
//                    setXORMode(Color.WHITE) // исключ или ( отрицание эквив)
//                // копирует все переменные и работает только с копиями
//                    pt1?.let{ p1 ->
//                        repeat(2){
//                            pt2?.let { p2 ->
//                                val w = Math.abs(p2.x - p1.x)
//                                val h = Math.abs(p2.y - p1.y)
//                                drawRect(p1.x, p1.y, w, h)
//                            }
//                            pt2 = e?.point
//                        }
//                    }
//                    setPaintMode()  // возвр режим рисования в стандартный режим
//                    // по белому рисуем черным, по черному белый
//                    // если мы нарисуем один и тот же прямоугольник 2 раза, он останется неизменным
//                    // ЭТО БУДЕТ РАБОТАТЬ И ДЛЯ ЦВЕТНЫХ ИЗОБРАЖЕНИЙ ТОЖЕ
//                }
//                // наш комп не сможет быстро перестраивать фрактал
//                // лучше отрисовывать фрактал прямо на картинке


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

