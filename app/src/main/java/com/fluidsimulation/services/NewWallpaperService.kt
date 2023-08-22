package com.fluidsimulation.services

import android.content.*
import android.graphics.*
import android.view.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.helper.*
import com.fluidsimulation.model.*
import com.magicfluids.*
import com.magicfluids.QualitySetting.setQualitySettingsFromPerf
import kotlinx.coroutines.*

class NewWallpaperService : GLWallpaperServiceRBG() {

    private val TAG = "NewWallpaperService"
    var mostRecentEngine: OpenGLES2Engine? = null

    inner class OpenGLES2Engine : GLEngine() {

        private var nativeInterface: NativeInterface? = null
        private var orientationSensor: OrientationSensor? = null
        private var renderer: GLES20Renderer? = null

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            setTouchEventsEnabled(true)
            nativeInterface = NativeInterface()
            setEGLContextClientVersion(2)
            orientationSensor = OrientationSensor(this@NewWallpaperService, this@NewWallpaperService.application)
            if (Settings.Current == null) {
                Settings.Current = Settings()
            }
            renderer = GLES20Renderer(this@NewWallpaperService, nativeInterface, orientationSensor, Settings.Current)
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            surfaceHolder.setFormat(PixelFormat.TRANSLUCENT)

            setRenderer(renderer)
            renderer?.setInitialScreenSize(300, 200)
            nativeInterface?.setAssetManager(this@NewWallpaperService.assets)
            nativeInterface?.onCreate(300, 200, true)
            runBlocking {
                initPresets()
                QualitySetting.init()
                Settings.Current?.let { loadSettingsFromMap(it, DEFAULT_SETTING, true) }
                if (getNumLwpRuns(this@NewWallpaperService) == 0) {
                    presetList?.get(TinyDB(this@NewWallpaperService).getInt(SELECTED_PRESET, 0))?.setting?.let {
                        Settings.Current?.setFromInternalPreset(it)
                        setQualitySettingsFromPerf(it, nativeInterface)
                    }
                }
            }
//            Settings.Current?.process()
//            Settings.Current?.let { nativeInterface?.updateSettings(it) }
            mostRecentEngine = this
        }

        override fun onDesiredSizeChanged(i: Int, i2: Int) {
            nativeInterface?.windowChanged(i, i2)
            TAG.log("GLEngine onDesiredSizeChanged. NTV ID: " + nativeInterface?.id)
            super.onDesiredSizeChanged(i, i2)
        }

        override fun onVisibilityChanged(z: Boolean) {
            super.onVisibilityChanged(z)
            TAG.log("GLEngine onVisibilityChanged to " + z + ", NTV ID: " + nativeInterface?.id)
            if (z) {
                Settings.Current?.process()
                Settings.Current?.let { nativeInterface?.updateSettings(it) }
                nativeInterface?.onResume()
                if (!isPreview && Settings.Current?.ReloadRequired == true) {
                    nativeInterface?.clearScreen()
                    Settings.Current?.ReloadRequired = false
                }
                if (isPreview && Settings.Current?.ReloadRequiredPreview == true) {
                    nativeInterface?.clearScreen()
                    Settings.Current?.ReloadRequiredPreview = false
                }

                if ((Settings.Current?.Gravity ?: 0F) > 3.0E-4f) {
                    orientationSensor?.register()
                    return
                }
                return
            }
            nativeInterface?.onPause()
            orientationSensor?.unregister()
        }

        override fun onDestroy() {
            super.onDestroy()
            TAG.log("GLEngine onDestroy. NTV ID: " + nativeInterface?.id)
            nativeInterface?.onDestroy()
            orientationSensor?.unregister()
        }

        override fun onTouchEvent(motionEvent: MotionEvent) {
            InputBuffer.Instance.addEvent(motionEvent)
        }
    }

    override fun onCreateEngine(): Engine {
        return OpenGLES2Engine()
    }

    inner class WidgetGroup(context: Context?) : ViewGroup(context) {

        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
            layout(l, t, r, b)
        }
    }
}