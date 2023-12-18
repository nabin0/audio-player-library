package com.github.nabin0.audioplayer.audioplayer.view

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.exoplayer.ExoPlayer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.nabin0.audioplayer.CustomServiceTestRule
import com.github.nabin0.audioplayer.audioplayer.Utils.sampleAudioList
import com.github.nabin0.audioplayer.utils.ExoPlayerHelper
import com.github.nabin0.audioplayer.utils.PlaybackMode
import com.github.nabin0.audioplayer.view.AudioPlayerHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioPlayerHelperTest {

    private lateinit var mContext: Context
    private lateinit var audioPlayerHelper: com.github.nabin0.audioplayer.view.AudioPlayerHelper

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val serviceRule = CustomServiceTestRule()

    @MockK
    lateinit var simpleExoPlayer: ExoPlayer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mContext = ApplicationProvider.getApplicationContext()

        mockkObject(ExoPlayerHelper)
        every { ExoPlayerHelper.getExoplayerInstance(mContext) }.returns(
            simpleExoPlayer
        )

        audioPlayerHelper = com.github.nabin0.audioplayer.view.AudioPlayerHelper(mContext)
        every { simpleExoPlayer.prepare() }.returns(Unit)
        every { simpleExoPlayer.addListener(audioPlayerHelper) }.returns(Unit)

        every { simpleExoPlayer.playWhenReady = true }.returns(Unit)
        every { simpleExoPlayer.playWhenReady = false }.returns(Unit)
        every { simpleExoPlayer.currentPosition } returns 100L
        every { simpleExoPlayer.duration } returns 1000L
        every { simpleExoPlayer.isPlaying } returns true
        every { simpleExoPlayer.seekTo(any()) } returns (Unit)
        audioPlayerHelper.initializePlayer(mContext)
    }

    @Test
    fun test_isPlayerInitialized() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            verify {
                ExoPlayerHelper.getExoplayerInstance(
                    mContext
                )
            }
            Assert.assertNotNull(audioPlayerHelper.player)
            Assert.assertTrue(audioPlayerHelper.player == simpleExoPlayer)
        }
    }

    @Test
    fun test_updateSeekTo() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayerHelper.progressSeekTo(134L)
            verify { simpleExoPlayer.seekTo(134L) }
        }
    }

    @Test
    fun test_getCurrentTrackDuration() {
        every { simpleExoPlayer.duration }.returns(1234L)
        var mAssert = audioPlayerHelper.currentTrackDuration == 1234L
        assert(mAssert)
        audioPlayerHelper.player = null
        mAssert = audioPlayerHelper.currentTrackDuration == 0L
        assert(mAssert)
    }

    @Test
    fun test_getCurrentPlaybackMode() {
        every { simpleExoPlayer.shuffleModeEnabled } returns false
        every { simpleExoPlayer.repeatMode } returns REPEAT_MODE_OFF
        var mAssert = audioPlayerHelper.getCurrentPlaybackMode() == PlaybackMode.NO_REPEAT
        Assert.assertTrue(mAssert)
        every { simpleExoPlayer.repeatMode } returns Player.REPEAT_MODE_ONE
        mAssert =
            audioPlayerHelper.getCurrentPlaybackMode() == PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY
        Assert.assertTrue(mAssert)
    }

    @Test
    fun test_setPlaybackMode() {
        every { simpleExoPlayer.repeatMode = any() } returns Unit
        every { simpleExoPlayer.shuffleModeEnabled } returns true
        every { simpleExoPlayer.shuffleModeEnabled = any() } returns Unit
        every { simpleExoPlayer.repeatMode } returns REPEAT_MODE_OFF
        audioPlayerHelper.setPlaybackMode(PlaybackMode.SHUFFLE)
        var mAssert = audioPlayerHelper.getCurrentPlaybackMode() == PlaybackMode.SHUFFLE
        Assert.assertTrue(mAssert)

        every { simpleExoPlayer.shuffleModeEnabled } returns false
        audioPlayerHelper.setPlaybackMode(PlaybackMode.NO_REPEAT)
        every { simpleExoPlayer.repeatMode } returns REPEAT_MODE_OFF
        mAssert = audioPlayerHelper.getCurrentPlaybackMode() == PlaybackMode.NO_REPEAT
        Assert.assertTrue(mAssert)

        every { simpleExoPlayer.repeatMode } returns REPEAT_MODE_ALL
        audioPlayerHelper.setPlaybackMode(PlaybackMode.REPEAT_PLAYLIST)
        mAssert = audioPlayerHelper.getCurrentPlaybackMode() == PlaybackMode.REPEAT_PLAYLIST
        Assert.assertTrue(mAssert)

        every { simpleExoPlayer.repeatMode } returns REPEAT_MODE_ONE
        audioPlayerHelper.setPlaybackMode(PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY)
        mAssert =
            audioPlayerHelper.getCurrentPlaybackMode() == PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY
        Assert.assertTrue(mAssert)
    }

    @Test
    fun test_setPlaybackModeRepeatPlayList() {
        every { simpleExoPlayer.shuffleModeEnabled } returns false
        every { simpleExoPlayer.shuffleModeEnabled = true } returns Unit
        every { simpleExoPlayer.shuffleModeEnabled = false } returns Unit
        every { simpleExoPlayer.repeatMode } returns REPEAT_MODE_ALL
        every { simpleExoPlayer.repeatMode = REPEAT_MODE_ALL } returns Unit
        audioPlayerHelper.setPlaybackMode(PlaybackMode.REPEAT_PLAYLIST)
        val mAssert = audioPlayerHelper.getCurrentPlaybackMode() == PlaybackMode.REPEAT_PLAYLIST
        Assert.assertTrue(mAssert)
    }

    @Test
    fun test_setPlaylist() {
        var mediaItemList: List<MediaItem>
        sampleAudioList.map { audio ->
            var artWorkUri: Uri? = null
            try {
                artWorkUri = Uri.parse(audio.albumArtUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            MediaItem.Builder().setUri(audio.audioUri).setMediaMetadata(
                MediaMetadata.Builder().setAlbumArtist(audio.artist).setTitle(audio.title)
                    .setDisplayTitle(audio.displayName).setSubtitle(audio.displayName)
                    .setArtworkUri(artWorkUri).build()
            ).build()
        }.also {
            mediaItemList = it
        }
        every { simpleExoPlayer.setMediaItems(any()) }.returns(Unit)
        audioPlayerHelper.setAudioPlaylist(sampleAudioList)
        verify { simpleExoPlayer.setMediaItems(mediaItemList) }
        audioPlayerHelper.setMediaItemPlaylist(mediaItemList)
        verify { simpleExoPlayer.setMediaItems(mediaItemList) }
    }

    @Test
    fun test_addPlaylist() {
        var mediaItemList: List<MediaItem> = mutableListOf()
        sampleAudioList.map { audio ->
            var artWorkUri: Uri? = null
            try {
                artWorkUri = Uri.parse(audio.albumArtUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            MediaItem.Builder().setUri(audio.audioUri).setMediaMetadata(
                MediaMetadata.Builder().setAlbumArtist(audio.artist).setTitle(audio.title)
                    .setDisplayTitle(audio.displayName).setSubtitle(audio.displayName)
                    .setArtworkUri(artWorkUri).build()
            ).build()
        }.also {
            mediaItemList = it
        }
        every { simpleExoPlayer.addMediaItems(any()) }.returns(Unit)
        every { simpleExoPlayer.prepare() }.returns(Unit)
        audioPlayerHelper.addAudioPlaylist(sampleAudioList)
        verify { simpleExoPlayer.addMediaItems(mediaItemList) }
        audioPlayerHelper.addMediaItemPlaylist(mediaItemList)
        verify { simpleExoPlayer.addMediaItems(mediaItemList) }
        verify { simpleExoPlayer.prepare() }
    }


    @Test
    fun test_preparePlayer() {
        audioPlayerHelper.prepare()
        verify { simpleExoPlayer.prepare() }
    }

    @Test
    fun test_play() {
        audioPlayerHelper.initializePlayer(mContext)
        audioPlayerHelper.play()
        verify { simpleExoPlayer.playWhenReady = true }
        Assert.assertTrue(audioPlayerHelper.isPlaying())
    }

    @Test
    fun test_pause() {
        audioPlayerHelper.initializePlayer(mContext)
        audioPlayerHelper.pause()
        verify { simpleExoPlayer.playWhenReady = false }
    }

    @Test
    fun test_playMediaItemByIndex() {
        every { simpleExoPlayer.hasNextMediaItem() }.returns(false)
        every { simpleExoPlayer.seekToDefaultPosition(any()) } returns Unit
        every { simpleExoPlayer.currentMediaItemIndex } returns 0
        every { simpleExoPlayer.mediaMetadata }.returns(MediaMetadata.EMPTY)
        audioPlayerHelper.playMediaItemByIndex(2)
        verify { simpleExoPlayer.seekToDefaultPosition(2) }
    }

    @Test
    fun test_playNextMediaItemFromPlayList() {
        every { simpleExoPlayer.hasNextMediaItem() }.returns(true)
        every { simpleExoPlayer.seekToNextMediaItem() }.returns(Unit)
        audioPlayerHelper.playNextFromList()
        verify { simpleExoPlayer.seekToNextMediaItem() }
    }


    @Test
    fun test_playPreviousMediaItemFromPlayList() {
        every { simpleExoPlayer.hasPreviousMediaItem() }.returns(true)
        every { simpleExoPlayer.seekToPreviousMediaItem() }.returns(Unit)
        audioPlayerHelper.playPreviousFromList()
        verify { simpleExoPlayer.seekToPreviousMediaItem() }
    }

    @Test
    fun test_togglePlayPauseEvent() {
        every { simpleExoPlayer?.isPlaying } returns true
        audioPlayerHelper.togglePlayPauseEvent()
        verify { simpleExoPlayer.playWhenReady = false }
        every { simpleExoPlayer.isPlaying } returns false
        audioPlayerHelper.togglePlayPauseEvent()
        verify { simpleExoPlayer.playWhenReady = true }
    }


    @Test
    fun test_startForegroundService() {
        val mockkAudioPlayerHelper = mockk<AudioPlayerHelper>()
//        every { simpleExoPlayer.addMediaItems(any()) }.returns(Unit)
//        audioPlayerHelper.initializePlayer(mContext)
//        audioPlayerHelper.addAudioPlaylist(sampleAudioList)
//        audioPlayerHelper.prepare()
//        every { mockkAudioPlayerHelper.startForegroundService() }.returns(Unit)
//        audioPlayerHelper.startForegroundService()
//        verify { AudioPlayerService.isServiceRunning }

    }

    @Test
    fun test_currentPlayingMediaIndex() {
        every { simpleExoPlayer.currentMediaItemIndex }.returns(1)
        val result = audioPlayerHelper.currentPayingMediaIndex()
        assert(result == 1)
    }

    @Test
    fun test_destroy() {
        every { simpleExoPlayer.stop() }.returns(Unit)
        every { simpleExoPlayer.release() }.returns(Unit)
        every { ExoPlayerHelper.release() }.returns(Unit)
        val mockkAudioPlayerHelper = spyk(audioPlayerHelper)
        mockkAudioPlayerHelper.destroy()
        every { mockkAudioPlayerHelper.isMyServiceRunning(any()) }.returns(false)
        mockkAudioPlayerHelper.destroy()
        verify { ExoPlayerHelper.release() }
    }

    @Test
    fun test_setAutoPlayEnabled() {
        every { simpleExoPlayer.playWhenReady = any() }.returns(Unit)
        audioPlayerHelper.setAutoPlayEnabled(true)
        verify { simpleExoPlayer.playWhenReady = true }
        audioPlayerHelper.setAutoPlayEnabled(false)
        verify { simpleExoPlayer.playWhenReady = false }
    }
}