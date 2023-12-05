package com.github.nabin0.audioplayer.audioplayer.view

import android.content.Context
import android.view.View
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.nabin0.audioplayer.view.AudioPlayer
import com.github.nabin0.audioplayer.view.AudioPlayerHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import com.github.nabin0.audioplayer.R
import com.github.nabin0.audioplayer.utils.ExoPlayerHelper
import com.github.nabin0.audioplayer.utils.PlaybackMode
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioPlayerTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var mContext: Context

    @MockK
    lateinit var audioPlayerHelper: AudioPlayerHelper

    private lateinit var audioPlayer: AudioPlayer

    @MockK
    lateinit var simpleExoPlayer: ExoPlayer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(ExoPlayerHelper)
        mContext = ApplicationProvider.getApplicationContext()
        every { ExoPlayerHelper.getAudioPlayerHelperInstance(mContext.applicationContext) }.returns(audioPlayerHelper)
        every { audioPlayerHelper.getCurrentPlaybackMode() }.returns(PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY)
        every { audioPlayerHelper.buffering }.returns(true)
        every { audioPlayerHelper.player }.returns(simpleExoPlayer)
        every { audioPlayerHelper.initializePlayer(mContext) }.returns(Unit)
        every { simpleExoPlayer.mediaMetadata}.returns(MediaMetadata.EMPTY)
        every { simpleExoPlayer.hasPreviousMediaItem()}.returns(true)
        every { simpleExoPlayer.hasNextMediaItem()}.returns(true)
        every { simpleExoPlayer.isPlaying }.returns(true)
        every { simpleExoPlayer.currentMediaItem }.returns(null)


        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer = AudioPlayer(mContext.applicationContext)
        }
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer.initializePlayer()
        }
    }

    @After
    fun tearDown(){
        unmockkAll()
    }

    @Test
    fun test_initializePlayerView() {
        Assert.assertNotNull(audioPlayer)
        Assert.assertTrue(audioPlayer is View)
    }

    @Test
    fun test_setCustomLayout() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer.setCustomLayout(R.layout.audio_player_control)
        }
    }

    @Test
    fun test_setAutoPlayEnabled() {
        every { audioPlayerHelper.setAutoPlayEnabled(true) }.returns(Unit)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer.setAutoPlayEnabled(true)
            verify { audioPlayerHelper.setAutoPlayEnabled(true) }
        }
    }


    @Test
    fun test_prepare() {
        every { audioPlayerHelper.prepare() }.returns(Unit)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer.prepare()
            verify { audioPlayerHelper.prepare() }
        }
    }

    @Test
    fun test_initializePlayer() {
        every { audioPlayerHelper.initializePlayer(any()) }.returns(Unit)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer.initializePlayer()
            verify { audioPlayerHelper.initializePlayer(any()) }
        }
    }

}