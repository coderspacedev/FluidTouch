package com.fluidsimulation.ui.activity

import android.app.*
import android.content.*
import android.graphics.*
import android.view.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.helper.*
import com.fluidsimulation.model.*
import com.fluidsimulation.services.*
import com.magicfluids.*
import kotlinx.coroutines.*

class FluidActivity : BaseActivity<ActivityFluidBinding>(ActivityFluidBinding::inflate, isFullScreen = true, isStatusBarTransparent = true) {

    private var renderer: GLES20Renderer? = null
    private var orientationSensor: OrientationSensor? = null
    private val TAG = "FluidActivity"
    private var nativeInterface: NativeInterface? = null

    override fun initExtra() {
        initNative()
        initGL()
    }

    private fun initNative() {
        nativeInterface = NativeInterface()
        if (NativeInterface.loadingFailed) {
            TAG.log("Installation failed!")
        }
        nativeInterface?.setAssetManager(assets)
    }

    private fun initGL() {
        binding?.apply {
            orientationSensor = OrientationSensor(this@FluidActivity, application)
            surfaceView.setEGLContextClientVersion(3)
            surfaceView.setEGLConfigChooser(MultiSampleConfigChooser())

            if (Settings.Current == null) {
                Settings.Current = Settings()
            }
            surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            renderer = GLES20Renderer(this@FluidActivity, nativeInterface, orientationSensor, Settings.Current)
//            surfaceView.setZOrderOnTop(true)
            surfaceView.holder.setFormat(PixelFormat.TRANSLUCENT)
            surfaceView.setRenderer(renderer)

            renderer?.setInitialScreenSize(300, 200)
            nativeInterface?.onCreate(300, 200, false)
            runBlocking {
                initPresets()
                QualitySetting.init()
                Settings.Current?.let { loadSettingsFromMap(it, DEFAULT_SETTING, true) }
                if (getNumAppRuns(this@FluidActivity) == 0) {
                    presetList?.get(TinyDB(this@FluidActivity).getInt(SELECTED_PRESET, 0))?.setting?.let {
                        Settings.Current?.setFromInternalPreset(it)
                        QualitySetting.setQualitySettingsFromPerf(it, nativeInterface)
                        Settings.Current?.GPUQuality = if ((it.QualityBaseValue ?: 0) < 320) 1 else 2
                    }
                } else {
                    Settings.Current?.ReloadRequired = true
                }
                newAppRun(this@FluidActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.surfaceView?.onResume()
        nativeInterface?.onResume()
        if ((Settings.Current?.GPUQuality ?: 0) > 0) {
            nativeInterface?.clearScreen()
        }
        if ((Settings.Current?.Gravity ?: 0F) > 3.0E-4f) {
            orientationSensor?.register()
        } else {
            orientationSensor?.unregister()
        }
        if (Settings.Current?.ReloadRequired == true)
            onSettingsChanged()
    }

    override fun onPause() {
        super.onPause()
        Settings.Current?.let { saveSessionSettings(it, SETTINGS_NAME) }
        nativeInterface?.onPause()
        binding?.surfaceView?.onPause()
        orientationSensor?.unregister()
    }

    private fun onSettingsChanged() {
        Settings.Current?.process()
        if ((Settings.Current?.GPUQuality ?: 0) > 0) {
            nativeInterface?.clearScreen()
        }
        if ((Settings.Current?.Gravity ?: 0f) > 3.0E-4f) {
            orientationSensor?.register()
        } else {
            orientationSensor?.unregister()
        }
        Settings.Current?.let { nativeInterface?.updateSettings(it) }
    }

    fun updateAdvGPUFeaturesAvailable() {
    }

    override fun initListeners() {
        binding?.apply {
            buttonSettings.setOnClickListener {
                go(SettingsActivity::class.java)
            }
            buttonClear.setOnClickListener {
                nativeInterface?.clearScreen()
            }
            buttonApply.setOnClickListener {
                Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                    putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, ComponentName(this@FluidActivity, NewWallpaperService::class.java))
                    startActivity(this)
                }
            }
        }
    }

    override fun initView() {
        binding?.apply {
            layoutTopBar.setOnApplyWindowInsetsListener { v: View, insets: WindowInsets ->
                v.setPadding(0, getStatusBarHeight(), 0, 0)
                insets
            }
        }
    }

    override fun onDestroy() {
        nativeInterface?.onDestroy()
        super.onDestroy()
    }
}