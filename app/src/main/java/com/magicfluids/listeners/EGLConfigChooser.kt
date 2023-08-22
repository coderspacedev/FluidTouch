package com.magicfluids.listeners

import javax.microedition.khronos.egl.*

interface EGLConfigChooser {

    fun chooseConfig(egl10: EGL10?, eGLDisplay: EGLDisplay?): EGLConfig?
}