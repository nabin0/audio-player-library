package com.github.nabin0.audioplayer.events

import androidx.media3.common.MediaMetadata

interface AudioPlayerEventListener {
    fun onBuffer()
    fun onReady()
    fun onMetadataChanged(mediaMetadata: MediaMetadata)
    fun onUpdateSeekbar(currentPosition: Long, totalDuration: Long)
    fun onPlay()
    fun onPause()
}