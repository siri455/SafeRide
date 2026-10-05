package com.saferide.app.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import com.saferide.app.detectors.DrowsinessLevel

class AlertManager(private val context: Context) {

    private var vibrator: Vibrator? = null
    private var mediaPlayer: MediaPlayer? = null
    private var currentAlertLevel: DrowsinessLevel = DrowsinessLevel.NONE
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        initializeVibrator()
    }

    private fun initializeVibrator() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun triggerDrowsinessAlert(level: DrowsinessLevel) {
        if (level == currentAlertLevel) return
        
        Log.i(TAG, "Changing Alert Level: $level")
        currentAlertLevel = level

        when (level) {
            DrowsinessLevel.NONE, DrowsinessLevel.LOW -> {
                stopAll()
            }
            DrowsinessLevel.MEDIUM -> {
                vibratePhone(longArrayOf(0, 500, 200, 500), true)
                playAlertSound(0.7f, true)
            }
            DrowsinessLevel.HIGH -> {
                vibratePhone(longArrayOf(0, 800, 200, 800), true)
                playAlertSound(0.9f, true)
            }
            DrowsinessLevel.CRITICAL -> {
                vibratePhone(longArrayOf(0, 1000, 100, 1000), true)
                playAlertSound(1.0f, true)
            }
        }
    }

    private fun vibratePhone(pattern: LongArray, repeat: Boolean) {
        vibrator?.let { v ->
            if (v.hasVibrator()) {
                v.cancel()
                val repeatIndex = if (repeat) 0 else -1
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createWaveform(pattern, repeatIndex))
                } else {
                    @Suppress("DEPRECATION")
                    v.vibrate(pattern, repeatIndex)
                }
            }
        }
    }

    private fun playAlertSound(volume: Float, loop: Boolean) {
        mainHandler.post {
            try {
                stopAlertSound()
                
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                
                // Boost Music stream volume for safety (more consistent than Alarm stream)
                val maxMusicVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (maxMusicVol * 0.85).toInt(), 0)

                val alertUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

                mediaPlayer = MediaPlayer().apply {
                    setDataSource(context, alertUri)
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build()
                    )
                    setVolume(volume, volume)
                    isLooping = loop
                    prepare()
                    start()
                }
                Log.d(TAG, "Alert sound started via STREAM_MUSIC")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play sound: ${e.message}")
                playFallbackSound(volume)
            }
        }
    }

    private fun playFallbackSound(volume: Float) {
        try {
            val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            mediaPlayer = MediaPlayer.create(context, notificationUri)
            mediaPlayer?.setVolume(volume, volume)
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e(TAG, "Fallback sound also failed")
        }
    }

    fun stopVibration() {
        vibrator?.cancel()
    }

    fun stopAlertSound() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) it.stop()
                it.release()
            }
            mediaPlayer = null
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun stopAll() {
        if (currentAlertLevel != DrowsinessLevel.NONE && currentAlertLevel != DrowsinessLevel.LOW) {
            Log.d(TAG, "Stopping all alerts")
        }
        currentAlertLevel = DrowsinessLevel.NONE
        stopVibration()
        stopAlertSound()
    }

    companion object {
        private const val TAG = "AlertManager"
    }
}
