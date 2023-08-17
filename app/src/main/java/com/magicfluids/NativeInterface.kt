package com.magicfluids

import android.content.res.*
import android.graphics.*
import android.opengl.*
import com.fluidsimulation.ext.*
import java.io.*

class NativeInterface {

    private val TAG = "NativeInterface"
    var id = 0
    private var assetMgr: AssetManager? = null

    @Volatile
    private var drawOnly = false

    @Volatile
    private var paused = true
    private external fun clearScreenImpl(i: Int)
    private external fun onCreateImpl(i: Int, i2: Int, i3: Int, z: Boolean)
    private external fun onDestroyImpl(i: Int)
    private external fun onGLContextRestartedImpl(i: Int)
    private external fun onMotionEventImpl(i: Int, motionEventWrapper: MotionEventWrapper)
    private external fun onPauseImpl(i: Int)
    private external fun onResumeImpl(i: Int)
    private external fun perfHeuristicImpl(i: Int): Int
    private external fun updateAppImpl(i: Int, z: Boolean, z2: Boolean, f: Float, f2: Float, i2: Int)
    private external fun updateSettingsImpl(i: Int, settings: Settings)
    private external fun windowChangedImpl(i: Int, i2: Int, i3: Int)

    init {
        synchronized(nextIdLock) {
            val i = nextID
            nextID += 1
            id = i
        }
    }

    fun windowChanged(i: Int, i2: Int) {
        if (loadingFailed) {
            return
        }
        if (!paused) {
            paused = true
            synchronized(nativeLock) { windowChangedImpl(id, i, i2) }
            paused = false
            return
        }
        synchronized(nativeLock) { windowChangedImpl(id, i, i2) }
    }

    fun updateApp(z: Boolean, f: Float, f2: Float, i: Int) {
        if (loadingFailed || paused) {
            return
        }
        synchronized(nativeLock) { updateAppImpl(id, z, drawOnly, f, f2, i) }
    }

    fun onMotionEvent(motionEventWrapper: MotionEventWrapper) {
        if (loadingFailed) {
            return
        }
        synchronized(nativeLock) { onMotionEventImpl(id, motionEventWrapper) }
    }

    fun onCreate(i: Int, i2: Int, z: Boolean) {
        if (loadingFailed) {
            return
        }
        if (!paused) {
            paused = true
            synchronized(nativeLock) {
                TAG.log("onCreate while not paused! $id")
                onCreateImpl(id, i, i2, z)
            }
            paused = false
            return
        }
        synchronized(nativeLock) {
            TAG.log("onCreate $id")
            onCreateImpl(id, i, i2, z)
        }
    }

    fun onDestroy() {
        if (loadingFailed) {
            return
        }
        if (!paused) {
            paused = true
            synchronized(nativeLock) {
                TAG.log("onDestroy while not paused! $id")
                onDestroyImpl(id)
            }
            paused = false
            return
        }
        synchronized(nativeLock) {
            TAG.log("onDestroy $id")
            onDestroyImpl(id)
        }
    }

    fun onPause() {
        if (loadingFailed) {
            return
        }
        if (!paused) {
            paused = true
            synchronized(nativeLock) {
                TAG.log("onPause $id")
                onPauseImpl(id)
            }
            return
        }
        TAG.log("onPause no effect $id")
    }

    fun onResume() {
        if (loadingFailed) {
            return
        }
        if (paused) {
            paused = false
            synchronized(nativeLock) {
                TAG.log("onResume $id")
                onResumeImpl(id)
            }
            return
        }
        TAG.log("onResume no effect $id")
    }

    fun clearScreen() {
        if (loadingFailed) {
            return
        }
        if (!paused) {
            paused = true
            synchronized(nativeLock) { clearScreenImpl(id) }
            paused = false
            return
        }
        synchronized(nativeLock) { clearScreenImpl(id) }
    }

    fun onGLContextRestarted() {
        if (loadingFailed) {
            return
        }
        synchronized(nativeLock) {
            TAG.log("onGLContextRestarted $id")
            onGLContextRestartedImpl(id)
        }
    }

    fun updateSettings(settings: Settings) {
        if (loadingFailed) {
            return
        }
        if (!paused) {
            paused = true
            synchronized(nativeLock) { updateSettingsImpl(id, settings) }
            paused = false
            return
        }
        synchronized(nativeLock) { updateSettingsImpl(id, settings) }
    }

    fun perfHeuristic(): Int {
        var perfHeuristicImpl: Int
        if (loadingFailed) {
            return 0
        }
        synchronized(nativeLock) { perfHeuristicImpl = perfHeuristicImpl(id) }
        return perfHeuristicImpl
    }

    fun onPauseAnim() {
        drawOnly = true
    }

    fun onResumeAnim() {
        drawOnly = false
    }

    fun setAssetManager(assetManager: AssetManager?) {
        assetMgr = assetManager
    }

    fun loadFileContentsFromAssets(str: String): ByteArrayWithSize {
        val byteArrayWithSize = ByteArrayWithSize(null, 0)
        return try {
            TAG.log("Reading asset file: $str")
            val read = assetMgr?.open(str)?.read(tmpBuf)
            if (read != null) {
                val bArr = ByteArray(read)
                System.arraycopy(tmpBuf, 0, bArr, 0, read)
                ByteArrayWithSize(bArr, read)
            } else {
                ByteArrayWithSize(byteArrayOf(), 0)
            }
        } catch (e: IOException) {
            TAG.log("Failed to read asset file: $str")
            e.printStackTrace()
            byteArrayWithSize
        }
    }

    fun loadTexture2DFromAssets(str: String): Boolean {
        val inputStream: InputStream? = null
        try {
            TAG.log("Reading texture file: textures/$str")
        } catch (th: Throwable) {
            TAG.log("Reading texture file: error-> ${th.message}")
        }
        return try {
            val assetManager = assetMgr
            val open = assetManager?.open("textures/$str")
            if (str.contains("detail/")) {
                val decodeStream = BitmapFactory.decodeStream(open)
                val createBitmap = Bitmap.createBitmap(decodeStream.width, decodeStream.height, Bitmap.Config.ALPHA_8)
                val width = decodeStream.width
                val height = decodeStream.height
                val i = width * height
                val iArr = IntArray(i)
                decodeStream.getPixels(iArr, 0, width, 0, 0, width, height)
                for (i2 in 0 until i) {
                    iArr[i2] = iArr[i2] and 16711680 shl 8
                }
                createBitmap.setPixels(iArr, 0, width, 0, 0, width, height)
                GLUtils.texImage2D(3553, 0, 6406, createBitmap, 0)
                decodeStream.recycle()
                createBitmap.recycle()
                open?.close()
                return true
            }
            val decodeStream2 = BitmapFactory.decodeStream(open)
            GLUtils.texImage2D(3553, 0, decodeStream2, 0)
            decodeStream2.recycle()
            open?.close()
            true
        } catch (e2: IOException) {
            "ASSETS".log("Failed to read texture file: textures/$str")
            false
        } catch (th2: Throwable) {
            try {
                inputStream?.close()
            } catch (unused: IOException) {
            }
            throw th2
        }
    }

    companion object {

        var loadingFailed = false
        private var nextID = 0
        var nativeLock = Any()
        var nextIdLock = Any()
        private val tmpBuf = ByteArray(2097152)

        init {
            try {
                System.loadLibrary("nativelib")
            } catch (unused: UnsatisfiedLinkError) {
                loadingFailed = true
            }
        }
    }
}