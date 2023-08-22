package com.magicfluids

import android.service.wallpaper.*
import android.util.*
import android.view.*
import com.magicfluids.listeners.*
import com.magicfluids.BaseConfigChooser.ComponentSizeChooser
import com.magicfluids.BaseConfigChooser.SimpleEGLConfigChooser

open class GLWallpaperServiceRBG : WallpaperService() {
    open inner class GLEngine : Engine() {
        private var eglConfigChooser: EGLConfigChooser? = null
        private var eglContextClientVersion = 0
        private var eglContextFactory: EGLContextFactory? = null
        private var eglWindowSurfaceFactory: EGLWindowSurfaceFactory? = null
        private var glThread: GLThread? = null
        private var glWrapper: GLWrapper? = null
        override fun onVisibilityChanged(z: Boolean) {
            if (z) {
                onResume()
            } else {
                onPause()
            }
            super.onVisibilityChanged(z)
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
        }

        override fun onDestroy() {
            super.onDestroy()
            glThread?.requestExitAndWait()
        }

        override fun onSurfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i2: Int, i3: Int) {
            glThread?.onWindowResize(i2, i3)
            super.onSurfaceChanged(surfaceHolder, i, i2, i3)
        }

        override fun onSurfaceCreated(surfaceHolder: SurfaceHolder) {
            Log.d(TAG, "onSurfaceCreated()")
            glThread?.surfaceCreated(surfaceHolder)
            super.onSurfaceCreated(surfaceHolder)
        }

        override fun onSurfaceDestroyed(surfaceHolder: SurfaceHolder) {
            Log.d(TAG, "onSurfaceDestroyed()")
            glThread?.surfaceDestroyed()
            super.onSurfaceDestroyed(surfaceHolder)
        }

        fun setGLWrapper(gLWrapper: GLWrapper?) {
            glWrapper = gLWrapper
        }

        fun setEGLContextClientVersion(i: Int) {
            checkRenderThreadState()
            eglContextClientVersion = i
        }

        fun setRenderer(gLES20Renderer: GLES20Renderer?) {
            checkRenderThreadState()
            if (eglConfigChooser == null) {
                eglConfigChooser = SimpleEGLConfigChooser(true, eglContextClientVersion)
            }
            if (eglContextFactory == null) {
                eglContextFactory = DefaultContextFactory()
            }
            if (eglWindowSurfaceFactory == null) {
                eglWindowSurfaceFactory = DefaultWindowSurfaceFactory()
            }
            glThread = GLThread(gLES20Renderer, eglConfigChooser, eglContextFactory, eglWindowSurfaceFactory, glWrapper, eglContextClientVersion)
            glThread?.start()
        }

        fun setEGLContextFactory(eGLContextFactory: EGLContextFactory?) {
            checkRenderThreadState()
            eglContextFactory = eGLContextFactory
        }

        fun setEGLWindowSurfaceFactory(eGLWindowSurfaceFactory: EGLWindowSurfaceFactory?) {
            checkRenderThreadState()
            eglWindowSurfaceFactory = eGLWindowSurfaceFactory
        }

        fun setEGLConfigChooser(eGLConfigChooser: EGLConfigChooser?) {
            checkRenderThreadState()
            eglConfigChooser = eGLConfigChooser
        }

        fun setEGLConfigChooser(z: Boolean) {
            setEGLConfigChooser(SimpleEGLConfigChooser(z, eglContextClientVersion))
        }

        fun setEGLConfigChooser(i: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int) {
            setEGLConfigChooser(ComponentSizeChooser(i, i2, i3, i4, i5, i6, eglContextClientVersion))
        }

        var renderMode: Int
            get() = glThread?.renderMode ?: 0
            set(i) {
                glThread?.renderMode = i
            }

        fun requestRender() {
            glThread?.requestRender()
        }

        fun onPause() {
            glThread?.onPause()
        }

        fun onResume() {
            glThread?.onResume()
        }

        fun queueEvent(runnable: Runnable) {
            glThread?.queueEvent(runnable)
        }

        private fun checkRenderThreadState() {
            check(glThread == null) { "setRenderer has already been called for this instance." }
        }
    }

    override fun onCreateEngine(): Engine {
        return GLEngine()
    }

    companion object {

        const val RENDERMODE_CONTINUOUSLY = 1
        const val RENDERMODE_WHEN_DIRTY = 0
        private const val TAG = "GLWallpaperService"
    }
}