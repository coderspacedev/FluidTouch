package com.magicfluids.listeners

import javax.microedition.khronos.egl.*

interface EGLWindowSurfaceFactory {

    fun createWindowSurface(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLConfig: EGLConfig?, obj: Any?): EGLSurface?
    fun destroySurface(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLSurface: EGLSurface?)
}