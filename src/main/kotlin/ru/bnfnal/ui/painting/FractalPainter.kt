package ru.bnfnal.ui.painting

import org.kotlinmath.Complex
import org.kotlinmath.complex
import ru.bnfnal.math.fractals.Fractal
import ru.bnfnal.math.fractals.Mandelbrot
import java.awt.*
import java.awt.image.BufferedImage
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

// отрисовка фрактала
// нужно пробежаться по всем пикселям панели (по всем точкам плоскости)
// и проверить принадлежат ли они множеству Мандельброта
// если да - закрасить черным, если нет - белым
// либо линия длиной 1 пиксель, либо прямоугольник со сторонами 1, 1

class FractalPainter(private val plane: CartesianPlane, val fractal: Fractal) : Painter {

    var colorizer: (Double) -> Color = ::getDefaultColor
    private var pool: ExecutorService? = null

//    override var width:Int
//        get() = plane.width
//        set(value){
//            plane.width = value
//        }
//
//    override var height:Int
//        get() = plane.height
//        set(value){
//            plane.height = value
//        }

    override var size: Dimension
        get() = plane.pixelSize
        set(value) {
            plane.pixelSize = value
        }

//    override fun paint(g: Graphics) {
//
//        if (plane.width == 0 || plane.height == 0) return
//        // поток автоматически запускается при вызове функции
//        // но просто так все это работать не будет, появится белое окошко
//        // тк после выхода из функции лок перем g удалится и мы ничего не нарисуем
//        // мы должны дорисовать фрактал
//        // создадим несколько потоков
//        val threadCount = 6   // равно количеству логических процессоров системы( в диспетчере задач)
//        // конструктор списка из 6 эл и прин объект класса поток, с промощью функции thread
//        List(threadCount){
//            thread {
//                with(g as Graphics2D) {
//                    stroke = BasicStroke(3F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
//                    var rh = mapOf(
//                        RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,  // сглаживание - включено, на границах цвет по-бледнее
//                        RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
//                        RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY, // помедленнее, но зато покачественнее
//                        RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE
//                    )
//
//                    // все потоки одноврем работают с g, они все меняют его цвет и рисуют прямоугольник последним из измененных цветов
//                    // измет цвета и рисование точки должны происходить у одного потока подряд, остальные должны приостановиться
//                    // это синхрон блок и оьщий обЪект ссылочного типа g
//                    with(plane) {
//                        val stripWidth = width/threadCount
//                        val start = it * stripWidth
//                        val end = (it + 1) * stripWidth - 1 + if ((it + 1) == threadCount) width % threadCount else 0
//
//                        // класс для работы с двойной буфферизацией в Котлин
//                        val img = BufferedImage(end - start + 1, height, BufferedImage.TYPE_INT_RGB)
//                        val ig = img.graphics
//
//                        for (i in start..end) {
//                            for (j in 0..height) {
//                                val fc = fractal.isInSet(complex(xScr2Crt(i), yScr2Crt(j)))
////                                synchronized(g) {
////                                    // внутри этого блока нет многопоточности, он должен быть как можно короче
////                                    //color = colorizer(fractal.isInSet(complex(xScr2Crt(i), yScr2Crt(j))))
////                                    color = colorizer(fc)
////                                    //fillRect(xCrt2Scr(xScr2Crt(i)), yCrt2Scr(yScr2Crt(j)), 1, 1)
////                                    fillRect(i, j, 1, 1)
////                                }
//                                ig.color = colorizer(fc)
//                                ig.fillRect(i - start, j, 1, 1)
//                            }
//                        }
//                        // чтобы программа работала быстрее, каждый поток будет рисовать свою картинку,
//                        // а когда все картинки будут готовы, мы просто обЪединим их всех на панели
//                        g.drawImage(img, start, 0, null)
//                        // чем меньше точек множества внутри полосы, тем быстрее мы нарисуем полоски,
//                        // тк пройдем не все итерации цикла на проверку включения во множество
//                        // они закончат свою работу
//                        // нужно чтобы если один из потоков закончил свою работу, он начал помогать остальным
//                    }
//                }
//            }
//        }.forEach{ it.join() }   // притормаживает работу основного потока, // пока мы не нарисуем всю картинку, пока все остальные наши потоки не завершат работу
//    }

    // вместо того, чтобы самим создавать потоки, создадим сразу пул потоков
    // белый экран, тк у пула нет метода join, кот приостан работу осн потока
    override fun paint(g: Graphics) {  // сначала все рисуем в памяти и толдько потом выводим всю картинку

        if (plane.width == 0 || plane.height == 0) return

        val threadCount = 6
        val poolTaskCount = threadCount * 4   // количество задач больше количества потоков
        if (pool?.isShutdown == false)  // если вовремя отрисовки, мы изменяем размер окна, мы останавливаем старый пул и запускаем новый
            pool?.shutdown()
        pool = Executors.newFixedThreadPool(threadCount)
        val stripWidth = plane.width / poolTaskCount

        List(poolTaskCount) {      // список будующих картинок
            // result  имеет тип Future<BufferedImage>, обобщ класс, говорит о том, в ы будущем это будет картинка
            pool?.submit(Callable {
                // вызываемый типданных, создали объект интерфейса Callable и он реализуется прямовнутри{}
                // раньше лямбда-выражение ничего не возвращала, теперь будем возвращать img, вместо того, чтобы сразу ее рисовать
                with(plane) {
                    val start = it * stripWidth
                    val end = (it + 1) * stripWidth - 1 + if ((it + 1) == poolTaskCount) width % poolTaskCount else 0
                    val img = BufferedImage(end - start + 1, height, BufferedImage.TYPE_INT_RGB)
                    val ig = img.graphics

                    for (i in start..end) {
                        for (j in 0..height) {
                            val fc = fractal.isInSet(complex(xScr2Crt(i), yScr2Crt(j)))
                            ig.color = colorizer(fc)
                            ig.fillRect(i - start, j, 1, 1)
                        }
                    }
                    img
                }
            })
            // если картинка еще не создана, он будет тормозить работу основного подпроцессора
        }.forEachIndexed { i, v -> g.drawImage(v?.get(), i * stripWidth, 0, null) }
    }

    fun getDefaultColor(x:Double) =
        if ( x == 1.0) Color.BLACK else Color.WHITE


}