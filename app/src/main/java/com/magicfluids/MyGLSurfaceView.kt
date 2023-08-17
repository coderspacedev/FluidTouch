package com.magicfluids

import android.content.*
import android.opengl.*
import android.util.*
import android.view.*

class MyGLSurfaceView : GLSurfaceView {
    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context?) : super(context)

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        InputBuffer.Instance.addEvent(motionEvent)
        return true
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }
}