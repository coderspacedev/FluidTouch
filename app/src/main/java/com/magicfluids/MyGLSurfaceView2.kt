package com.magicfluids

import android.content.*
import android.graphics.*
import android.opengl.*
import android.util.*
import android.view.*
import com.fluidsimulation.App.Companion.app
import com.fluidsimulation.ext.*
import com.fluidsimulation.helper.*
import com.fluidsimulation.model.*
import kotlinx.coroutines.*

class MyGLSurfaceView2 : GLSurfaceView {

    private var renderer: GLES20Renderer2? = null
    private var orientationSensor: OrientationSensor? = null
    private var nativeInterface: NativeInterface? = null

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context?) : super(context){
        initView(context)
    }

    private fun initView(context: Context?) {
        nativeInterface = NativeInterface()
        orientationSensor = app?.let { context?.let { it1 -> OrientationSensor(it1, it) } }
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        renderer = GLES20Renderer2(null, nativeInterface, orientationSensor, Settings.Current)
        holder?.setFormat(PixelFormat.TRANSLUCENT)
        setZOrderOnTop(true)
        setRenderer(renderer)
        renderer?.setInitialScreenSize(300, 200)
        nativeInterface?.setAssetManager(context?.assets)
        nativeInterface?.onCreate(300, 200, true)

        context?.apply {
        runBlocking {
            initPresets()
            QualitySetting.init()
            Settings.Current?.let { loadSettingsFromMap(it, DEFAULT_SETTING, true) }
            if (getNumLwpRuns(this@apply) == 0) {
                presetList?.get(TinyDB(this@apply).getInt(SELECTED_PRESET, 0))?.setting?.let {
                    Settings.Current?.setFromInternalPreset(it)
                    QualitySetting.setQualitySettingsFromPerf(it, nativeInterface)
                }
            }
        } }
        Settings.Current?.process()
        Settings.Current?.let { nativeInterface?.updateSettings(it) }

        renderMode = RENDERMODE_CONTINUOUSLY
    }

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