package com.example.audioplayerlibaray

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.audioplayerlibaray.audioplayer.ActivityAudioPlayerDemo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this@MainActivity, ActivityAudioPlayerDemo::class.java))
    }
}