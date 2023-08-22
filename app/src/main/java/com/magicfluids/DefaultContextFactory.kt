package com.magicfluids

import com.magicfluids.listeners.*
import javax.microedition.khronos.egl.*

class DefaultContextFactory : EGLContextFactory {

    private val EGL_CONTEXT_CLIENT_VERSION = 12440
    override fun createContext(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLConfig: EGLConfig?, i: Int): EGLContext? {
        var iArr: IntArray? = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, i, 12344)
        val eGLContext = EGL10.EGL_NO_CONTEXT
        if (i == 0) {
            iArr = null
        }
        return egl10!!.eglCreateContext(eGLDisplay, eGLConfig, eGLContext, iArr)
    }

    override fun destroyContext(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLContext: EGLContext?) {
        egl10!!.eglDestroyContext(eGLDisplay, eGLContext)
    }
}