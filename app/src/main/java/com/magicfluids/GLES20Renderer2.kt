package com.magicfluids

import android.content.*
import android.opengl.*
import android.os.*
import com.fluidsimulation.ext.*
import com.magicfluids.InputBuffer.Companion.Instance
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.*

open class GLES20Renderer2(var activity: Context?, private val nativeInterface: NativeInterface?, private val orientationSensor: OrientationSensor?, private val settings: Settings?) : GLSurfaceView.Renderer {

    private val TAG = "GLES20Renderer"
    private var screenHeight = 0
    private var screenWidth = 0
    private var ignoreNextFrameTime = false
    var input = Input()

    fun setInitialScreenSize(i: Int, i2: Int) {
        screenWidth = i
        screenHeight = i2
    }

    override fun onDrawFrame(gl10: GL10?) {
        TAG.log("onDrawFrame()")
        Instance.getCurrentInputState(input)
        for (i in 0 until input.NumEvents) {
            input.Events[i]?.let { nativeInterface?.onMotionEvent(it) }
        }
        nativeInterface?.updateApp(ignoreNextFrameTime, orientationSensor?.AccelerationX ?: 0F, orientationSensor?.AccelerationY ?: 0F, orientationSensor?.Orientation ?: 0)
    }

    override fun onSurfaceChanged(gl10: GL10, width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
        nativeInterface?.windowChanged(width, height)
        TAG.log("onSurfaceChanged")
    }

    fun checkGPUExtensions(): Boolean {
        val glGetString = GLES20.glGetString(7939)
        return glGetString.contains("GL_OES_texture_half_float_linear") and glGetString.contains("GL_EXT_color_buffer_half_float") and glGetString.contains("GL_OES_texture_half_float")
    }

    override fun onSurfaceCreated(gl10: GL10, eGLConfig: EGLConfig) {
        settings?.GPUAnimAvailable = checkGPUExtensions()
        settings?.let { nativeInterface?.updateSettings(it) }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        TAG.log(GLES20.glGetString(7939))
        if (isLWP) {
            return
        }
        TAG.log("onSurfaceCreated: Not a wallpaper. Reloading resources")
        onEGLContextCreated()
    }

    private val isLWP: Boolean
        private get() = activity == null

    fun onEGLContextCreated() {
        nativeInterface?.onGLContextRestarted()
    }

    companion object {

        fun byteToUint(b: Byte): Int {
            return if (b < 0) b + 0 else b.toInt()
        }
    }
}