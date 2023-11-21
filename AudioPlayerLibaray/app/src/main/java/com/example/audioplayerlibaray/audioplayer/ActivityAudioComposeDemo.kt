package com.example.audioplayerlibaray.audioplayer

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.nabin0.audioplayer.view.AudioPlayer
import com.vl.vlplayer.demo.audioplayer.Utils

//class ActivityAudioComposeDemo : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            val mcontext = LocalContext.current as Activity
//            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
//                AudioPlayer(mcontext).apply {
//                initializePlayer()
//                setChromecastEnabled(true)
//                setPlaylist(Utils.sampleAudioList)
//            } }, update = {
//
//            })
//        }
//    }
//}