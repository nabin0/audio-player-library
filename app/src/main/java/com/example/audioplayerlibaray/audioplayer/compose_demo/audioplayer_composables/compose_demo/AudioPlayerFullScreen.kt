package com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.compose_demo

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.audioplayerlibaray.R
import com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.AudioPlayerViewModel
import com.github.nabin0.audioplayer.view.AudioPlayer

@Composable
fun AudioPlayerFullScreen(
    audioId: Int,
    audioPlayerViewModel: AudioPlayerViewModel,
) {
    var audioPlayer: AudioPlayer? = null

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer?.removeCallbacks()
        }
    }
    val mContext = LocalContext.current as Activity
    Surface(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = {
                AudioPlayer(mContext).apply {
                    audioPlayer = this;
                }
            },
            update = {
//                audioPlayer?.setCustomLayout(R.layout.audio_full_screen_player_custom_layout)
                audioPlayer?.setChromecastEnabled(true)
                audioPlayer?.prepare()
                audioPlayer?.setAutoPlayEnabled(true)
                if (audioPlayerViewModel.currentPlayingAudioIndex.value != audioId) {
                    audioPlayer?.playMediaItemByIndex(audioId)
                }
                audioPlayer?.startForegroundService()
                audioPlayerViewModel.showMiniPlayer = true
            }
        )
    }

}