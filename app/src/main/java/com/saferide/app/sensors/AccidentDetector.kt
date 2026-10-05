package com.saferide.app.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.sqrt

class AccidentDetector(private val context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    // Thresholds
    private val shakeThreshold = 15.0f // m/s² acceleration above gravity
    private val impactThreshold = 35.0f // sharp impact
    private val shakeDurationThreshold = 2000L // 2 seconds

    // State
    private var shakeStartTime = 0L
    private var isCurrentlyShaking = false
    private var lastAccidentTriggerTime = 0L
    private val accidentCooldown = 5000L

    private var onAccidentDetected: ((AccidentType) -> Unit)? = null
    private var onImpactDetected: ((Float) -> Unit)? = null

    private var isMonitoring = false

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun startMonitoring() {
        if (accelerometer != null && !isMonitoring) {
            sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
            isMonitoring = true
            Log.d(TAG, "Accident monitoring started")
        }
    }

    fun stopMonitoring() {
        if (isMonitoring) {
            sensorManager?.unregisterListener(this)
            isMonitoring = false
            resetState()
        }
    }

    fun setAccidentListener(listener: (AccidentType) -> Unit) {
        this.onAccidentDetected = listener
    }

    fun setImpactListener(listener: (Float) -> Unit) {
        this.onImpactDetected = listener
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = calculateAccelerationMagnitude(x, y, z)
            
            checkShaking(acceleration)
            checkExtremeImpact(acceleration)
        }
    }

    private fun calculateAccelerationMagnitude(x: Float, y: Float, z: Float): Float {
        val totalAcceleration = sqrt(x * x + y * y + z * z)
        return Math.abs(totalAcceleration - SensorManager.GRAVITY_EARTH)
    }

    private fun checkShaking(acceleration: Float) {
        val currentTime = System.currentTimeMillis()

        if (acceleration > shakeThreshold) {
            if (!isCurrentlyShaking) {
                isCurrentlyShaking = true
                shakeStartTime = currentTime
                Log.d(TAG, "Shake started")
            } else {
                val duration = currentTime - shakeStartTime
                if (duration >= shakeDurationThreshold) {
                    if (currentTime - lastAccidentTriggerTime > accidentCooldown) {
                        Log.e(TAG, "ACCIDENT DETECTED: Continuous shaking for ${duration}ms")
                        onAccidentDetected?.invoke(AccidentType.MODERATE_CRASH)
                        lastAccidentTriggerTime = currentTime
                        resetState() // Reset to prevent multiple triggers for same shake
                    }
                }
            }
        } else {
            if (isCurrentlyShaking) {
                isCurrentlyShaking = false
                shakeStartTime = 0L
                Log.d(TAG, "Shake stopped")
            }
        }
    }

    private fun checkExtremeImpact(acceleration: Float) {
        if (acceleration > impactThreshold) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastAccidentTriggerTime > accidentCooldown) {
                Log.e(TAG, "ACCIDENT DETECTED: Extreme Impact! Magnitude: $acceleration")
                onAccidentDetected?.invoke(AccidentType.SEVERE_CRASH)
                lastAccidentTriggerTime = currentTime
            }
        }
    }

    private fun resetState() {
        isCurrentlyShaking = false
        shakeStartTime = 0L
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun release() {
        stopMonitoring()
        sensorManager = null
        accelerometer = null
    }

    companion object {
        private const val TAG = "AccidentDetector"
    }
}

enum class AccidentType {
    MINOR_IMPACT,
    MODERATE_CRASH,
    SEVERE_CRASH,
    FALL_WITH_IMPACT
}
