package com.saferide.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saferide.app.models.EmergencyContact

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "SafeRidePrefs"
        private const val KEY_EMERGENCY_CONTACTS = "emergency_contacts"
        private const val KEY_DROWSINESS_ENABLED = "drowsiness_enabled"
        private const val KEY_ACCIDENT_ENABLED = "accident_enabled"
        private const val KEY_SENSITIVITY = "sensitivity"
    }

    fun saveEmergencyContacts(contacts: List<EmergencyContact>) {
        val json = gson.toJson(contacts)
        prefs.edit().putString(KEY_EMERGENCY_CONTACTS, json).apply()
    }

    fun getEmergencyContacts(): List<EmergencyContact> {
        val json = prefs.getString(KEY_EMERGENCY_CONTACTS, null) ?: return emptyList()
        val type = object : TypeToken<List<EmergencyContact>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addEmergencyContact(contact: EmergencyContact) {
        val contacts = getEmergencyContacts().toMutableList()
        contacts.add(contact)
        saveEmergencyContacts(contacts)
    }

    fun removeEmergencyContact(contact: EmergencyContact) {
        val contacts = getEmergencyContacts().toMutableList()
        contacts.remove(contact)
        saveEmergencyContacts(contacts)
    }

    var drowsinessDetectionEnabled: Boolean
        get() = prefs.getBoolean(KEY_DROWSINESS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_DROWSINESS_ENABLED, value).apply()

    var accidentDetectionEnabled: Boolean
        get() = prefs.getBoolean(KEY_ACCIDENT_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_ACCIDENT_ENABLED, value).apply()

    var sensitivityLevel: Int
        get() = prefs.getInt(KEY_SENSITIVITY, 50)
        set(value) = prefs.edit().putInt(KEY_SENSITIVITY, value).apply()
}
