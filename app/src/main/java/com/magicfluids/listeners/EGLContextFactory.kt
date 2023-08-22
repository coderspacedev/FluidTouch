package com.magicfluids.listeners

import javax.microedition.khronos.egl.*

interface EGLContextFactory {

    fun createContext(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLConfig: EGLConfig?, i: Int): EGLContext?
    fun destroyContext(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLContext: EGLContext?)
}