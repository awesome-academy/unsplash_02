package com.sun.unsplash_02.utils.customview

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs
import kotlin.math.min

class ZoomImageView @JvmOverloads constructor(
    contextImage: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(contextImage, attrs, defStyleAttr), GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private var matrixImage: Matrix
    private var mode = NONE
    private var last: PointF
    private var start: PointF
    private var matrixArray: FloatArray
    private var viewWidth = 0f
    private var viewHeight = 0f
    private var origWidth = 0f
    private var origHeight = 0f
    private var oldMeasureWidth = 0f
    private var oldMeasureHeight = 0f
    private var minScale = 1f
    private var maxScale = 3f
    private var saveScale = 1f
    private var scaleGestureDetector: ScaleGestureDetector
    private var gestureDetector: GestureDetector

    init {
        super.setClickable(true)
        last = PointF()
        start = PointF()
        GestureDetector(context, this).apply {
            setOnDoubleTapListener(this@ZoomImageView)
            gestureDetector = this
        }
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        matrixImage = Matrix()
        matrixArray = FloatArray(9)
        imageMatrix = matrixImage
        scaleType = ScaleType.MATRIX
        setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
            val currentPointF = PointF(event.x, event.y)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    last.set(currentPointF)
                    start.set(last)
                    mode = DRAG
                }
                MotionEvent.ACTION_MOVE -> {
                    if (mode == DRAG) {
                        val deltaX = currentPointF.x - last.x
                        val deltaY = currentPointF.y - last.y
                        val fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale)
                        val fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale)
                        matrixImage.postTranslate(fixTransX, fixTransY)
                        fixTrans()
                        last.set(currentPointF.x, currentPointF.y)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    mode = NONE
                    val xDiff = abs(currentPointF.x - start.x)
                    val yDiff = abs(currentPointF.y - start.y)
                    if (xDiff < CLICK && yDiff < CLICK) {
                        performClick()
                    }
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    mode = NONE
                }
                else -> Unit
            }
            imageMatrix = matrixImage
            invalidate()
            true
        }
    }

    override fun onDown(e: MotionEvent?) = false

    override fun onShowPress(e: MotionEvent?) = Unit

    override fun onSingleTapUp(e: MotionEvent?) = false

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ) = false

    override fun onLongPress(e: MotionEvent?) = Unit

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) = false

    override fun onSingleTapConfirmed(e: MotionEvent?) = false

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        val origScale = saveScale
        val scaleFactor: Float
        if (saveScale == maxScale) {
            saveScale = minScale
            scaleFactor = minScale / origScale
        } else {
            saveScale = maxScale
            scaleFactor = maxScale / origScale
        }
        matrixImage.postScale(scaleFactor, scaleFactor, viewWidth / 2, viewHeight / 2)
        fixTrans()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?) = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        viewHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        if (oldMeasureHeight == viewHeight && oldMeasureHeight == viewWidth
            || viewHeight == 0f || viewWidth == 0f
        ) return
        oldMeasureHeight = viewHeight
        oldMeasureWidth = viewWidth
        if (saveScale == 1f) {
            drawable?.let {
                if (it.intrinsicWidth == 0 || it.intrinsicHeight == 0) {
                    return
                }
                val scale: Float
                val bmWidth = it.intrinsicWidth
                val bmHeight = it.intrinsicHeight
                val scaleX = viewWidth / bmWidth
                val scaleY = viewHeight / bmHeight
                scale = min(scaleX, scaleY)
                matrixImage.setScale(scale, scale)
                var redundantYSpace = (viewHeight - scale * bmHeight.toFloat())
                var redundantXSpace = (viewWidth - scale * bmWidth.toFloat())
                redundantYSpace /= 2.toFloat()
                redundantXSpace /= 2.toFloat()
                matrixImage.postTranslate(redundantXSpace, redundantYSpace)
                origWidth = viewWidth - 2 * redundantXSpace
                origHeight = viewHeight - 2 * redundantYSpace
                imageMatrix = matrixImage
            }
            fixTrans()
        }
    }

    private fun fixTrans() {
        matrixImage.getValues(matrixArray)
        val transX = matrixArray[Matrix.MTRANS_X]
        val transY = matrixArray[Matrix.MTRANS_Y]
        val fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale)
        val fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale)
        if (fixTransX != 0f || fixTransY != 0f) {
            matrixImage.postTranslate(fixTransX, fixTransY)
        }
    }

    private fun getFixTrans(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float
        if (contentSize <= viewSize) {
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }
        if (trans < minTrans) return -trans + minTrans
        return if (trans > maxTrans) -trans + maxTrans else 0f
    }

    private fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float {
        if (contentSize <= viewSize) {
            return 0f
        }
        return delta
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var scaleFactor = detector.scaleFactor
            val origScale = saveScale
            saveScale *= scaleFactor
            when {
                saveScale > maxScale -> {
                    saveScale = maxScale
                    scaleFactor = maxScale / origScale
                }
                saveScale < minScale -> {
                    saveScale = minScale
                    scaleFactor = minScale / origScale
                }
                else -> Unit
            }
            when {
                origWidth * saveScale <= viewWidth
                        || origHeight * saveScale <= viewHeight -> {
                    matrixImage.postScale(
                        scaleFactor, scaleFactor, viewWidth / 2, viewHeight / 2
                    )
                }
                else -> {
                    matrixImage.postScale(
                        scaleFactor, scaleFactor,
                        detector.focusX, detector.focusY
                    )
                }
            }
            fixTrans()
            return true
        }
    }

    companion object {
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 2
        private const val CLICK = 3
    }
}
