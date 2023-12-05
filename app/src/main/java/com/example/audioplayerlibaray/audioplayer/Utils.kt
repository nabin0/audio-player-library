package com.example.audioplayerlibaray.audioplayer

import android.net.Uri
import com.github.nabin0.audioplayer.models.Audio

object Utils {
    var SHOW_MINI_PLAYER = false
    var CURRENT_AUDIO_INDEX = -1

    val sampleAudioList = listOf(
        com.github.nabin0.audioplayer.models.Audio(
            1L,
            Uri.parse("https://audio.jukehost.co.uk/VgRsaqsm6pq9usbsOXipERF9dgBYwwGP"),
            "",
            "",
            "Artist 1",
            "Ringtone 1",
            "",
            0
        ), com.github.nabin0.audioplayer.models.Audio(
            2L,
            Uri.parse("https://audio.jukehost.co.uk/232UbyB4GT5jnoBTnm75I1vDy9G7FYvR"),
            "",
            "https://img.freepik.com/premium-photo/music-mind-music-abstract-art-created-with-generative-ai-technology_545448-15311.jpg",
            "Artist 2",
            "Ringtone 2",
            "",
            0
        ), com.github.nabin0.audioplayer.models.Audio(
            101L,
            Uri.parse("https://audio.jukehost.co.uk/9YZDpnZ1JtabA78mh21x2nmvMxUbfwmi"),
            "",
            "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
            "Artist Unknown",
            "Ringtone",
            "",
            0
        ), com.github.nabin0.audioplayer.models.Audio(
            3L,
            Uri.parse("https://audio.jukehost.co.uk/zG1wD3kJ8aCsw5iycu6f5ArvZyuhXDVu"),
            "",
            "",
            "Artist 3",
            "Ringtone 3",
            "",
            0
        )
    )

    val dummyAudioInstance = com.github.nabin0.audioplayer.models.Audio(
        0L, Uri.EMPTY, "", "", "", "", "", 0
    )

}
