package com.example.audioplayerlibaray.audioplayer.compose_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.AudioPlayerViewModel
import com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.compose_demo.AudioListWithMiniPlayer
import com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.compose_demo.AudioPlayerFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposePlayerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                val audioPlayerViewModel: AudioPlayerViewModel = hiltViewModel()
                val navHostController = rememberNavController()
                NavHost(
                    navController = navHostController,
                    startDestination = Screens.AudioListScreen.route
                ) {
                    composable(route = Screens.AudioListScreen.route) {
                        AudioListWithMiniPlayer(
                            audioPlayerViewModel = audioPlayerViewModel,
                            navigateToFullScreen = {
                                navHostController.navigate(
                                    Screens.AudioFullScreenPlayer.route.replace(
                                        "{AUDIO_ID}", it.toString()
                                    )
                                )
                            })
                    }

                    composable(route = Screens.AudioFullScreenPlayer.route) { backStackEntry ->
                        val audioId = backStackEntry.arguments?.getString("AUDIO_ID")
                        AudioPlayerFullScreen(
                            audioId = audioId?.toInt() ?: 0,
                            audioPlayerViewModel = audioPlayerViewModel
                        )
                    }
                }
            }
        }
    }

}

sealed class Screens(val route: String) {
    data object AudioListScreen : Screens("audioListScreen")
    data object AudioFullScreenPlayer : Screens("audioFullScreenPlayer/{AUDIO_ID}")
}