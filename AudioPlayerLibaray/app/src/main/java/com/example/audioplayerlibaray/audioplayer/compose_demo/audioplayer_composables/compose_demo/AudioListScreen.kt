package com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.compose_demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.AudioPlayerViewModel

@Composable
fun AudioListScreen(
    audioPlayerViewModel: AudioPlayerViewModel,
    navigateToFullScreen: (index: Int) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        AudioListWithMiniPlayer(audioPlayerViewModel = audioPlayerViewModel,
            navigateToFullScreen  = navigateToFullScreen)
    }

}