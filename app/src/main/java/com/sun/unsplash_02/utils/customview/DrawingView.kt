package com.sun.unsplash_02.utils.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sun.unsplash_02.data.model.LinePath
import java.util.*
import kotlin.math.abs

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private lateinit var drawCanvas: Canvas
    private lateinit var path: Path
    private var touchX = 0f
    private var touchY = 0f
    private var brushSize = DEFAULT_BRUSH_SIZE
    private var opacity = DEFAULT_OPACITY
    private val drawnPaths: Stack<LinePath> = Stack<LinePath>()
    private val drawingPaint = Paint()

    init {
        setupPathAndPaint()
    }

    private fun setupPathAndPaint() {
        path = Path()
        drawingPaint.run {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = brushSize
            alpha = opacity
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).also {
            drawCanvas = Canvas(it)
        }
    }

    override fun onDraw(canvas: Canvas) {
        for (linePath in drawnPaths) {
            canvas.drawPath(linePath.drawPath, linePath.drawPaint)
        }
        canvas.drawPath(path, drawingPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart(touchX, touchY)
            MotionEvent.ACTION_MOVE -> touchMove(touchX, touchY)
            MotionEvent.ACTION_UP -> touchUp()
        }
        invalidate()
        return true
    }

    private fun touchStart(x: Float, y: Float) {
        path.run {
            reset()
            moveTo(x, y)
        }
        touchX = x
        touchY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - touchX)
        val dy = abs(y - touchY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(touchX, touchY, (x + touchX) / 2, (y + touchY) / 2)
            touchX = x
            touchY = y
        }
    }

    private fun touchUp() {
        path.lineTo(touchX, touchY)
        drawCanvas.drawPath(path, drawingPaint)
        drawnPaths.push(LinePath(path, drawingPaint))
        path = Path()
    }

    companion object {
        private const val DEFAULT_BRUSH_SIZE = 25.0f
        private const val DEFAULT_OPACITY = 255
        private const val TOUCH_TOLERANCE = 4f
    }
}
