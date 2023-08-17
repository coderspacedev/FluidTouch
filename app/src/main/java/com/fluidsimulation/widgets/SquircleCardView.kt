package com.fluidsimulation.widgets

import android.annotation.*
import android.content.*
import android.graphics.*
import android.util.*
import com.google.android.material.card.*

class SquircleCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = cardBackgroundColor.defaultColor
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val cornerRadius = radius
        val padding = cardElevation.toInt()

        val bounds = RectF(
                padding.toFloat(),
                padding.toFloat(),
                (width - padding).toFloat(),
                (height - padding).toFloat()
        )

        val squircleRadius = calculateSquircleRadius(bounds.width(), bounds.height(), cornerRadius)
        path.reset()

        path.moveTo(bounds.left, bounds.top + squircleRadius)
        path.quadTo(
                bounds.left, bounds.top,
                bounds.left + squircleRadius, bounds.top
        )
        path.lineTo(bounds.right - squircleRadius, bounds.top)
        path.quadTo(
                bounds.right, bounds.top,
                bounds.right, bounds.top + squircleRadius
        )
        path.lineTo(bounds.right, bounds.bottom - squircleRadius)
        path.quadTo(
                bounds.right, bounds.bottom,
                bounds.right - squircleRadius, bounds.bottom
        )
        path.lineTo(bounds.left + squircleRadius, bounds.bottom)
        path.quadTo(
                bounds.left, bounds.bottom,
                bounds.left, bounds.bottom - squircleRadius
        )
        path.close()

        canvas.clipPath(path)
        super.onDraw(canvas)
    }

    private fun calculateSquircleRadius(width: Float, height: Float, cornerRadius: Float): Float {
        val size = width.coerceAtMost(height)
        return cornerRadius.coerceAtMost(size / 2f)
    }
}
