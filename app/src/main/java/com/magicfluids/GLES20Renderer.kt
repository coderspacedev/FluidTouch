package com.magicfluids

import android.content.*
import android.graphics.*
import android.net.*
import android.opengl.*
import android.os.*
import android.text.format.*
import android.widget.*
import androidx.core.view.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.ui.activity.*
import com.magicfluids.InputBuffer.Companion.Instance
import java.io.*
import java.nio.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.*

class GLES20Renderer(var activity: FluidActivity?, private val nativeInterface: NativeInterface?, private val orientationSensor: OrientationSensor?, private val settings: Settings?) : GLSurfaceView.Renderer {

    private val TAG = "GLES20Renderer"
    private var screenHeight = 0
    private var screenWidth = 0
    private var ignoreNextFrameTime = false
    private val screenshotLock = Any()
    private var takeScreenshot = false
    var input = Input()
    fun setInitialScreenSize(i: Int, i2: Int) {
        screenWidth = i
        screenHeight = i2
    }

    fun orderScreenshot() {
        synchronized(screenshotLock) { takeScreenshot = true }
    }

    fun checkScreenshotOrder(): Boolean {
        synchronized(screenshotLock) {
            if (takeScreenshot) {
                takeScreenshot = false
                return true
            }
            return false
        }
    }

    fun saveBitmap(bitmap: Bitmap) {
        val obj: Any
        val obj2: Any
        val obj3: Any
        val obj4: Any
        val obj5: Any
        val str = Environment.DIRECTORY_PICTURES
        try {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/MagicFluids")
            TAG.log("saveBitmap-> mkdirs result: " + file.mkdirs())
            val time = Time()
            time.setToNow()
            val i = time.month + 1
            val sb = StringBuilder()
            sb.append(time.year)
            sb.append("-")
            if (i >= 10) {
                obj = Integer.valueOf(i)
            } else {
                obj = "0$i"
            }
            sb.append(obj)
            sb.append("-")
            if (time.monthDay >= 10) {
                obj2 = Integer.valueOf(time.monthDay)
            } else {
                obj2 = "0" + time.monthDay
            }
            sb.append(obj2)
            sb.append("-")
            if (time.hour >= 10) {
                obj3 = Integer.valueOf(time.hour)
            } else {
                obj3 = "0" + time.hour
            }
            sb.append(obj3)
            sb.append("-")
            if (time.minute >= 10) {
                obj4 = Integer.valueOf(time.minute)
            } else {
                obj4 = "0" + time.minute
            }
            sb.append(obj4)
            sb.append("-")
            if (time.second >= 10) {
                obj5 = Integer.valueOf(time.second)
            } else {
                obj5 = "0" + time.second
            }
            sb.append(obj5)
            val sb2 = ("MagicFluids" + sb + ".jpg")
            val file2 = File(file, sb2)
            val fileOutputStream = FileOutputStream(file2)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            activity?.runOnUiThread {
                val intent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
                intent.data = Uri.fromFile(file2)
                activity?.sendBroadcast(intent)
                Toast.makeText(activity, "Saved to /Pictures/MagicFluids", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            TAG.log("saveBitmap->$e")
            activity?.runOnUiThread { Toast.makeText(activity, "External storage unavailable! Please put in SD Card or disconnect device from computer", Toast.LENGTH_LONG).show() }
        }
    }

    fun screenshot() {
        val i = screenWidth
        val i2 = screenHeight
        val iArr = IntArray(i * i2)
        val allocate = ByteBuffer.allocate(i * i2 * 4)
        GLES20.glGetError()
        GLES20.glPixelStorei(3333, 1)

        TAG.log("screenshot-> glPixelStorei result: " + GLES20.glGetError())
        GLES20.glReadPixels(0, 0, screenWidth, screenHeight, 6408, 5121, allocate)
        TAG.log("screenshot-> glReadPixels result: " + GLES20.glGetError())
        val array = allocate.array()
        val i3 = 0
        var z = false
        while (true) {
            val i4 = screenHeight
            if (i3 >= i4) {
                saveBitmap(Bitmap.createBitmap(iArr, screenWidth, i4, Bitmap.Config.ARGB_8888))
                TAG.log("screenshot-> wtf: $z")
                return
            }
            var i5 = 0
            while (true) {
                val i6 = screenWidth
                if (i5 < i6) {
                    val i7 = screenHeight
                    val b = array[((((i7 - 1) - i3) * i6) + i5) * 4]
                    val b2 = array[(((((i7 - 1) - i3) * i6) + i5) * 4) + 1]
                    val b3 = array[(((((i7 - 1) - i3) * i6) + i5) * 4) + 2]
                    if ((b < 0) || (b2 < 0) || (b3 < 0)) {
                        z = true
                    }
                    iArr[(i6 * i3) + i5] = (byteToUint(b) shl 16) + ViewCompat.MEASURED_STATE_MASK + (byteToUint(b2) shl 8) + byteToUint(b3)
                    i5++
                }
            }
        }
    }

    // android.opengl.GLSurfaceView.Renderer
    override fun onDrawFrame(gl10: GL10) {
        Instance.getCurrentInputState(input)
        for (i in 0 until input.NumEvents) {
            input.Events[i]?.let { nativeInterface?.onMotionEvent(it) }
        }
        nativeInterface?.updateApp(ignoreNextFrameTime, orientationSensor?.AccelerationX ?: 0F, orientationSensor?.AccelerationY ?: 0F, orientationSensor?.Orientation ?: 0)
        ignoreNextFrameTime = false
        if (checkScreenshotOrder()) {
            screenshot()
            ignoreNextFrameTime = true
        }
    }

    override fun onSurfaceChanged(gl10: GL10, i: Int, i2: Int) {
        screenWidth = i
        screenHeight = i2
        nativeInterface?.windowChanged(i, i2)
        TAG.log("onSurfaceChanged")
    }

    fun checkGPUExtensions(): Boolean {
        val glGetString = GLES20.glGetString(7939)
        return glGetString.contains("GL_OES_texture_half_float_linear") and glGetString.contains("GL_EXT_color_buffer_half_float") and glGetString.contains("GL_OES_texture_half_float")
    }

    override fun onSurfaceCreated(gl10: GL10, eGLConfig: EGLConfig) {
        settings?.GPUAnimAvailable = checkGPUExtensions()
        settings?.let { nativeInterface?.updateSettings(it) }
        if (activity != null) {
            activity?.runOnUiThread { activity?.updateAdvGPUFeaturesAvailable() }
        }
        TAG.log(GLES20.glGetString(7939))
        if (isLWP) {
            return
        }
        TAG.log("onSurfaceCreated: Not a wallpaper. Reloading resources")
        onEGLContextCreated()
    }

    private val isLWP: Boolean
        private get() = activity == null

    fun onEGLContextCreated() {
        nativeInterface?.onGLContextRestarted()
    }

    companion object {

        fun byteToUint(b: Byte): Int {
            return if (b < 0) b + 0 else b.toInt()
        }
    }
}