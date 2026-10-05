package com.saferide.app.models

data class AccidentLog(
    val timestamp: Long,
    val dateTime: String,
    val accidentType: String,
    val severity: String,
    val acknowledged: Boolean = false,
    val notes: String = "",
    val location: String = "Location unavailable"
)

data class DrowsinessLog(
    val timestamp: Long,
    val dateTime: String,
    val level: String,
    val duration: Long = 0L
)

data class SessionLog(
    val sessionId: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Long,
    val accidentCount: Int,
    val drowsinessCount: Int
)
