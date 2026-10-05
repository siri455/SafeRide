package com.saferide.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.saferide.app.models.AccidentLog
import com.saferide.app.models.DrowsinessLog
import com.saferide.app.utils.LogManager

class AlertHistoryActivity : AppCompatActivity() {

    private lateinit var logManager: LogManager
    private lateinit var accidentLogsContainer: LinearLayout
    private lateinit var drowsinessLogsContainer: LinearLayout
    private lateinit var statsContainer: LinearLayout
    private lateinit var noAccidentsText: TextView
    private lateinit var noDrowsinessText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Alert History"

        logManager = LogManager(this)

        initializeViews()
        loadStatistics()
        loadAccidentLogs()
        loadDrowsinessLogs()
        setupClearButton()
    }

    private fun initializeViews() {
        accidentLogsContainer = findViewById(R.id.accidentLogsContainer)
        drowsinessLogsContainer = findViewById(R.id.drowsinessLogsContainer)
        statsContainer = findViewById(R.id.statsContainer)
        noAccidentsText = findViewById(R.id.noAccidentsText)
        noDrowsinessText = findViewById(R.id.noDrowsinessText)
    }

    private fun loadStatistics() {
        val totalAccidents = logManager.getTotalAccidents()
        val totalDrowsiness = logManager.getTotalDrowsiness()
        val totalRideTime = logManager.getTotalRideTime()

        findViewById<TextView>(R.id.totalAccidentsText).text = totalAccidents.toString()
        findViewById<TextView>(R.id.totalDrowsinessText).text = totalDrowsiness.toString()
        findViewById<TextView>(R.id.totalRideTimeText).text = logManager.formatDuration(totalRideTime)
    }

    private fun loadAccidentLogs() {
        accidentLogsContainer.removeAllViews()
        val logs = logManager.getAccidentLogs().sortedByDescending { it.timestamp }

        if (logs.isEmpty()) {
            accidentLogsContainer.addView(noAccidentsText)
        } else {
            logs.forEach { log ->
                addAccidentLogView(log)
            }
        }
    }

    private fun loadDrowsinessLogs() {
        drowsinessLogsContainer.removeAllViews()
        val logs = logManager.getDrowsinessLogs().sortedByDescending { it.timestamp }

        if (logs.isEmpty()) {
            drowsinessLogsContainer.addView(noDrowsinessText)
        } else {
            logs.take(20).forEach { log -> // Show last 20
                addDrowsinessLogView(log)
            }
        }
    }

    private fun addAccidentLogView(log: AccidentLog) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_accident_log, accidentLogsContainer, false)

        view.findViewById<TextView>(R.id.accidentDateTime).text = log.dateTime
        view.findViewById<TextView>(R.id.accidentType).text = log.accidentType
        view.findViewById<TextView>(R.id.accidentLocation).text = "📍 ${log.location}"
        view.findViewById<TextView>(R.id.accidentSeverity).apply {
            text = log.severity
            setTextColor(ContextCompat.getColor(context, when(log.severity) {
                "Severe" -> R.color.danger_red
                "Moderate" -> R.color.warning_yellow
                else -> R.color.safe_green
            }))
        }

        val statusText = view.findViewById<TextView>(R.id.accidentStatus)
        if (log.acknowledged) {
            statusText.text = "✓ Acknowledged"
            statusText.setTextColor(ContextCompat.getColor(this, R.color.safe_green))
        } else {
            statusText.text = "⚠ Not Acknowledged"
            statusText.setTextColor(ContextCompat.getColor(this, R.color.danger_red))
        }

        view.setOnClickListener {
            showAccidentDetails(log)
        }

        accidentLogsContainer.addView(view)
    }

    private fun addDrowsinessLogView(log: DrowsinessLog) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_drowsiness_log, drowsinessLogsContainer, false)

        view.findViewById<TextView>(R.id.drowsinessDateTime).text = log.dateTime
        view.findViewById<TextView>(R.id.drowsinessLevel).apply {
            text = log.level
            setTextColor(ContextCompat.getColor(context, when(log.level) {
                "CRITICAL", "HIGH" -> R.color.danger_red
                "MEDIUM" -> R.color.warning_yellow
                else -> R.color.safe_green
            }))
        }

        drowsinessLogsContainer.addView(view)
    }

    private fun showAccidentDetails(log: AccidentLog) {
        AlertDialog.Builder(this)
            .setTitle("Accident Details")
            .setMessage("""
                Date & Time: ${log.dateTime}
                Type: ${log.accidentType}
                Severity: ${log.severity}
                Location: ${log.location}
                Status: ${if (log.acknowledged) "Acknowledged" else "Not Acknowledged"}
            """.trimIndent())
            .setPositiveButton("Acknowledge") { dialog, _ ->
                logManager.acknowledgeAccident(log.timestamp)
                loadAccidentLogs()
                loadStatistics()
                dialog.dismiss()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun setupClearButton() {
        findViewById<Button>(R.id.btnClearLogs).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear All Logs?")
                .setMessage("This will permanently delete all accident and drowsiness logs. This action cannot be undone.")
                .setPositiveButton("Clear") { dialog, _ ->
                    logManager.clearAllLogs()
                    loadStatistics()
                    loadAccidentLogs()
                    loadDrowsinessLogs()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
