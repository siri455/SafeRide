package com.saferide.app

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.permissionx.guolindev.PermissionX
import com.saferide.app.detectors.DrowsinessLevel
import com.saferide.app.detectors.FaceStatus
import com.saferide.app.models.EmergencyContact
import com.saferide.app.sensors.AccidentType
import com.saferide.app.services.SafetyMonitoringService
import com.saferide.app.utils.CameraAnalyzer
import com.saferide.app.utils.PreferencesManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var cameraPreview: PreviewView
    private lateinit var btnToggleMonitoring: Button
    private lateinit var btnViewHistory: Button
    private lateinit var btnAddContact: Button
    private lateinit var drowsinessStatus: TextView
    private lateinit var accidentStatus: TextView
    private lateinit var contactsContainer: LinearLayout
    private lateinit var noContactsText: TextView
    private lateinit var faceOverlay: com.saferide.app.utils.FaceOverlayView

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    private var monitoringService: SafetyMonitoringService? = null
    private var isServiceBound = false
    private var imageAnalysis: ImageAnalysis? = null

    private var isMonitoring = false
    private var activeAlertDialog: AlertDialog? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as SafetyMonitoringService.LocalBinder
            val s = binder.getService()
            monitoringService = s
            isServiceBound = true

            // Set UI specific listener for face overlay
            s.getDrowsinessDetector()?.setFaceDetectionListener { face, width, height ->
                runOnUiThread {
                    faceOverlay.updateFace(face, width, height)
                }
            }

            if (isMonitoring) {
                startCameraAnalysis()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            monitoringService = null
            isServiceBound = false
            stopCameraAnalysis()
        }
    }

    private val drowsinessReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                SafetyMonitoringService.ACTION_DROWSINESS_UPDATE -> {
                    val levelStr = intent.getStringExtra(SafetyMonitoringService.EXTRA_DROWSINESS_LEVEL)
                    val statusStr = intent.getStringExtra(SafetyMonitoringService.EXTRA_FACE_STATUS)

                    levelStr?.let { updateDrowsinessStatus(DrowsinessLevel.valueOf(it)) }
                    statusStr?.let { updateFaceStatus(FaceStatus.valueOf(it)) }
                }
                SafetyMonitoringService.ACTION_ACCIDENT_DETECTED -> {
                    val accidentTypeStr = intent.getStringExtra(SafetyMonitoringService.EXTRA_ACCIDENT_TYPE)
                    val severity = intent.getStringExtra(SafetyMonitoringService.EXTRA_ACCIDENT_SEVERITY)

                    accidentTypeStr?.let {
                        updateAccidentStatus(AccidentType.valueOf(it), severity ?: "Unknown")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Keep screen on and show over lock screen for constant monitoring
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        initializeViews()
        preferencesManager = PreferencesManager(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        setupListeners()
        updateContactsList()
        requestPermissions()
        registerBroadcastReceiver()
    }

    override fun onResume() {
        super.onResume()
        if (isMonitoring && isServiceBound) {
            startCameraAnalysis()
        }
    }

    override fun onPause() {
        super.onPause()
        // Keep service running, just pause camera analysis if needed
    }

    private fun initializeViews() {
        cameraPreview = findViewById(R.id.cameraPreview)
        btnToggleMonitoring = findViewById(R.id.btnToggleMonitoring)
        btnViewHistory = findViewById(R.id.btnViewHistory)
        btnAddContact = findViewById(R.id.btnAddContact)
        drowsinessStatus = findViewById(R.id.drowsinessStatus)
        accidentStatus = findViewById(R.id.accidentStatus)
        contactsContainer = findViewById(R.id.contactsContainer)
        noContactsText = findViewById(R.id.noContactsText)
        faceOverlay = findViewById(R.id.faceOverlay)
    }

    private fun setupListeners() {
        btnToggleMonitoring.setOnClickListener {
            toggleMonitoring()
        }

        btnViewHistory.setOnClickListener {
            val intent = Intent(this, AlertHistoryActivity::class.java)
            startActivity(intent)
        }

        btnAddContact.setOnClickListener {
            showAddContactDialog()
        }
    }

    private fun requestPermissions() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.POST_NOTIFICATIONS
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "SafeRide needs these permissions to function properly",
                    "OK",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "You need to allow permissions in Settings",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    startCamera()
                } else {
                    Toast.makeText(this, "Permissions denied: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                bindCameraPreview(cameraProvider)
            } catch (e: Exception) {
                Toast.makeText(this, "Camera initialization failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        preview.setSurfaceProvider(cameraPreview.surfaceProvider)

        imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        } catch (e: Exception) {
            Toast.makeText(this, "Camera binding failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCameraAnalysis() {
        imageAnalysis?.setAnalyzer(cameraExecutor, CameraAnalyzer { bitmap ->
            monitoringService?.getDrowsinessDetector()?.analyzeFace(bitmap)
        })
    }

    private fun stopCameraAnalysis() {
        imageAnalysis?.clearAnalyzer()
        faceOverlay.updateFace(null, 0, 0)
    }

    private fun registerBroadcastReceiver() {
        val filter = IntentFilter().apply {
            addAction(SafetyMonitoringService.ACTION_DROWSINESS_UPDATE)
            addAction(SafetyMonitoringService.ACTION_ACCIDENT_DETECTED)
        }

        ContextCompat.registerReceiver(
            this,
            drowsinessReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    private fun updateAccidentStatus(accidentType: AccidentType, severity: String) {
        runOnUiThread {
            val text = when (accidentType) {
                AccidentType.MINOR_IMPACT -> "Minor Impact!"
                AccidentType.MODERATE_CRASH -> "Crash Detected!"
                AccidentType.SEVERE_CRASH -> "SEVERE CRASH!"
                AccidentType.FALL_WITH_IMPACT -> "Fall Detected!"
            }

            accidentStatus.text = text
            accidentStatus.setTextColor(ContextCompat.getColor(this, R.color.danger_red))

            // Show urgent alert dialog
            AlertDialog.Builder(this)
                .setTitle("⚠️ ACCIDENT DETECTED")
                .setMessage("$text\nSeverity: $severity\n\nEmergency contacts will be notified (Module 4).")
                .setPositiveButton("I'm OK") { dialog, _ ->
                    dialog.dismiss()
                    accidentStatus.text = getString(R.string.status_active)
                    accidentStatus.setTextColor(ContextCompat.getColor(this, R.color.safe_green))
                }
                .setCancelable(false)
                .show()

            Toast.makeText(this, getString(R.string.accident_alert), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateDrowsinessStatus(level: DrowsinessLevel) {
        runOnUiThread {
            val (text, color) = when (level) {
                DrowsinessLevel.NONE -> "Active" to R.color.safe_green
                DrowsinessLevel.LOW -> "Low Alert" to R.color.warning_yellow
                DrowsinessLevel.MEDIUM -> "Medium Alert" to R.color.warning_yellow
                DrowsinessLevel.HIGH -> "High Alert!" to R.color.danger_red
                DrowsinessLevel.CRITICAL -> "CRITICAL ALERT!" to R.color.danger_red
            }

            drowsinessStatus.text = text
            drowsinessStatus.setTextColor(ContextCompat.getColor(this, color))

            // Show popup alert for MEDIUM and above (eyes closed 3+ seconds)
            if (level == DrowsinessLevel.MEDIUM || level == DrowsinessLevel.HIGH || level == DrowsinessLevel.CRITICAL) {
                showDrowsinessAlertDialog(level)
            }
        }
    }

    private fun showDrowsinessAlertDialog(level: DrowsinessLevel) {
        if (activeAlertDialog?.isShowing == true) return

        val alertMessage = when (level) {
            DrowsinessLevel.MEDIUM -> "⚠️ DROWSINESS DETECTED!\n\nYour eyes were closed for 3+ seconds.\n\nPlease take a break immediately!"
            DrowsinessLevel.HIGH -> "🚨 HIGH DROWSINESS ALERT!\n\nYou are showing signs of severe drowsiness.\n\nSTOP and rest NOW!"
            DrowsinessLevel.CRITICAL -> "🆘 CRITICAL DROWSINESS!\n\nYou are extremely drowsy!\n\nSTOP RIDING IMMEDIATELY!"
            else -> "Drowsiness detected. Please be careful."
        }

        activeAlertDialog = AlertDialog.Builder(this)
            .setTitle("⚠️ DROWSINESS ALERT")
            .setMessage(alertMessage)
            .setPositiveButton("I'm Awake Now") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun updateFaceStatus(status: FaceStatus) {
        runOnUiThread {
            if (status == FaceStatus.NO_FACE) {
                if (isMonitoring) {
                    drowsinessStatus.text = "No Face Detected"
                    drowsinessStatus.setTextColor(ContextCompat.getColor(this, R.color.warning_yellow))
                }
                faceOverlay.updateFace(null, 0, 0)
            }
        }
    }

    private fun toggleMonitoring() {
        if (preferencesManager.getEmergencyContacts().isEmpty()) {
            Toast.makeText(this, "Please add at least one emergency contact", Toast.LENGTH_SHORT).show()
            return
        }

        isMonitoring = !isMonitoring

        if (isMonitoring) {
            startMonitoringService()
            btnToggleMonitoring.text = getString(R.string.stop_monitoring)
            drowsinessStatus.text = getString(R.string.status_active)
            drowsinessStatus.setTextColor(ContextCompat.getColor(this, R.color.safe_green))
            accidentStatus.text = getString(R.string.status_active)
            accidentStatus.setTextColor(ContextCompat.getColor(this, R.color.safe_green))
        } else {
            stopMonitoringService()
            btnToggleMonitoring.text = getString(R.string.start_monitoring)
            drowsinessStatus.text = getString(R.string.status_inactive)
            drowsinessStatus.setTextColor(ContextCompat.getColor(this, R.color.safe_green))
            accidentStatus.text = getString(R.string.status_inactive)
            accidentStatus.setTextColor(ContextCompat.getColor(this, R.color.safe_green))
        }
    }

    private fun startMonitoringService() {
        val intent = Intent(this, SafetyMonitoringService::class.java)
        ContextCompat.startForegroundService(this, intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun stopMonitoringService() {
        stopCameraAnalysis()

        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
        }

        val intent = Intent(this, SafetyMonitoringService::class.java)
        stopService(intent)
    }

    private fun showAddContactDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null)
        val editName = dialogView.findViewById<EditText>(R.id.editContactName)
        val editNumber = dialogView.findViewById<EditText>(R.id.editContactNumber)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.add_contact))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val name = editName.text.toString().trim()
                val number = editNumber.text.toString().trim()

                if (name.isNotEmpty() && number.isNotEmpty()) {
                    val contact = EmergencyContact(name, number)
                    preferencesManager.addEmergencyContact(contact)
                    updateContactsList()
                    Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun updateContactsList() {
        val contacts = preferencesManager.getEmergencyContacts()
        contactsContainer.removeAllViews()

        if (contacts.isEmpty()) {
            contactsContainer.addView(noContactsText)
        } else {
            contacts.forEach { contact ->
                val contactView = LayoutInflater.from(this)
                    .inflate(R.layout.item_contact, contactsContainer, false)

                contactView.findViewById<TextView>(R.id.contactName).text = contact.name
                contactView.findViewById<TextView>(R.id.contactNumber).text = contact.phoneNumber

                contactView.findViewById<Button>(R.id.btnRemoveContact).setOnClickListener {
                    preferencesManager.removeEmergencyContact(contact)
                    updateContactsList()
                    Toast.makeText(this, "Contact removed", Toast.LENGTH_SHORT).show()
                }

                contactsContainer.addView(contactView)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(drowsinessReceiver)
        } catch (e: Exception) {
            // Receiver not registered
        }

        if (isServiceBound) {
            unbindService(serviceConnection)
        }

        cameraExecutor.shutdown()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_help -> {
                val intent = Intent(this, HelpActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
