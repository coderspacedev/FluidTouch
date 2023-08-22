package com.magicfluids

import android.util.*
import android.view.*
import com.magicfluids.listeners.*
import javax.microedition.khronos.egl.*
import javax.microedition.khronos.opengles.*

class EglHelper(private val eglConfigChooser: EGLConfigChooser?, private val eglContextFactory: EGLContextFactory?, private val eglWindowSurfaceFactory: EGLWindowSurfaceFactory?, private val glWrapper: GLWrapper?, private val eglContextClientVersion: Int) {

    private var egl10: EGL10? = null
    @JvmField
    var eglConfig: EGLConfig? = null
    private var eglContext: EGLContext? = null
    private var eglDisplay: EGLDisplay? = null
    private var eglSurface: EGLSurface? = null
    fun start(): Boolean {
        val z: Boolean
        if (egl10 == null) {
            egl10 = EGLContext.getEGL() as EGL10
        }
        if (eglDisplay == null) {
            eglDisplay = egl10!!.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        }
        if (eglConfig == null) {
            egl10?.eglInitialize(eglDisplay, IntArray(2))
            eglConfig = eglConfigChooser?.chooseConfig(egl10, eglDisplay)
        }
        if (eglContext == null) {
            Log.d("EglHelper", "creating new context")
            eglContext = eglContextFactory?.createContext(egl10, eglDisplay, eglConfig, eglContextClientVersion)
            if (eglContext == null || eglContext === EGL10.EGL_NO_CONTEXT) {
                throw RuntimeException("createContext failed")
            }
            z = true
        } else {
            z = false
        }
        eglSurface = null
        return z
    }

    fun createSurface(surfaceHolder: SurfaceHolder?): GL? {
        if (eglSurface != null && eglSurface !== EGL10.EGL_NO_SURFACE) {
            egl10?.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)
            eglWindowSurfaceFactory?.destroySurface(egl10, eglDisplay, eglSurface)
        }
        eglSurface = eglWindowSurfaceFactory?.createWindowSurface(egl10, eglDisplay, eglConfig, surfaceHolder)
        if (eglSurface == null) {
            throw RuntimeException("createWindowSurface failed: mEglSurface is null")
        }
        if (eglSurface == EGL10.EGL_NO_SURFACE) {
            eglSurface = eglWindowSurfaceFactory?.createWindowSurface(egl10, eglDisplay, eglConfig, surfaceHolder)
            if (eglSurface == EGL10.EGL_NO_SURFACE) {
                throw RuntimeException("createWindowSurface failed: mEglSurface is EGL10.EGL_NO_SURFACE")
            }
        }
        if (egl10?.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)?.not() == true) {
            throw RuntimeException("eglMakeCurrent failed.")
        }
        return if (glWrapper != null) glWrapper.wrap(eglContext?.gl) else eglContext?.gl
    }

    fun swap(): Boolean {
        egl10?.eglSwapBuffers(eglDisplay, eglSurface)
        return egl10?.eglGetError() != 12302
    }

    fun destroySurface() {
        if (eglSurface == null || eglSurface === EGL10.EGL_NO_SURFACE) {
            return
        }
        egl10?.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)
        eglWindowSurfaceFactory?.destroySurface(egl10, eglDisplay, eglSurface)
        eglSurface = null
    }

    fun finish() {
        Log.e("GLThread", "finish()")
        if (eglContext != null) {
            eglContextFactory?.destroyContext(egl10, eglDisplay, eglContext)
            eglContext = null
        }
        if (eglDisplay != null) {
            egl10?.eglTerminate(eglDisplay)
            eglDisplay = null
        }
    }
}