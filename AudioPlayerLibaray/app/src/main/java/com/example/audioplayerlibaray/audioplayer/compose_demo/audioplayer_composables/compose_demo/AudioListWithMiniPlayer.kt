package com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.compose_demo

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.audioplayerlibaray.R
import com.example.audioplayerlibaray.audioplayer.compose_demo.audioplayer_composables.AudioPlayerViewModel
import com.github.nabin0.audioplayer.models.Audio

@Composable
fun AudioListWithMiniPlayer(
    audioPlayerViewModel: AudioPlayerViewModel,
    navigateToFullScreen: (index: Int) -> Unit,
) {

    val listState = rememberLazyListState()
    val currentPlayingAudioIndex by audioPlayerViewModel.currentPlayingAudioIndex.collectAsState()
    val mContext = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        audioPlayerViewModel.getAudioPlayer(mContext)
    }
    Scaffold(
        bottomBar = {
            if (audioPlayerViewModel.showMiniPlayer) {
                AudioMiniPlayer(modifier = Modifier.clickable {
                    navigateToFullScreen(currentPlayingAudioIndex)
                })
            }
        },
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 4.dp),
            state = listState,
            modifier = Modifier
                .background(Color(0xFF07132D))
                .fillMaxSize()
                .padding(it)
        ) {
            val playingAudio = if (currentPlayingAudioIndex != -1) {
                audioPlayerViewModel.sampleAudioList[currentPlayingAudioIndex]
            } else audioPlayerViewModel.dummyAudioInstance
            itemsIndexed(audioPlayerViewModel.sampleAudioList) { index, audio ->
                AudioItem(audio = audio, onItemClick = {
                    navigateToFullScreen(index)
                })
            }
        }
    }
}


@Composable
fun PlayListInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = com.github.nabin0.audioplayer.R.drawable.audio_placeholder),
            contentDescription = "playlist image",
            modifier = Modifier.fillMaxWidth()
        )

        Column(Modifier.padding(8.dp)) {
            Text(
                text = "Locked on Capitals Podcast",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin",
                style = TextStyle(color = Color.White),
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,

                )
        }
    }
}

@Composable
fun CustomArtImage(albumArtUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = modifier
            .padding(16.dp)
            .aspectRatio(1f)
            .heightIn(min = 220.dp, max = 240.dp),
        model = ImageRequest.Builder(LocalContext.current).data(albumArtUrl)
            .error(com.github.nabin0.audioplayer.R.drawable.audio_placeholder).crossfade(true)
            .build(),
        placeholder = painterResource(id = com.github.nabin0.audioplayer.R.drawable.audio_placeholder),
        contentDescription = "Albumart",
        contentScale = ContentScale.Crop
    )
}


@Composable
fun AudioItem(audio: Audio, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .background(Color(0xFF0D2148))
            .fillMaxWidth()
            .clickable {
                onItemClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        audio.albumArtUrl?.let {
            CustomArtImage(
                albumArtUrl = it,
                modifier = Modifier.size(100.dp)
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Column(
            modifier = Modifier.padding(start = 4.dp), verticalArrangement = Arrangement.SpaceEvenly
        ) {
            androidx.compose.material.Text(
                text = "EPISODE 1",
                style = TextStyle(color = Color.White.copy(0.7f))
            )
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material.Text(
                text = audio.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(color = Color.White)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 8.dp)
                )
                androidx.compose.material.Text(
                    text = TimeUtil.formatTime(audio.duration.toLong()),
                    style = TextStyle(color = Color.White.copy(0.5f))
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = androidx.media3.ui.R.drawable.exo_icon_play),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            onItemClick.invoke()
                        }
                )
            }
        }
    }
}