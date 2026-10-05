package com.saferide.app.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saferide.app.models.AccidentLog
import com.saferide.app.models.DrowsinessLog
import com.saferide.app.models.SessionLog
import java.text.SimpleDateFormat
import java.util.*

class LogManager(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    companion object {
        private const val PREFS_NAME = "SafeRideLogData"
        private const val KEY_ACCIDENT_LOGS = "accident_logs"
        private const val KEY_DROWSINESS_LOGS = "drowsiness_logs"
        private const val KEY_SESSION_LOGS = "session_logs"
        private const val KEY_CURRENT_SESSION = "current_session"
        private const val KEY_TOTAL_ACCIDENTS = "total_accidents"
        private const val KEY_TOTAL_DROWSINESS = "total_drowsiness"
        private const val KEY_TOTAL_RIDE_TIME = "total_ride_time"
    }

    // Accident Logs
    fun logAccident(accidentType: String, severity: String, location: String = "Location unavailable") {
        val logs = getAccidentLogs().toMutableList()
        val log = AccidentLog(
            timestamp = System.currentTimeMillis(),
            dateTime = dateFormat.format(Date()),
            accidentType = accidentType,
            severity = severity,
            location = location
        )
        logs.add(log)
        saveAccidentLogs(logs)
        incrementTotalAccidents()
    }

    fun getAccidentLogs(): List<AccidentLog> {
        val json = prefs.getString(KEY_ACCIDENT_LOGS, null) ?: return emptyList()
        val type = object : TypeToken<List<AccidentLog>>() {}.type
        return gson.fromJson(json, type)
    }

    fun acknowledgeAccident(timestamp: Long) {
        val logs = getAccidentLogs().toMutableList()
        val index = logs.indexOfFirst { it.timestamp == timestamp }
        if (index != -1) {
            logs[index] = logs[index].copy(acknowledged = true)
            saveAccidentLogs(logs)
        }
    }

    fun clearAccidentLogs() {
        prefs.edit().remove(KEY_ACCIDENT_LOGS).apply()
    }

    private fun saveAccidentLogs(logs: List<AccidentLog>) {
        val json = gson.toJson(logs)
        prefs.edit().putString(KEY_ACCIDENT_LOGS, json).commit()
    }

    // Drowsiness Logs
    fun logDrowsiness(level: String, duration: Long = 0L) {
        val logs = getDrowsinessLogs().toMutableList()
        val log = DrowsinessLog(
            timestamp = System.currentTimeMillis(),
            dateTime = dateFormat.format(Date()),
            level = level,
            duration = duration
        )
        logs.add(log)
        saveDrowsinessLogs(logs)
        incrementTotalDrowsiness()
    }

    fun getDrowsinessLogs(): List<DrowsinessLog> {
        val json = prefs.getString(KEY_DROWSINESS_LOGS, null) ?: return emptyList()
        val type = object : TypeToken<List<DrowsinessLog>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearDrowsinessLogs() {
        prefs.edit().remove(KEY_DROWSINESS_LOGS).apply()
    }

    private fun saveDrowsinessLogs(logs: List<DrowsinessLog>) {
        val json = gson.toJson(logs)
        prefs.edit().putString(KEY_DROWSINESS_LOGS, json).commit()
    }

    // Session Management
    fun startSession(): String {
        val sessionId = "session_${System.currentTimeMillis()}"
        val session = SessionLog(
            sessionId = sessionId,
            startTime = System.currentTimeMillis(),
            endTime = 0L,
            duration = 0L,
            accidentCount = 0,
            drowsinessCount = 0
        )
        val json = gson.toJson(session)
        prefs.edit().putString(KEY_CURRENT_SESSION, json).apply()
        return sessionId
    }

    fun endSession() {
        val currentSessionJson = prefs.getString(KEY_CURRENT_SESSION, null) ?: return
        val session = gson.fromJson(currentSessionJson, SessionLog::class.java)

        val endTime = System.currentTimeMillis()
        val duration = endTime - session.startTime

        val accidentCount = getAccidentLogsSince(session.startTime).size
        val drowsinessCount = getDrowsinessLogsSince(session.startTime).size

        val completedSession = session.copy(
            endTime = endTime,
            duration = duration,
            accidentCount = accidentCount,
            drowsinessCount = drowsinessCount
        )

        saveCompletedSession(completedSession)
        addToTotalRideTime(duration)
        prefs.edit().remove(KEY_CURRENT_SESSION).apply()
    }

    fun getCurrentSession(): SessionLog? {
        val json = prefs.getString(KEY_CURRENT_SESSION, null) ?: return null
        return gson.fromJson(json, SessionLog::class.java)
    }

    fun getSessionLogs(): List<SessionLog> {
        val json = prefs.getString(KEY_SESSION_LOGS, null) ?: return emptyList()
        val type = object : TypeToken<List<SessionLog>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearSessionLogs() {
        prefs.edit().remove(KEY_SESSION_LOGS).apply()
    }

    private fun saveCompletedSession(session: SessionLog) {
        val sessions = getSessionLogs().toMutableList()
        sessions.add(session)
        val json = gson.toJson(sessions)
        prefs.edit().putString(KEY_SESSION_LOGS, json).apply()
    }

    private fun getAccidentLogsSince(timestamp: Long): List<AccidentLog> {
        return getAccidentLogs().filter { it.timestamp >= timestamp }
    }

    private fun getDrowsinessLogsSince(timestamp: Long): List<DrowsinessLog> {
        return getDrowsinessLogs().filter { it.timestamp >= timestamp }
    }

    // Statistics
    private fun incrementTotalAccidents() {
        val current = prefs.getInt(KEY_TOTAL_ACCIDENTS, 0)
        prefs.edit().putInt(KEY_TOTAL_ACCIDENTS, current + 1).apply()
    }

    private fun incrementTotalDrowsiness() {
        val current = prefs.getInt(KEY_TOTAL_DROWSINESS, 0)
        prefs.edit().putInt(KEY_TOTAL_DROWSINESS, current + 1).apply()
    }

    private fun addToTotalRideTime(duration: Long) {
        val current = prefs.getLong(KEY_TOTAL_RIDE_TIME, 0L)
        prefs.edit().putLong(KEY_TOTAL_RIDE_TIME, current + duration).apply()
    }

    fun getTotalAccidents(): Int {
        return prefs.getInt(KEY_TOTAL_ACCIDENTS, 0)
    }

    fun getTotalDrowsiness(): Int {
        return prefs.getInt(KEY_TOTAL_DROWSINESS, 0)
    }

    fun getTotalRideTime(): Long {
        return prefs.getLong(KEY_TOTAL_RIDE_TIME, 0L)
    }

    fun clearAllLogs() {
        prefs.edit().clear().apply()
    }

    fun formatDuration(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return when {
            hours > 0 -> String.format("%dh %dm", hours, minutes % 60)
            minutes > 0 -> String.format("%dm %ds", minutes, seconds % 60)
            else -> String.format("%ds", seconds)
        }
    }
}
