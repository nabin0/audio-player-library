package com.github.nabin0.audioplayer.events

import androidx.media3.common.MediaMetadata

class AudioPlayerEventBus {
    private val listeners = ArrayList<AudioPlayerEventListener>()

    fun registerReceiver(audioPlayerEventListener: AudioPlayerEventListener) {
        listeners.add(audioPlayerEventListener)
    }

    fun unregisterReceiver(audioPlayerEventListener: AudioPlayerEventListener) {
        listeners.remove(audioPlayerEventListener)
    }

    fun triggerPlayEvent() {
        for (listener in listeners)
            listener.onPlay()
    }

    fun triggerPauseEvent() {
        for (listener in listeners)
            listener.onPause()
    }

    fun triggerOnBuffer() {
        for (listener in listeners)
            listener.onBuffer()
    }

    fun triggerOnReady() {
        for (listener in listeners)
            listener.onReady()
    }

    fun triggerOnMetadataChanged(mediaMetadata: MediaMetadata) {
        for (listener in listeners)
            listener.onMetadataChanged(mediaMetadata)
    }

    fun onUpdateSeekBar(currentProgress: Long, duration: Long) {
        for (listener in listeners)
            listener.onUpdateSeekbar(currentProgress, duration)
    }

    companion object {
        val instance = AudioPlayerEventBus()
    }
}