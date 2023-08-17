package com.magicfluids

import android.app.*
import android.content.*
import android.hardware.*
import android.view.*

class OrientationSensor(context: Context, application: Application) : SensorEventListener {

    @JvmField
    var AccelerationX = 0f
    @JvmField
    var AccelerationY = 0f
    @JvmField
    var Orientation = 0
    private lateinit var acceleration: FloatArray
    private val accelerometer: Sensor
    private val application: Application
    var isRegistered: Boolean
        private set
    private val sensorManager: SensorManager

    // android.hardware.SensorEventListener
    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}

    init {
        val sensorManager = context.getSystemService("sensor") as SensorManager
        this.sensorManager = sensorManager
        accelerometer = sensorManager.getDefaultSensor(1)
        this.application = application
        isRegistered = false
    }

    fun register() {
        if (isRegistered) {
            return
        }
        sensorManager.registerListener(this, accelerometer, 3)
        isRegistered = true
    }

    fun unregister() {
        if (isRegistered) {
            sensorManager.unregisterListener(this)
            isRegistered = false
        }
    }

    // android.hardware.SensorEventListener
    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val fArr = sensorEvent.values.clone()
        acceleration = fArr
        AccelerationX = fArr[1]
        AccelerationY = fArr[0]
        Orientation = (application.getSystemService("window") as WindowManager).defaultDisplay.rotation
    }
}