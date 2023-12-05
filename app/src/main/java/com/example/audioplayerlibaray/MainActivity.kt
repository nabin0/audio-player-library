package com.example.audioplayerlibaray

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audioplayerlibaray.audioplayer.ActivityAudioPlayerDemo
import com.example.audioplayerlibaray.audioplayer.compose_demo.ComposePlayerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
                    .clickable {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                ActivityAudioPlayerDemo::class.java
                            )
                        )
                    }) {
                    Text(
                        text = "Kotlin + XML demo",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.wrapContentSize(),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    )
                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
                    .clickable {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                ComposePlayerActivity::class.java
                            )
                        )
                    }) {
                    Text(
                        text = "Compose demo",
                        modifier = Modifier.wrapContentSize(),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    )
                }
            }
        }
    }
}