package com.fluidsimulation.ext

import android.annotation.SuppressLint
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator

@SuppressLint("ClickableViewAccessibility")
fun RecyclerView.applyDefault() {
    itemAnimator = FadeInUpAnimator(OvershootInterpolator(1f))
    scheduleLayoutAnimation()
    setItemViewCacheSize(100)
    invalidateItemDecorations()
}