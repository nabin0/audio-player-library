package com.github.nabin0.audioplayer.audioplayer.view

import android.content.Context
import android.view.View
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.github.nabin0.audioplayer.events.AudioPlayerEventBus
import com.github.nabin0.audioplayer.utils.ExoPlayerHelper
import com.github.nabin0.audioplayer.utils.PlaybackMode
import com.github.nabin0.audioplayer.view.AudioMiniPlayer
import com.github.nabin0.audioplayer.view.AudioPlayerHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AudioMiniPlayerTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var audioMiniPlayer : AudioMiniPlayer

    @MockK
    lateinit var audioPlayerHelper: AudioPlayerHelper

    @MockK
    lateinit var simpleExoPlayer: ExoPlayer

    private lateinit var mContext: Context

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        mockkObject(ExoPlayerHelper)
        mContext = ApplicationProvider.getApplicationContext()
        every { ExoPlayerHelper.getAudioPlayerHelperInstance(mContext.applicationContext) }.returns(
            audioPlayerHelper
        )
        every { audioPlayerHelper.getCurrentPlaybackMode() }.returns(PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY)
        every { audioPlayerHelper.buffering }.returns(true)
        every { audioPlayerHelper.currentPlayingMediaIndex }.returns(MutableStateFlow(1))
        every { audioPlayerHelper.player }.returns(simpleExoPlayer)
        every { audioPlayerHelper.initializePlayer(mContext) }.returns(Unit)
        every { simpleExoPlayer.mediaMetadata }.returns(MediaMetadata.EMPTY)
        every { simpleExoPlayer.hasPreviousMediaItem() }.returns(true)
        every { simpleExoPlayer.hasNextMediaItem() }.returns(true)
        every { simpleExoPlayer.isPlaying }.returns(true)
        every { simpleExoPlayer.currentMediaItem }.returns(null)

        audioMiniPlayer = AudioMiniPlayer(mContext)
    }
    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test_initializePlayerView() {
        Assert.assertNotNull(audioMiniPlayer)
        Assert.assertTrue(audioMiniPlayer is View)
    }
//
//    @Test
//    fun test_setAutoPlayEnabled() {
//        every { audioPlayerHelper.setAutoPlayEnabled(true) }.returns(Unit)
//        InstrumentationRegistry.getInstrumentation().runOnMainSync {
//            audioMiniPlayer.setAutoPlayEnabled(true)
//            verify { audioPlayerHelper.setAutoPlayEnabled(true) }
//        }
//    }
//
//
//    @Test
//    fun test_prepare() {
//        every { audioPlayerHelper.prepare() }.returns(Unit)
//        InstrumentationRegistry.getInstrumentation().runOnMainSync {
//            audioMiniPlayer.prepare(listOf())
//            verify { audioPlayerHelper.prepare() }
//        }
//    }
//
//    @Test
//    fun test_initializePlayer() {
//        every { audioPlayerHelper.initializePlayer(any()) }.returns(Unit)
//        InstrumentationRegistry.getInstrumentation().runOnMainSync {
//            audioMiniPlayer.initializePlayer()
//            verify { audioPlayerHelper.initializePlayer(any()) }
//        }
//    }
//
//
//
//    @Test
//    fun test_onPause() {
//        every { audioPlayerHelper.player?.isPlaying }.returns(true)
//        audioMiniPlayer.onPause()
//        every { audioPlayerHelper.player?.isPlaying }.returns(false)
//        audioMiniPlayer.onPause()
//
//        verify { audioPlayerHelper.player?.isPlaying }
//    }
//
//
//    @Test
//    fun test_removeCallbacks() {
//        mockkObject(AudioPlayerEventBus)
//        every { AudioPlayerEventBus.instance.unregisterReceiver(any()) }.returns(Unit)
//        every { AudioPlayerEventBus.instance.registerReceiver(any()) }.returns(Unit)
//        every { AudioPlayerEventBus.instance.onUpdateSeekBar(any(), any()) }.returns(Unit)
//
//        audioMiniPlayer.removeCallbacks()
//        verify { AudioPlayerEventBus.instance.unregisterReceiver(any()) }
//    }
//
//    @Test
//    fun test_onPlay() {
//        every { audioPlayerHelper.player?.isPlaying }.returns(true)
//        audioMiniPlayer.onPause()
//        every { audioPlayerHelper.player?.isPlaying }.returns(false)
//        audioMiniPlayer.onPlay()
//
//        verify { audioPlayerHelper.player?.isPlaying }
//    }
//
//    @Test
//    fun test_onMetadataChanged() {
//        mockkObject(AudioPlayerEventBus)
//        every { AudioPlayerEventBus.instance.unregisterReceiver(any()) }.returns(Unit)
//        every { AudioPlayerEventBus.instance.registerReceiver(any()) }.returns(Unit)
//        every { AudioPlayerEventBus.instance.onUpdateSeekBar(any(), any()) }.returns(Unit)
//
//        val mockedMediaMetadata = mockk<MediaMetadata>()
//        audioMiniPlayer.onMetadataChanged(mockedMediaMetadata)
//    }
//
//    @Test
//    fun test_currentPlayingMediaIndex() {
//        every { audioPlayerHelper.currentPayingMediaIndex() }.returns(2)
//        val a = audioMiniPlayer.currentPlayingAudioIndex()
//        assert(a == 2)
//    }
//
//    @Test
//    fun test_onReady_and_onBuffer() {
//        every { audioPlayerHelper.player?.isPlaying }.returns(true)
//        audioMiniPlayer.onPause()
//        every { audioPlayerHelper.player?.isPlaying }.returns(false)
//
//        audioMiniPlayer.onReady()
//        audioMiniPlayer.onBuffer()
//    }
//
//    @Test
//    fun test_isPlaying() {
//        every { audioPlayerHelper.player?.isPlaying }.returns(true)
//        audioMiniPlayer.isPlaying()
//        verify { audioPlayerHelper.player?.isPlaying }
//    }
//
//    @Test
//    fun test_onDestroy() {
//        mockkObject(AudioPlayerEventBus)
//        every { AudioPlayerEventBus.instance.unregisterReceiver(any()) }.returns(Unit)
//        every { AudioPlayerEventBus.instance.registerReceiver(any()) }.returns(Unit)
//        every { AudioPlayerEventBus.instance.onUpdateSeekBar(any(), any()) }.returns(Unit)
//        every { audioPlayerHelper.destroy() }.returns(Unit)
//        audioMiniPlayer.destroy()
//    }
//
//
//    @Test
//    fun test_setAudioList_and_Add_AudioList() {
//        every { audioPlayerHelper.setAudioPlaylist(any()) }.returns(Unit)
//        every { audioPlayerHelper.addAudioPlaylist(any()) }.returns(Unit)
//        audioMiniPlayer.addAudioList(listOf())
//        audioMiniPlayer.setPlaylist(listOf())
//
//        verify { audioPlayerHelper.setAudioPlaylist(any()) }
//    }
//
}