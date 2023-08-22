package com.magicfluids

import com.magicfluids.listeners.*
import javax.microedition.khronos.egl.*

class DefaultWindowSurfaceFactory : EGLWindowSurfaceFactory {

    override fun createWindowSurface(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLConfig: EGLConfig?, obj: Any?): EGLSurface? {
        var eGLSurface: EGLSurface? = null
        while (eGLSurface == null) {
            try {
                eGLSurface = egl10?.eglCreateWindowSurface(eGLDisplay, eGLConfig, obj, null)
                if (eGLSurface == null) {
                    try {
                        Thread.sleep(10L)
                    } catch (unused: InterruptedException) {
                    }
                }
            } catch (th: Throwable) {
                if (eGLSurface == null) {
                    try {
                        Thread.sleep(10L)
                    } catch (unused2: InterruptedException) {
                    }
                }
                throw th
            }
        }
        return eGLSurface
    }

    override fun destroySurface(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLSurface: EGLSurface?) {
        egl10?.eglDestroySurface(eGLDisplay, eGLSurface)
    }
}