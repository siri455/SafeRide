package com.saferide.app.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark

class FaceOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val facePaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val eyePaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }

    private var face: Face? = null
    private var previewWidth: Int = 0
    private var previewHeight: Int = 0

    fun updateFace(face: Face?, previewWidth: Int, previewHeight: Int) {
        this.face = face
        this.previewWidth = previewWidth
        this.previewHeight = previewHeight
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val face = this.face ?: return
        if (previewWidth == 0 || previewHeight == 0) return

        // Calculate scale factors
        val scaleX = width.toFloat() / previewWidth
        val scaleY = height.toFloat() / previewHeight

        // Draw face as an ellipse adjacent to the face
        val bounds = face.boundingBox

        // Mirror adjustments for front camera if needed
        // The analyzer already mirrors the bitmap, so bounds should be correct for the preview.

        val mappedBounds = RectF(
            bounds.left * scaleX,
            bounds.top * scaleY,
            bounds.right * scaleX,
            bounds.bottom * scaleY
        )
        
        // Draw the ellipse
          canvas.drawRect(mappedBounds, facePaint)

        // Draw eyes as circles
        face.getLandmark(FaceLandmark.LEFT_EYE)?.let { landmark ->
            val pos = landmark.position
            canvas.drawCircle(pos.x * scaleX, pos.y * scaleY, 15f * scaleX, eyePaint)
        }

        face.getLandmark(FaceLandmark.RIGHT_EYE)?.let { landmark ->
            val pos = landmark.position
            canvas.drawCircle(pos.x * scaleX, pos.y * scaleY, 15f * scaleX, eyePaint)
        }
    }
}
