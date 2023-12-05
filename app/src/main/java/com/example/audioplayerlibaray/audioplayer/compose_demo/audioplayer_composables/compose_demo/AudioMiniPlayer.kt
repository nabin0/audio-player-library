package com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.compose_demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.audioplayerlibaray.R
import com.github.nabin0.audioplayer.view.AudioMiniPlayer

@Composable
fun AudioMiniPlayer(modifier: Modifier = Modifier) {

    var audioMiniPlayer: AudioMiniPlayer? = null
    Column(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.primaryVariant)) {
        DisposableEffect(Unit) {
            onDispose {
                audioMiniPlayer?.removeCallbacks()
            }
        }
        AndroidView(
            modifier = Modifier
                .fillMaxWidth(),
            factory = { context ->
                AudioMiniPlayer(context).apply {
                    audioMiniPlayer = this
                }
            },
            update = {
                 audioMiniPlayer?.setCustomLayout(R.layout.audio_mini_player_custom_layout)
            }
        )
    }
}