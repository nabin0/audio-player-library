package com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.github.nabin0.audioplayer.models.Audio
import com.github.nabin0.audioplayer.view.AudioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * This is shared viewModel used to manage exoplayer instance and provide playlist to multiple composable
 */
@SuppressLint("StaticFieldLeak")
@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    /**
     * This is just used to initialize and release the data related to audioPlayer when the parent activity is removed as well as to store playlist
     */
    private var audioPlayer: AudioPlayer? = null
    var showMiniPlayer = false

    val sampleAudioList = listOf(
        Audio(
            1L,
            Uri.parse("https://audio.jukehost.co.uk/VgRsaqsm6pq9usbsOXipERF9dgBYwwGP"),
            "",
            "",
            "Artist a, Artist b, Artist c",
            "Melodic Moonlit Nigh",
            "",
            147000
        ), Audio(
            2L,
            Uri.parse("https://audio.jukehost.co.uk/232UbyB4GT5jnoBTnm75I1vDy9G7FYvR"),
            "",
            "https://img.freepik.com/premium-photo/music-mind-music-abstract-art-created-with-generative-ai-technology_545448-15311.jpg",
            "Artist 1, Artist 2",
            "Morning Dew Serenity",
            "",
            19000
        ), Audio(
            101L,
            Uri.parse("https://audio.jukehost.co.uk/9YZDpnZ1JtabA78mh21x2nmvMxUbfwmi"),
            "",
            "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
            "Artist Unknown",
            "Eternal Ocean Breeze",
            "",
            59000
        ), Audio(
            3L,
            Uri.parse("https://audio.jukehost.co.uk/zG1wD3kJ8aCsw5iycu6f5ArvZyuhXDVu"),
            "",
            "",
            "Artist Name, ",
            "Soothing Sunset Serenade 3",
            "",
            213000
        )
    )

    val dummyAudioInstance = Audio(
        0L, Uri.EMPTY, "", "", "", "", "", 0
    )

    fun getAudioPlayer(context: Context): AudioPlayer {
        audioPlayer = audioPlayer ?: AudioPlayer(context = context).apply {
            initializePlayer()
            setPlaylist(sampleAudioList)
            removeCallbacks()
        }
        return audioPlayer as AudioPlayer
    }

    val currentPlayingAudioIndex = audioPlayer?.currentPlayingMediaIndex ?: MutableStateFlow(-1)

    override fun onCleared() {
        audioPlayer?.destroy()
        showMiniPlayer = false
        super.onCleared()
    }
}