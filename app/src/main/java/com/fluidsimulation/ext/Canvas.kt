package com.fluidsimulation.ext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.os.Debug
import android.text.*
import androidx.annotation.RequiresApi
import androidx.core.graphics.withMatrix
import androidx.core.graphics.withTranslation
import androidx.core.util.lruCache
import kotlin.math.ceil
import kotlin.math.sqrt


fun Canvas.drawRectWithOffset(rect: Rect, offset: Float, paint: Paint) =
    drawRect(
        rect.left - offset,
        rect.top - offset,
        rect.right + offset,
        rect.bottom + offset,
        paint
    )

fun getFitTextSize(paint: TextPaint, width: Float, text: String?): Float {
    val nowWidth = paint.measureText(text)
    return width / nowWidth * paint.textSize
}

fun calculateWidthFromFontSize(testString: String, currentSize: Float): Int {
    val bounds = Rect()
    val paint = Paint()
    paint.textSize = currentSize
    paint.getTextBounds(testString, 0, testString.length, bounds)
    return ceil(bounds.width().toDouble()).toInt()
}

fun calculateHeightFromFontSize(testString: String, currentSize: Float): Int {
    val bounds = Rect()
    val paint = Paint()
    paint.textSize = currentSize
    paint.getTextBounds(testString, 0, testString.length, bounds)
    return ceil(bounds.height().toDouble()).toInt()
}

@SuppressLint("WrongConstant")
@RequiresApi(Build.VERSION_CODES.O)
fun Canvas.drawMultilineText(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    x: Float,
    y: Float,
    start: Int = 0,
    end: Int = text.length,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    textDir: TextDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_LTR,
    spacingMult: Float = 1f,
    spacingAdd: Float = 0f,
    includePad: Boolean = true,
    ellipsizedWidth: Int = width,
    ellipsize: TextUtils.TruncateAt? = null,
    maxLines: Int = Int.MAX_VALUE,
    breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
    hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE,
    justificationMode: Int = Layout.JUSTIFICATION_MODE_NONE
) {

    val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-$textDir-" +
            "$spacingMult-$spacingAdd-$includePad-$ellipsizedWidth-$ellipsize-" +
            "$maxLines-$breakStrategy-$hyphenationFrequency-$justificationMode"

    val staticLayout = StaticLayoutCache[cacheKey] ?: StaticLayout.Builder.obtain(
        text,
        start,
        end,
        textPaint,
        width
    )
        .setAlignment(alignment)
        .setTextDirection(textDir)
        .setLineSpacing(spacingAdd, spacingMult)
        .setIncludePad(includePad)
        .setEllipsizedWidth(ellipsizedWidth)
        .setEllipsize(ellipsize)
        .setMaxLines(maxLines)
        .setBreakStrategy(breakStrategy)
        .setHyphenationFrequency(hyphenationFrequency)
        .setJustificationMode(justificationMode)
        .build().apply { StaticLayoutCache[cacheKey] = this }

    staticLayout.draw(this, x, y, 0F)
}

@SuppressLint("WrongConstant")
fun Canvas.drawMultilineText(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    matrix: Matrix? = null,
    x: Float,
    rotation: Float,
    y: Float,
    start: Int = 0,
    end: Int = text.length,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    textDir: TextDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_LTR,
    spacingMult: Float = 1f,
    spacingAdd: Float = 0f,
    includePad: Boolean = true,
    ellipsizedWidth: Int = width,
    ellipsize: TextUtils.TruncateAt? = null,
    maxLines: Int = Int.MAX_VALUE,
    breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
    hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE
) {

    val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-$textDir-" +
            "$spacingMult-$spacingAdd-$includePad-$ellipsizedWidth-$ellipsize-" +
            "$maxLines-$breakStrategy-$hyphenationFrequency"

    val staticLayout = StaticLayoutCache[cacheKey] ?: StaticLayout.Builder.obtain(
        text,
        start,
        end,
        textPaint,
        width
    )
        .setAlignment(alignment)
        .setTextDirection(textDir).setLineSpacing(spacingAdd, spacingMult).setIncludePad(includePad).setEllipsizedWidth(ellipsizedWidth)
        .setEllipsize(ellipsize).setMaxLines(maxLines).setBreakStrategy(breakStrategy).setHyphenationFrequency(hyphenationFrequency).build()
        .apply { StaticLayoutCache[cacheKey] = this }


    matrix?.let {
        staticLayout.drawText(this, matrix = it)
    }
}

fun Canvas.drawMultilineText(
    text: CharSequence,
    textPaint: TextPaint,
    width: Int,
    x: Float,
    y: Float,
    start: Int = 0,
    end: Int = text.length,
    alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    spacingMult: Float = 1f,
    spacingAdd: Float = 0f,
    includePad: Boolean = true,
    ellipsizedWidth: Int = width,
    ellipsize: TextUtils.TruncateAt? = null
) {

    val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-" +
            "$spacingMult-$spacingAdd-$includePad-$ellipsizedWidth-$ellipsize"

    val staticLayout = StaticLayoutCache[cacheKey] ?: StaticLayout.Builder.obtain(
        text,
        start,
        end,
        textPaint,
        width
    )
        .setAlignment(alignment)
        .setLineSpacing(spacingAdd, spacingMult)
        .setIncludePad(includePad)
        .setEllipsizedWidth(ellipsizedWidth)
        .setEllipsize(ellipsize)
        .build()

    staticLayout.draw(this, x, y, 0F)
}

private fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float, rotation: Float) {
    canvas.withTranslation(x, y) {
        rotate(rotation)
        draw(this)
    }
}

private fun StaticLayout.drawText(
    canvas: Canvas,
    matrix: Matrix
) {
    canvas.withMatrix(matrix) {
        draw(this)
    }
}

private object StaticLayoutCache {

    private const val MAX_SIZE = 1 // Arbitrary max number of cached items
    private val cache = lruCache<String, StaticLayout>(MAX_SIZE)

    operator fun set(key: String, staticLayout: StaticLayout) {
        cache.put(key, staticLayout)
    }

    operator fun get(key: String): StaticLayout? {
        return cache[key]
    }
}

private const val limitDivider = 30.0f
private const val limitDividerGinger = 160.0f

fun Context.maxSizeForSave(upperLimit: Float): Int {
    var divider: Float = limitDivider
    val maxSize = sqrt(getLeftSizeOfMemory() / divider.toDouble()).toInt()
    return if (maxSize > 0) {
        maxSize.toFloat().coerceAtMost(upperLimit).toInt()
    } else upperLimit.toInt()
}

fun getLeftSizeOfMemory(): Double {
    val totalSize = java.lang.Double.valueOf(Runtime.getRuntime().maxMemory().toDouble()).toDouble()
    val heapAllocated =
        java.lang.Double.valueOf(Runtime.getRuntime().totalMemory().toDouble()).toDouble()
    return totalSize - (heapAllocated - java.lang.Double.valueOf(
        Runtime.getRuntime().freeMemory().toDouble()
    ).toDouble()) - java.lang.Double.valueOf(Debug.getNativeHeapAllocatedSize().toDouble())
        .toDouble()
}