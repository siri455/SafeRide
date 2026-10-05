package com.saferide.app.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.saferide.app.models.EmergencyContact
import com.saferide.app.sensors.AccidentType

class EmergencyAlertManager(private val context: Context) {

    private val preferencesManager = PreferencesManager(context)
    private val logManager = LogManager(context)

    fun handleEmergency(accidentType: AccidentType, severity: String) {
        fetchLocationAndExecute(accidentType, severity)
    }

    private fun fetchLocationAndExecute(accidentType: AccidentType, severity: String) {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            val locationText = "Location: Permission not granted"
            logAndSend(accidentType, severity, locationText)
            return
        }

        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            @SuppressLint("MissingPermission")
            val locationTask = fusedLocationClient.lastLocation
            locationTask.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val locationText = "https://maps.google.com/?q=${location.latitude},${location.longitude} (Lat: ${location.latitude}, Lng: ${location.longitude})"
                    logAndSend(accidentType, severity, locationText)
                } else {
                    fetchLocationFromManager(accidentType, severity)
                }
            }.addOnFailureListener {
                fetchLocationFromManager(accidentType, severity)
            }
        } catch (e: Exception) {
            logAndSend(accidentType, severity, "Location unavailable")
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocationFromManager(accidentType: AccidentType, severity: String) {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            val bestLocation = when {
                gpsLocation != null && netLocation != null -> {
                    if (gpsLocation.time > netLocation.time) gpsLocation else netLocation
                }
                gpsLocation != null -> gpsLocation
                else -> netLocation
            }

            val locationText = if (bestLocation != null) {
                "https://maps.google.com/?q=${bestLocation.latitude},${bestLocation.longitude} (Lat: ${bestLocation.latitude}, Lng: ${bestLocation.longitude})"
            } else {
                "Location unavailable (offline)"
            }

            logAndSend(accidentType, severity, locationText)
        } catch (e: Exception) {
            logAndSend(accidentType, severity, "Location unavailable")
        }
    }

    private fun logAndSend(accidentType: AccidentType, severity: String, locationText: String) {
        // 1. Log accident with GPS location regardless of SMS success/failure
        logManager.logAccident(accidentType.name, severity, locationText)

        // 2. Send SMS to emergency contacts
        sendSmsToContacts(accidentType, severity, locationText)
    }

    private fun sendSmsToContacts(accidentType: AccidentType, severity: String, locationText: String) {
        val contacts = preferencesManager.getEmergencyContacts()
        if (contacts.isEmpty()) {
            Log.w(TAG, "No emergency contacts registered. Cannot send SMS.")
            return
        }

        val hasSmsPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasSmsPermission) {
            Log.e(TAG, "SEND_SMS permission not granted. Cannot send emergency SMS.")
            return
        }

        val typeDescription = when (accidentType) {
            AccidentType.MINOR_IMPACT -> "Minor Impact"
            AccidentType.MODERATE_CRASH -> "Moderate Crash"
            AccidentType.SEVERE_CRASH -> "SEVERE CRASH"
            AccidentType.FALL_WITH_IMPACT -> "Fall Detected"
        }

        val message = "🚨 EMERGENCY ALERT! SafeRide detected a $typeDescription (Severity: $severity).\nLocation: $locationText\nPlease help immediately!"

        try {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }

            for (contact in contacts) {
                val phoneNumber = contact.phoneNumber
                if (phoneNumber.isNotEmpty()) {
                    try {
                        val parts = smsManager.divideMessage(message)
                        if (parts.size > 1) {
                            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
                        } else {
                            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                        }
                        Log.i(TAG, "Emergency SMS sent to ${contact.name} ($phoneNumber)")
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to send SMS to ${contact.phoneNumber}: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing SmsManager: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "EmergencyAlertManager"
    }
}
