package com.example.audioplayerlibaray.audioplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.audioplayerlibaray.R
import com.github.nabin0.audioplayer.view.AudioPlayer
class ActivityAudioPlayerDemo : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private var audioPlayer: AudioPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_audio_player)

        audioPlayer = AudioPlayer(this)
        audioPlayer?.initializePlayer()
        audioPlayer?.setPlaylist(Utils.sampleAudioList)
        audioPlayer?.prepare()

        // audioPlayer?.setAutoPlayEnabled(true) // This will play the first audio of given playlist call only if you are in playerdetail activity or need to autoplay audio without opening detail screen
        audioPlayer?.removeCallbacks() // removing callback as this is not used as ui

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, AudioListFragment())
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        audioPlayer?.destroy()
        Utils.SHOW_MINI_PLAYER = false
        super.onDestroy()
    }

}