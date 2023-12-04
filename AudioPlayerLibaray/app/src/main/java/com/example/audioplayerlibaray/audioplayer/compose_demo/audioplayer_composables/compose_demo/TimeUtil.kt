package com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.compose_demo

object TimeUtil {
    fun formatTime(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}