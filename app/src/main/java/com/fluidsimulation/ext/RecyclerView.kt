package com.fluidsimulation.ext

import android.annotation.*
import android.view.animation.*
import androidx.recyclerview.widget.*
import jp.wasabeef.recyclerview.animators.*

@SuppressLint("ClickableViewAccessibility")
fun RecyclerView.applyDefault() {
    itemAnimator = FadeInUpAnimator(OvershootInterpolator(1f))
    scheduleLayoutAnimation()
    setItemViewCacheSize(100)
    invalidateItemDecorations()
}