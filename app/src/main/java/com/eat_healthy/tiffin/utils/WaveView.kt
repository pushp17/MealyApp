package com.eat_healthy.tiffin.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class WaveView(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs) {
    private val holder: SurfaceHolder
    private val paint: Paint
    private val centerX = 0f
    private val centerY = 0f
    private var radius = 0f
    private var isExpanding = false

    init {
        holder = getHolder()
        paint = Paint()
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
    }

    fun update() {
        if (isExpanding) {
            radius += 5f
            if (radius >= 200) {
                isExpanding = false
            }
        } else {
            radius -= 5f
            if (radius <= 0) {
                isExpanding = true
            }
        }
    }

    fun draw() {
        val canvas = holder.lockCanvas()
        if (canvas != null) {
            canvas.drawColor(Color.WHITE)
            canvas.drawCircle(centerX, centerY, radius, paint)
            holder.unlockCanvasAndPost(canvas)
        }
    }
}