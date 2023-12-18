package com.github.nabin0.audioplayer.audioplayer.view

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.nabin0.audioplayer.events.AudioPlayerEventBus
import com.github.nabin0.audioplayer.provider.AudioChromeCastFeature
import com.github.nabin0.audioplayer.utils.ExoPlayerHelper
import com.github.nabin0.audioplayer.utils.PlaybackMode
import com.github.nabin0.audioplayer.view.AudioPlayer
import com.github.nabin0.audioplayer.view.AudioPlayerHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
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

    @RelaxedMockK
    lateinit var activity: FragmentActivity

    @Before
    fun setup() {
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

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer = AudioPlayer(mContext.applicationContext)
        }
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer.initializePlayer()
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test_initializePlayerView() {
        Assert.assertNotNull(audioPlayer)
        Assert.assertTrue(audioPlayer is View)
    }

//    @Test
//    fun test_setCustomLayout() {
//        InstrumentationRegistry.getInstrumentation().runOnMainSync {
//            audioPlayer.setCustomLayout(com.github.nabin0.audioplayer.R.layout.audio_player_control)
//        }
//    }

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

    @Test
    fun test_setActivityInstance() {
        mockkObject(AudioPlayerEventBus)
        every { AudioPlayerEventBus.instance.unregisterReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.registerReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.onUpdateSeekBar(any(), any()) }.returns(Unit)

        mockkObject(AudioChromeCastFeature)
        every { AudioChromeCastFeature.init(any()) }.returns(Unit)
        every { AudioChromeCastFeature.isChromeCastEnable() }.returns(true)
        every { AudioChromeCastFeature.setUpMediaRouteButton(any()) }.returns(Unit)
        audioPlayer.setActivityInstance(activity)
    }

    @Test
    fun test_onPause() {
        every { audioPlayerHelper.player?.isPlaying }.returns(true)
        audioPlayer.onPause()
        every { audioPlayerHelper.player?.isPlaying }.returns(false)
        audioPlayer.onPause()

        verify { audioPlayerHelper.player?.isPlaying }
    }

    @Test
    fun test_getCurrentPlayingMediaIndex() {
        audioPlayer.currentPlayingMediaIndex = MutableStateFlow(1)
        val currentIndex = audioPlayer.currentPlayingMediaIndex.value
        assert(currentIndex == 1)
    }

    @Test
    fun test_removeCallbacks() {
        mockkObject(AudioPlayerEventBus)
        every { AudioPlayerEventBus.instance.unregisterReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.registerReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.onUpdateSeekBar(any(), any()) }.returns(Unit)

        audioPlayer.removeCallbacks()
        verify { AudioPlayerEventBus.instance.unregisterReceiver(any()) }
    }

    @Test
    fun test_setMediaMetadata() {
        mockkObject(AudioPlayerEventBus)
        every { AudioPlayerEventBus.instance.unregisterReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.registerReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.onUpdateSeekBar(any(), any()) }.returns(Unit)

        val mockedMediaMetadata = mockk<MediaMetadata>()
        audioPlayer.setMediaMetadata(mediaMetadata = mockedMediaMetadata)
    }

    @Test
    fun test_onPlay() {
        every { audioPlayerHelper.player?.isPlaying }.returns(true)
        audioPlayer.onPause()
        every { audioPlayerHelper.player?.isPlaying }.returns(false)
        audioPlayer.onPlay()

        verify { audioPlayerHelper.player?.isPlaying }
    }

    @Test
    fun test_onMetadataChanged() {
        mockkObject(AudioPlayerEventBus)
        every { AudioPlayerEventBus.instance.unregisterReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.registerReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.onUpdateSeekBar(any(), any()) }.returns(Unit)

        val mockedMediaMetadata = mockk<MediaMetadata>()
        audioPlayer.onMetadataChanged(mockedMediaMetadata)
    }

    @Test
    fun test_setChromeCaseEnabled() {
        mockkObject(AudioPlayerEventBus)
        every { AudioPlayerEventBus.instance.unregisterReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.registerReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.onUpdateSeekBar(any(), any()) }.returns(Unit)

        mockkObject(AudioChromeCastFeature)
        every { AudioChromeCastFeature.init(any()) }.returns(Unit)
        every { AudioChromeCastFeature.isChromeCastEnable() }.returns(true)
        every { AudioChromeCastFeature.setUpMediaRouteButton(any()) }.returns(Unit)

        audioPlayer.setChromecastEnabled(true, "id")
    }

    @Test
    fun test_currentPlayingMediaIndex() {
        every { audioPlayerHelper.currentPayingMediaIndex() }.returns(2)
        val a = audioPlayer.currentPlayingAudioIndex()
        assert(a == 2)
    }

    @Test
    fun test_onReady_and_onBuffer() {
        every { audioPlayerHelper.player?.isPlaying }.returns(true)
        audioPlayer.onPause()
        every { audioPlayerHelper.player?.isPlaying }.returns(false)

        audioPlayer.onReady()
        audioPlayer.onBuffer()
    }

    @Test
    fun test_isPlaying() {
        every { audioPlayerHelper.player?.isPlaying }.returns(true)
        audioPlayer.isPlaying()
        verify { audioPlayerHelper.player?.isPlaying }
    }

    @Test
    fun test_playMediaByItemIndex() {
        every { audioPlayerHelper.playMediaItemByIndex(any()) }.returns(Unit)
        audioPlayer.playMediaItemByIndex(1)
        verify { audioPlayerHelper.playMediaItemByIndex(any()) }
    }

    @Test
    fun test_onDestroy() {
        mockkObject(AudioPlayerEventBus)
        every { AudioPlayerEventBus.instance.unregisterReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.registerReceiver(any()) }.returns(Unit)
        every { AudioPlayerEventBus.instance.onUpdateSeekBar(any(), any()) }.returns(Unit)
        every { audioPlayerHelper.destroy() }.returns(Unit)
        audioPlayer.destroy()
    }


    @Test
    fun test_setAudioList_and_Add_AudioList() {
        every { audioPlayerHelper.setAudioPlaylist(any()) }.returns(Unit)
        every { audioPlayerHelper.addAudioPlaylist(any()) }.returns(Unit)
        audioPlayer.addAudioList(listOf())
        audioPlayer.setPlaylist(listOf())

        verify { audioPlayerHelper.setAudioPlaylist(any()) }
    }
}