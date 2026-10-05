package com.saferide.app.detectors

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class DrowsinessDetector(private val context: Context) {

    private var faceDetector: FaceDetector
    private val earThreshold = 0.4f // Increased sensitivity as requested
    
    private var eyesClosedStartTime = 0L
    private val drowsinessThresholdMs = 2500L // 2.5 seconds
    
    private var onDrowsinessDetected: ((DrowsinessLevel) -> Unit)? = null
    private var onFaceStatus: ((FaceStatus) -> Unit)? = null
    private var onFaceDetectionResult: ((Face, Int, Int) -> Unit)? = null

    init {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE) // More accurate for better results
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.1f) // Detect faces further away
            .enableTracking() // Maintain identity across frames
            .build()

        faceDetector = FaceDetection.getClient(options)
    }

    fun setDrowsinessListener(listener: (DrowsinessLevel) -> Unit) {
        this.onDrowsinessDetected = listener
    }

    fun setFaceStatusListener(listener: (FaceStatus) -> Unit) {
        this.onFaceStatus = listener
    }

    fun setFaceDetectionListener(listener: (Face, Int, Int) -> Unit) {
        this.onFaceDetectionResult = listener
    }

    fun analyzeFace(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val width = bitmap.width
        val height = bitmap.height

        faceDetector.process(image)
            .addOnSuccessListener { faces ->
                processFaces(faces, width, height)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Face detection failed: ${e.message}")
                onFaceStatus?.invoke(FaceStatus.NO_FACE)
            }
    }

    private fun processFaces(faces: List<Face>, width: Int, height: Int) {
        if (faces.isEmpty()) {
            onFaceStatus?.invoke(FaceStatus.NO_FACE)
            resetCounters()
            return
        }

        val face = faces[0]
        onFaceStatus?.invoke(FaceStatus.FACE_DETECTED)
        onFaceDetectionResult?.invoke(face, width, height)

        val leftEyeOpenProb = face.leftEyeOpenProbability ?: -1.0f
        val rightEyeOpenProb = face.rightEyeOpenProbability ?: -1.0f
        
        if (leftEyeOpenProb == -1.0f || rightEyeOpenProb == -1.0f) {
            return
        }

        val avgEyeOpen = (leftEyeOpenProb + rightEyeOpenProb) / 2.0f
        checkDrowsiness(avgEyeOpen)
    }

    private fun checkDrowsiness(avgEyeOpen: Float) {
        val currentTime = System.currentTimeMillis()
        
        if (avgEyeOpen < earThreshold) {
            if (eyesClosedStartTime == 0L) {
                eyesClosedStartTime = currentTime
                onDrowsinessDetected?.invoke(DrowsinessLevel.LOW)
            } else {
                val duration = currentTime - eyesClosedStartTime
                if (duration >= drowsinessThresholdMs) {
                    val level = when {
                        duration > 8000 -> DrowsinessLevel.CRITICAL
                        duration > 5000 -> DrowsinessLevel.HIGH
                        else -> DrowsinessLevel.MEDIUM
                    }
                    onDrowsinessDetected?.invoke(level)
                }
            }
        } else {
            if (eyesClosedStartTime != 0L) {
                eyesClosedStartTime = 0L
                onDrowsinessDetected?.invoke(DrowsinessLevel.NONE)
            }
        }
    }

    private fun resetCounters() {
        if (eyesClosedStartTime != 0L) {
            eyesClosedStartTime = 0L
            onDrowsinessDetected?.invoke(DrowsinessLevel.NONE)
        }
    }

    fun release() {
        faceDetector.close()
    }

    companion object {
        private const val TAG = "DrowsinessDetector"
    }
}

enum class DrowsinessLevel {
    NONE,
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class FaceStatus {
    NO_FACE,
    FACE_DETECTED
}
