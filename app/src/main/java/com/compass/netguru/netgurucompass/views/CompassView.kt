package com.compass.netguru.netgurucompass.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import com.compass.netguru.netgurucompass.R


class CompassView : ImageView {

    private var arrowDrawable: Drawable? = null
    private var directionDrawable: Drawable? = null

    private val destinationRect = Rect()
    private val bitMapMatrix = Matrix()

    private val destinationDirRect = Rect()
    private val bitMapDirMatrix = Matrix()

    private var canvas: Canvas? = null
    private var degree: Float = 0.0f
    private var dirDegree: Float? = null

    constructor(context: Context?) : super(context) {
        setWillNotDraw(false)
        applyCustomFont(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setWillNotDraw(false)
        applyCustomFont(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)
        applyCustomFont(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        setWillNotDraw(false)
        applyCustomFont(context, attrs)
    }

    private fun applyCustomFont(context: Context?, attrs: AttributeSet?) {
        val a = context?.obtainStyledAttributes(attrs, R.styleable.CompassView)
        arrowDrawable = a?.getDrawable(R.styleable.CompassView_arrowDrawable)
        directionDrawable = a?.getDrawable(R.styleable.CompassView_directionDrawable)
        a?.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        this.canvas = canvas
        super.onDraw(canvas)
        drawArrow()
        drawDirection()
    }

    private fun drawArrow() {
        val arrowWidth = width * 0.12
        val arrowHeight = height * 0.37

        val calculatedDegree = when (degree) {
            in 90.0f..180.0f -> 180 - degree
            in 180.0f..270.0f -> degree - 180
            in 270.0f..360.0f -> 360 - degree
            else -> degree
        }
        val arrowNewWidth = (sin(calculatedDegree) * arrowHeight + cos(calculatedDegree) * arrowWidth).toInt()
        val arrowNewHeight = (cos(calculatedDegree) * arrowHeight + sin(calculatedDegree) * arrowWidth).toInt()

        destinationRect.set(0, 0, arrowNewWidth, arrowNewHeight)
        destinationRect.offset(width / 2 - arrowNewWidth / 2, height / 2 - arrowNewHeight / 2)

        bitMapMatrix.postRotate(-degree)
        var bitmap = (arrowDrawable as BitmapDrawable).bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, bitMapMatrix, true)

        canvas?.drawBitmap(bitmap, null, destinationRect, null)
        bitMapMatrix.reset()
    }

    fun drawDirection() {
        dirDegree?.let {dirDegree ->
            val arrowSize = (width * 0.07).toInt()
            val arrowOffset = (height * 0.21).toInt()
            val radius = height / 2 - arrowOffset

            val calculatedDegree = dirDegree % 90
            val arrowNewSize = (sin(calculatedDegree) * arrowSize + cos(calculatedDegree) * arrowSize).toInt()

            val dx = width / 2 - sin(dirDegree) * radius
            val dy = height / 2 - cos(dirDegree) * radius
            destinationDirRect.set(0, 0, arrowNewSize, arrowNewSize)
            destinationDirRect.offset(dx.toInt() - arrowNewSize / 2, dy.toInt() - arrowNewSize / 2)

            bitMapDirMatrix.postRotate(-dirDegree)
            var bitmap = (directionDrawable as BitmapDrawable).bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, bitMapDirMatrix, true)

            canvas?.drawBitmap(bitmap, null, destinationDirRect, null)
            bitMapDirMatrix.reset()
        }
    }

    fun rotateArrow(degree: Int) {
        this.degree = degree.toFloat()
        invalidate()
    }

    fun rotateDirection(degree: Int) {
        dirDegree = degree.toFloat()
    }

    private fun cos(degree: Float): Double {
        return Math.cos(Math.toRadians(degree.toDouble()))
    }

    private fun sin(degree: Float): Double {
        return Math.sin(Math.toRadians(degree.toDouble()))
    }
}