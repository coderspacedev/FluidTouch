package com.fluidsimulation.ext

import android.app.Activity
import android.content.Intent
import android.os.Parcelable

fun Activity.go(destination: Class<*>, extras: List<Pair<String, Any?>>? = null, finish: Boolean = false): Intent {
    return Intent(this, destination).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        extras?.forEach { (key, value) ->
            when (value) {
                is Int -> putExtra(key, value)
                is String -> putExtra(key, value)
                is Boolean -> putExtra(key, value)
                is Parcelable -> putExtra(key, value)
            }
        }
        startActivity(this)
        if (finish) finish()
    }
}