package com.fluidsimulation.services

import android.content.*
import android.graphics.*
import android.os.*
import android.service.wallpaper.*
import android.util.*
import android.view.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.helper.*
import com.fluidsimulation.model.*
import com.magicfluids.*
import kotlinx.coroutines.*

class ModifiedWallpaperService() : WallpaperService() {

    private val TAG = "ModifiedWallpaper"
    private var surfaceView: MyGLSurfaceView? = null
    private var nativeInterface: NativeInterface? = null
    private var renderer: GLES20Renderer2? = null
    private var orientationSensor: OrientationSensor? = null
    private var widgetGroup: WidgetGroup? = null
    private var height = 0
    private var width = 0

    open inner class OpenGLES2Engine : Engine() {

        private var handler: Handler? = null

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            setTouchEventsEnabled(true)
            nativeInterface = NativeInterface()
            widgetGroup = WidgetGroup(this@ModifiedWallpaperService)
            surfaceView = MyGLSurfaceView(this@ModifiedWallpaperService)
            surfaceView?.setEGLContextClientVersion(2)

            nativeInterface = NativeInterface()
            if (NativeInterface.loadingFailed) {
                TAG.log("Installation failed!")
            }
            nativeInterface?.setAssetManager(assets)

            orientationSensor = OrientationSensor(this@ModifiedWallpaperService, application)
            if (Settings.Current == null) {
                Settings.Current = Settings()
            }
            renderer = GLES20Renderer2(null, nativeInterface, orientationSensor, Settings.Current)
            surfaceView?.holder?.setFormat(PixelFormat.TRANSLUCENT)
            surfaceView?.setZOrderOnTop(true)
            surfaceView?.preserveEGLContextOnPause = true
            surfaceView?.setRenderer(renderer)
            renderer?.setInitialScreenSize(300, 200)
            nativeInterface?.setAssetManager(assets)
            nativeInterface?.onCreate(300, 200, true)

            handler = Handler(Looper.getMainLooper())
            startRenderingLoop()

            runBlocking {
                initPresets()
                QualitySetting.init()
                Settings.Current?.let { loadSettingsFromMap(it, DEFAULT_SETTING, true) }
                if (getNumLwpRuns(this@ModifiedWallpaperService) == 0) {
                    presetList?.get(TinyDB(this@ModifiedWallpaperService).getInt(SELECTED_PRESET, 0))?.setting?.let {
                        Settings.Current?.setFromInternalPreset(it)
                        QualitySetting.setQualitySettingsFromPerf(it, nativeInterface)
                    }
                }
            }
            Settings.Current?.process()
            Settings.Current?.let { nativeInterface?.updateSettings(it) }
            widgetGroup?.addView(surfaceView)
        }

        private fun startRenderingLoop() {
            handler?.post(renderingRunnable)
        }

        private val renderingRunnable = object : Runnable {
            override fun run() {
                renderer?.onDrawFrame(null) // Trigger the rendering
                handler?.postDelayed(this, 10000L) // Repeat the runnable in a loop
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder?, format: Int, w: Int, h: Int) {
            TAG.log("onSurfaceChanged()")
            width = w
            height = h
            initDraw()
            super.onSurfaceChanged(holder, format, width, height)
        }

        override fun onSurfaceCreated(surfaceHolder: SurfaceHolder) {
            TAG.log("onSurfaceCreated()")
            super.onSurfaceCreated(surfaceHolder)
        }

        override fun onSurfaceDestroyed(surfaceHolder: SurfaceHolder) {
            TAG.log("onSurfaceDestroyed()")
            super.onSurfaceDestroyed(surfaceHolder)
        }

        override fun onOffsetsChanged(xOffset: Float, yOffset: Float, xOffsetStep: Float, yOffsetStep: Float, xPixelOffset: Int, yPixelOffset: Int) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset)
            initDraw()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            if (visible) {
                initDraw()
                onResume()
            } else {
                onPause()
            }
            super.onVisibilityChanged(visible)
        }

        fun onPause() {
//            Settings.Current?.let { saveSessionSettings(it, SETTINGS_NAME) }
            surfaceView?.onPause()
            nativeInterface?.onPause()
            orientationSensor?.unregister()
            handler?.removeCallbacks(renderingRunnable)
        }

        fun onResume() {
            surfaceView?.onResume()
            nativeInterface?.onResume()
            if ((Settings.Current?.GPUQuality ?: 0) > 0) {
                nativeInterface?.clearScreen()
            }
            if ((Settings.Current?.Gravity ?: 0F) > 3.0E-4f) {
                orientationSensor?.register()
            } else {
                orientationSensor?.unregister()
            }
            handler?.post(renderingRunnable)
        }

        override fun onDestroy() {
            handler?.removeCallbacks(renderingRunnable)
            super.onDestroy()
        }

        private fun initDraw() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    drawView(canvas, surfaceHolder)
                }
            } finally {
                if (canvas != null) try {
                    holder.unlockCanvasAndPost(canvas)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }
            }
        }

        private fun drawView(canvas: Canvas, surfaceHolder: SurfaceHolder) {
            canvas.save()
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            widgetGroup?.layout(0, 0, width, height)
//            widgetGroup?.setBackgroundColor(Color.BLUE)
//            imageView?.scaleType = ImageView.ScaleType.CENTER_CROP
//            imageView?.setImageResource(R.drawable.ic_background_image)
//            imageView?.layout(0, 0, width, height)

            surfaceView?.layout(0, 0, width, height)
            widgetGroup?.draw(canvas)
            canvas.restore()
        }

        override fun onTouchEvent(motionEvent: MotionEvent) {
//            Log.e(TAG, "onTouchEvent: ${motionEvent.x}:${motionEvent.y}")
            surfaceView?.onTouchEvent(motionEvent)
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