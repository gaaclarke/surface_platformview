package com.example.platformview_example

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Choreographer
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import io.flutter.plugin.platform.PlatformView

internal class NativeView(context: Context, id: Int, creationParams: Map<String?, Any?>?) :
    PlatformView, SurfaceHolder.Callback, Choreographer.FrameCallback {
    private val surfaceView: SurfaceView
    private var startTimeNanos: Long = 0
    private val animationDurationNanos: Long = 1_000_000_000 // 1 second

    override fun getView(): View {
        return surfaceView
    }

    override fun dispose() {
        Choreographer.getInstance().removeFrameCallback(this)
    }

    init {
        surfaceView = SurfaceView(context)
        surfaceView.holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startTimeNanos = 0
        Choreographer.getInstance().postFrameCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Do nothing
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Choreographer.getInstance().removeFrameCallback(this)
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (startTimeNanos == 0L) {
            startTimeNanos = frameTimeNanos
        }

        val elapsedTimeNanos = frameTimeNanos - startTimeNanos
        val progress = (elapsedTimeNanos % animationDurationNanos) / animationDurationNanos.toFloat()

        draw(progress)
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun draw(progress: Float) {
        val holder = surfaceView.holder
        val canvas: Canvas? = holder.lockCanvas()
        if (canvas != null) {
            try {
                // Interpolate red component from 255 down to 0
                val red = (255 * (1.0f - progress)).toInt()
                val color = Color.rgb(red, 0, 0)
                canvas.drawColor(color)
            } finally {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}