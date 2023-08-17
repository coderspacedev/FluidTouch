package com.magicfluids

import android.opengl.GLSurfaceView.EGLConfigChooser
import com.fluidsimulation.ext.*
import javax.microedition.khronos.egl.*

class MultiSampleConfigChooser : EGLConfigChooser {

    private var mUsesCoverageAa = false
    private lateinit var mValue: IntArray
    override fun chooseConfig(egl10: EGL10, eGLDisplay: EGLDisplay): EGLConfig {
        var eGLConfigArr: Array<EGLConfig?>
        val iArr = IntArray(1)
        mValue = iArr
        var iArr2 = intArrayOf(12340, 12344, 12324, 5, 12323, 6, 12322, 5, 12325, 16, 12352, 4, 12338, 1, 12337, 2, 12344)
        if (!egl10.eglChooseConfig(eGLDisplay, iArr2, null, 0, iArr)) {
            TAG.log("chooseConfig" + "msaa not found")
        }
        val iArr3 = mValue
        var i = 0
        var i2 = iArr3[0]
        if (i2 <= 0) {
            iArr2 = intArrayOf(12340, 12344, 12324, 5, 12323, 6, 12322, 5, 12325, 16, 12352, 4, 12512, 1, 12513, 2, 12344)
            if (!egl10.eglChooseConfig(eGLDisplay, iArr2, null, 0, iArr3)) {
                TAG.log("chooseConfig" + "csaa not found")
            }
            val iArr4 = mValue
            i2 = iArr4[0]
            if (i2 <= 0) {
                iArr2 = intArrayOf(12340, 12344, 12324, 5, 12323, 6, 12322, 5, 12325, 16, 12352, 4, 12344)
                if (!egl10.eglChooseConfig(eGLDisplay, iArr2, null, 0, iArr4)) {
                    TAG.log("chooseConfig" + "is there ANY config?!")
                }
                i2 = mValue[0]
                if (i2 <= 0) {
                    TAG.log("chooseConfig" + "Damn. There isn't.")
                }
            } else {
                mUsesCoverageAa = true
                TAG.log("chooseConfig" + "CSAA config found")
            }
        } else {
            TAG.log("chooseConfig" + "MSAA config found")
        }
        val i3 = i2
        var eGLConfigArr2 = arrayOfNulls<EGLConfig>(i3)
        if (!egl10.eglChooseConfig(eGLDisplay, iArr2, eGLConfigArr2, i3, mValue)) {
            TAG.log("chooseConfig" + "rotfl. Don't know what happened")
        }
        TAG.log("chooseConfignum configs $i3")
        var i4 = 0
        while (true) {
            if (i4 >= i3) {
                eGLConfigArr = eGLConfigArr2
                i4 = -1
                break
            }
            eGLConfigArr = eGLConfigArr2
            if (findConfigAttrib(egl10, eGLDisplay, eGLConfigArr2[i4], 12324, 0) == 8) {
                break
            }
            i4++
            eGLConfigArr2 = eGLConfigArr
        }
        if (i4 == -1) {
            while (true) {
                if (i >= i3) {
                    break
                } else if (findConfigAttrib(egl10, eGLDisplay, eGLConfigArr[i], 12324, 0) == 5) {
                    i4 = i
                    break
                } else {
                    i++
                }
            }
        }
        if (i4 != -1) {
            TAG.log("chooseConfig" + "config index chosen: " + i4 + ", with red channel size: " + findConfigAttrib(egl10, eGLDisplay, eGLConfigArr[i4], 12324, 0))
        }
        if (i4 == -1) {
            TAG.log("Did not find sane config, using first")
        }
        val eGLConfig = if (i3 > 0) eGLConfigArr[i4] else null
        if (eGLConfig != null) {
            return eGLConfig
        }
        throw IllegalArgumentException("No config chosen")
    }

    private fun findConfigAttrib(egl10: EGL10, eGLDisplay: EGLDisplay, eGLConfig: EGLConfig?, i: Int, i2: Int): Int {
        return if (egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, mValue)) mValue[0] else i2
    }

    fun usesCoverageAa(): Boolean {
        return mUsesCoverageAa
    }

    companion object {

        private const val TAG = "MultiSampleConfigChooser"
    }
}