package com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.compose_demo

import androidx.fragment.app.FragmentActivity
import com.github.nabin0.audioplayer.view.AudioPlayer

object AudioPlayerProvider {

    private var audioPLayer: AudioPlayer? = null

    fun getSimpleAudioPlayer(context: FragmentActivity) {
        audioPLayer = audioPLayer ?: AudioPlayer(context = context).apply {
            initializePlayer()
        }

    }
}