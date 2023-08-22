package com.magicfluids

import com.magicfluids.listeners.*
import javax.microedition.khronos.egl.*

abstract class BaseConfigChooser(iArr: IntArray, protected var eglcontextclientversion: Int) : EGLConfigChooser {

    protected var configSpec: IntArray
    abstract fun chooseConfig(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLConfigArr: Array<EGLConfig?>): EGLConfig?
    open class ComponentSizeChooser(protected var redSize: Int, protected var greenSize: Int, protected var blueSize: Int, protected var alphaSize: Int, protected var depthSize: Int, protected var stencilSize: Int, i7: Int) : BaseConfigChooser(intArrayOf(12324, redSize, 12323, greenSize, 12322, blueSize, 12321, alphaSize, 12325, depthSize, 12326, stencilSize, 12344), i7) {

        private val value: IntArray
        override fun chooseConfig(egl10: EGL10?, eGLDisplay: EGLDisplay?): EGLConfig? {
            return super.chooseConfig(egl10, eGLDisplay)
        }

        init {
            value = IntArray(1)
        }

        override fun chooseConfig(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLConfigArr: Array<EGLConfig?>): EGLConfig? {
            var abs: Int = 0
            var eGLConfig: EGLConfig? = null
            var i = 1000
            for (eGLConfig2 in eGLConfigArr) {
                val findConfigAttrib = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12325, 0)
                val findConfigAttrib2 = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12326, 0)
                if (findConfigAttrib >= depthSize && findConfigAttrib2 >= stencilSize && Math.abs(findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12324, 0) - redSize) + Math.abs(findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12323, 0) - greenSize) + Math.abs(findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12322, 0) - blueSize) + Math.abs(findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12321, 0) - alphaSize).also { abs = it } < i) {
                    eGLConfig = eGLConfig2
                    i = abs
                }
            }
            return eGLConfig
        }

        private fun findConfigAttrib(egl10: EGL10?, eGLDisplay: EGLDisplay?, eGLConfig: EGLConfig?, i: Int, i2: Int): Int {
            return if (egl10!!.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, value)) value[0] else i2
        }
    }

    class SimpleEGLConfigChooser(z: Boolean, i: Int) : ComponentSizeChooser(4, 4, 4, 0, if (z) 16 else 0, 0, i) {

        override fun chooseConfig(egl10: EGL10?, eGLDisplay: EGLDisplay?): EGLConfig? {
            return super.chooseConfig(egl10, eGLDisplay)
        }

        init {
            redSize = 8
            greenSize = 8
            blueSize = 8
        }
    }

    init {
        configSpec = filterConfigSpec(iArr)
    }

    override fun chooseConfig(egl10: EGL10?, eGLDisplay: EGLDisplay?): EGLConfig? {
        val iArr = IntArray(1)
        egl10!!.eglChooseConfig(eGLDisplay, configSpec, null, 0, iArr)
        val i = iArr[0]
        require(i > 0) { "No configs match configSpec" }
        val eGLConfigArr = arrayOfNulls<EGLConfig>(i)
        egl10.eglChooseConfig(eGLDisplay, configSpec, eGLConfigArr, i, iArr)
        val chooseConfig = chooseConfig(egl10, eGLDisplay, eGLConfigArr)
        if (chooseConfig != null) {
            return chooseConfig
        }
        throw IllegalArgumentException("No config chosen")
    }

    private fun filterConfigSpec(iArr: IntArray): IntArray {
        if (eglcontextclientversion != 2) {
            return iArr
        }
        val length = iArr.size
        val iArr2 = IntArray(length + 2)
        val i = length - 1
        System.arraycopy(iArr, 0, iArr2, 0, i)
        iArr2[i] = 12352
        iArr2[length] = 4
        iArr2[length + 1] = 12344
        return iArr2
    }
}