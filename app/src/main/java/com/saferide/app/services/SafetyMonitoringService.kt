package com.saferide.app.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Binder
import android.content.pm.ServiceInfo
import androidx.core.app.NotificationCompat
import com.saferide.app.MainActivity
import com.saferide.app.R
import com.saferide.app.detectors.DrowsinessDetector
import com.saferide.app.detectors.DrowsinessLevel
import com.saferide.app.detectors.FaceStatus
import com.saferide.app.sensors.AccidentDetector
import com.saferide.app.sensors.AccidentType
import com.saferide.app.utils.AlertManager
import com.saferide.app.utils.EmergencyAlertManager
import com.saferide.app.utils.LogManager

class SafetyMonitoringService : Service() {

    private val binder = LocalBinder()
    private var drowsinessDetector: DrowsinessDetector? = null
    private var accidentDetector: AccidentDetector? = null
    private var alertManager: AlertManager? = null
    private var logManager: LogManager? = null
    private var notificationManager: NotificationManager? = null

    private var currentDrowsinessLevel = DrowsinessLevel.NONE
    private var currentFaceStatus = FaceStatus.NO_FACE
    private var lastAccidentType: AccidentType? = null
    private var lastDrowsinessLogTime = 0L

    inner class LocalBinder : Binder() {
        fun getService(): SafetyMonitoringService = this@SafetyMonitoringService
    }

    companion object {
        private const val CHANNEL_ID = "SafeRideMonitoring"
        private const val NOTIFICATION_ID = 1001
        const val ACTION_DROWSINESS_UPDATE = "com.saferide.app.DROWSINESS_UPDATE"
        const val ACTION_ACCIDENT_DETECTED = "com.saferide.app.ACCIDENT_DETECTED"
        const val EXTRA_DROWSINESS_LEVEL = "drowsiness_level"
        const val EXTRA_FACE_STATUS = "face_status"
        const val EXTRA_ACCIDENT_TYPE = "accident_type"
        const val EXTRA_ACCIDENT_SEVERITY = "accident_severity"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        drowsinessDetector = DrowsinessDetector(this)
        accidentDetector = AccidentDetector(this)
        alertManager = AlertManager(this)
        logManager = LogManager(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        setupDrowsinessDetection()
        setupAccidentDetection()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(
                NOTIFICATION_ID,
                createNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION or ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
            )
        } else {
            startForeground(NOTIFICATION_ID, createNotification())
        }

        accidentDetector?.startMonitoring()
        logManager?.startSession()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMonitoring()
    }

    private fun setupDrowsinessDetection() {
        drowsinessDetector?.setDrowsinessListener { level ->
            currentDrowsinessLevel = level
            handleDrowsinessDetection(level)
            broadcastDrowsinessUpdate()
        }

        drowsinessDetector?.setFaceStatusListener { status ->
            currentFaceStatus = status
            broadcastDrowsinessUpdate()
        }
    }

    private fun handleDrowsinessDetection(level: DrowsinessLevel) {
        android.util.Log.d("SafetyService", "Drowsiness level changed to: $level")
        if (level != DrowsinessLevel.NONE && level != DrowsinessLevel.LOW) {
            alertManager?.triggerDrowsinessAlert(level)
            updateNotification("Drowsiness Alert! Level: $level")

            // Log drowsiness (max once every 10 seconds for better history visibility)
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastDrowsinessLogTime > 10000) {
                logManager?.logDrowsiness(level.name)
                lastDrowsinessLogTime = currentTime
            }
        } else {
            alertManager?.triggerDrowsinessAlert(level) // Pass LOW/NONE to AlertManager
            if (level == DrowsinessLevel.NONE) {
                updateNotification("Monitoring your safety...")
            }
        }
    }

    private fun updateNotification(text: String) {
        val notification = createNotification(text)
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    private fun broadcastDrowsinessUpdate() {
        val intent = Intent(ACTION_DROWSINESS_UPDATE).apply {
            putExtra(EXTRA_DROWSINESS_LEVEL, currentDrowsinessLevel.name)
            putExtra(EXTRA_FACE_STATUS, currentFaceStatus.name)
        }
        sendBroadcast(intent)
    }

    fun getDrowsinessDetector(): DrowsinessDetector? = drowsinessDetector

    private fun setupAccidentDetection() {
        accidentDetector?.setAccidentListener { accidentType ->
            handleAccidentDetection(accidentType)
        }

        accidentDetector?.setImpactListener { magnitude ->
            android.util.Log.d("SafetyService", "Impact detected: $magnitude m/s²")
        }
    }

    private fun handleAccidentDetection(accidentType: AccidentType) {
        lastAccidentType = accidentType

        val severity = when (accidentType) {
            AccidentType.MINOR_IMPACT -> "Minor"
            AccidentType.MODERATE_CRASH -> "Moderate"
            AccidentType.SEVERE_CRASH -> "Severe"
            AccidentType.FALL_WITH_IMPACT -> "Fall"
        }

        android.util.Log.e("SafetyService", "ACCIDENT DETECTED! Type: $accidentType")
        updateNotification("⚠️ ACCIDENT DETECTED! Severity: $severity")

        // Trigger maximum alert
        alertManager?.triggerDrowsinessAlert(DrowsinessLevel.CRITICAL)

        // Broadcast accident detection
        broadcastAccidentDetection(accidentType, severity)

        // Handle emergency: fetch GPS location, log accident with location (regardless of SMS delivery), and send SMS to emergency contacts
        val emergencyAlertManager = EmergencyAlertManager(this)
        emergencyAlertManager.handleEmergency(accidentType, severity)
    }

    private fun broadcastAccidentDetection(accidentType: AccidentType, severity: String) {
        val intent = Intent(ACTION_ACCIDENT_DETECTED).apply {
            putExtra(EXTRA_ACCIDENT_TYPE, accidentType.name)
            putExtra(EXTRA_ACCIDENT_SEVERITY, severity)
        }
        sendBroadcast(intent)
    }

    private fun stopMonitoring() {
        alertManager?.stopAll()
        drowsinessDetector?.release()
        accidentDetector?.release()
        logManager?.endSession()
        drowsinessDetector = null
        accidentDetector = null
        alertManager = null
        logManager = null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Safety Monitoring",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitoring rider safety"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(text: String = "Monitoring your safety..."): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SafeRide Active")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }
}
