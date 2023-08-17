package com.fluidsimulation.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast

fun View.beInvisibleIf(beInvisible: Boolean) = if (beInvisible) beInvisible() else beVisible()

fun View.beVisibleIf(beVisible: Boolean) = if (beVisible) beVisible() else beGone()

fun View.beGoneIf(beGone: Boolean) = beVisibleIf(!beGone)

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View?.removeSelf() {
    this ?: return
    val parentView = parent as? ViewGroup ?: return
    parentView.removeView(this)
}

fun View.beGone() {
    visibility = View.GONE
}

fun View.beEnableIf(isEnable: Boolean) = if (isEnable) enable() else disable()

fun View.enable() {
    isEnabled = true
    alpha = 1.0F
}

fun View.disable() {
    isEnabled = false
    alpha = 0.5F
}

fun View.updateViewMargins(leftMargin: Int = 0, topMargin: Int = 0, rightMargin: Int = 0, bottomMargin: Int = 0) {
    val params = layoutParams as LinearLayout.LayoutParams
    params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
    layoutParams = params
}

//fun Context.showToast(message: String) {
//    val inflater = LayoutInflater.from(this)
//    val binding = LayoutToastBinding.inflate(inflater)
//    binding.toastMessage.text = "$message"
//    val toast = Toast(this)
//    toast.duration = Toast.LENGTH_SHORT
//    toast.view = binding.root
//    toast.show()
//}