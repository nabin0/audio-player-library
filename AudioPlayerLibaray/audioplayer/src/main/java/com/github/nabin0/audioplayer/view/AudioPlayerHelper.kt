package com.github.nabin0.audioplayer.view

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.github.nabin0.audioplayer.events.AudioPlayerEventBus
import com.github.nabin0.audioplayer.models.Audio
import com.github.nabin0.audioplayer.service.AudioPlayerService
import com.github.nabin0.audioplayer.utils.Constants
import com.github.nabin0.audioplayer.utils.ExoPlayerHelper
import com.github.nabin0.audioplayer.utils.PlaybackMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.reflect.Modifier

/**
 * This contains all the code related to audio control.
 *
 * @param mContext: a valid context should be passed
 */
@VisibleForTesting(otherwise = Modifier.PROTECTED) class AudioPlayerHelper(private val mContext: Context) : Player.Listener {

    private var autoPlayEnabled: Boolean = false
    var player: ExoPlayer? = null
    var buffering = false

    private var handler = Handler(Looper.getMainLooper())
    private var updateProgressRunnable = Runnable {
        updateCurrentPosition()
    }

    private val _currentPlayingMediaIndex = MutableStateFlow(-1)
    val currentPlayingMediaIndex = _currentPlayingMediaIndex.asStateFlow()

    fun prepare() {
        try {
            player!!.prepare()
            player!!.playWhenReady = false
            if (autoPlayEnabled) {
                play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        updateCurrentPosition()
    }

    fun initializePlayer(context: Context) {
        player = ExoPlayerHelper.getExoplayerInstance(mContext).apply {
            addListener(this@AudioPlayerHelper)
            playWhenReady = false
        }
    }

    /**
     * Starts a foreground service to show media notification
     */
    fun startForegroundService() {
        val intent = Intent(mContext.applicationContext, AudioPlayerService::class.java)
        intent.action = Constants.APP_IN_FOREGROUND
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(intent)
        } else {
            mContext.startService(intent)
        }
    }

    private fun stopForegroundService() {
        if (!isMyServiceRunning(AudioPlayerService::class.java)) return
        val intent = Intent(mContext, AudioPlayerService::class.java)
        mContext.stopService(intent)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            mContext.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun hideNotification() {
        val intent = Intent(mContext, AudioPlayerService::class.java)
        intent.action = Constants.STOP_FOREGROUND_SERVICE
        mContext.startService(intent)
    }

    fun progressSeekTo(seekValue: Long) {
        player?.seekTo(seekValue)
        updateCurrentPosition()
    }

    /**
     * Emits current position value in millis
     */
    private fun updateCurrentPosition() {
        val currentPosition = player?.currentPosition ?: 0
        var duration = currentTrackDuration
        if (duration <= 0) duration = 0

        AudioPlayerEventBus.instance.onUpdateSeekBar(currentPosition, duration)
        handler.postDelayed(updateProgressRunnable, 60)
    }

    fun setAutoPlayEnabled(boolean: Boolean) {
        autoPlayEnabled = boolean
        player?.playWhenReady = boolean
    }

    /**
     * Plays selected media item from the playlist assigned to it
     *
     * @param index  Index of the mediaitem you want to play
     */
    fun playMediaItemByIndex(index: Int) {
        if (player != null) {
            _currentPlayingMediaIndex.value = index
            val mediaMetadata = player?.mediaMetadata
            sendMediaMetadata(mediaMetadata)
            if (player?.currentMediaItemIndex != index) player?.seekToDefaultPosition(index)
            if (autoPlayEnabled) play()
        }
    }

    fun play() {
        if (player != null) {
            AudioPlayerEventBus.instance.triggerPlayEvent()
            player!!.playWhenReady = true
        }
    }

    fun pause() {
        if (player != null) {
            hideNotification()
            player!!.playWhenReady = false
        }
    }

    fun playNextFromList() {
        if (player?.hasNextMediaItem() == true) {
            player?.seekToNextMediaItem()
        }
    }

    fun playPreviousFromList() {
        if (player?.hasPreviousMediaItem() == true) {
            player?.seekToPreviousMediaItem()
        }
    }

    fun currentPayingMediaIndex(): Int {
        _currentPlayingMediaIndex.value = player?.currentMediaItemIndex ?: -1
        return player?.currentMediaItemIndex ?: -1
    }

    fun isPlaying(): Boolean = player?.isPlaying == true

    fun togglePlayPauseEvent() {
        if (player?.isPlaying == true) {
            pause()
            AudioPlayerEventBus.instance.triggerPauseEvent()
        } else {
            play()
            AudioPlayerEventBus.instance.triggerPlayEvent()
        }
    }

    /**
     * Releases all the resources assigned related to the audio player
     */
    fun destroy() {
        handler.removeCallbacks(updateProgressRunnable)
        stopForegroundService()
        if (player != null) {
            if (player?.isPlaying == true) {
                player?.stop()
            }
            player!!.release()
        }
        ExoPlayerHelper.release()
    }

    fun setPlaybackMode(playbackMode: PlaybackMode) {
        if (playbackMode != PlaybackMode.SHUFFLE) player?.shuffleModeEnabled = false
        when (playbackMode) {
            PlaybackMode.SHUFFLE -> player?.shuffleModeEnabled = true

            PlaybackMode.NO_REPEAT -> player?.repeatMode = Player.REPEAT_MODE_OFF

            PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY -> player?.repeatMode =
                Player.REPEAT_MODE_ONE

            PlaybackMode.REPEAT_PLAYLIST -> player?.repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    fun getCurrentPlaybackMode(): PlaybackMode {
        if (player?.shuffleModeEnabled == true) return PlaybackMode.SHUFFLE
        when (player?.repeatMode) {
            Player.REPEAT_MODE_OFF -> {
                return PlaybackMode.NO_REPEAT
            }

            Player.REPEAT_MODE_ONE -> {
                return PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY
            }

            Player.REPEAT_MODE_ALL -> {
                return PlaybackMode.REPEAT_PLAYLIST
            }
        }
        return PlaybackMode.NO_REPEAT
    }

    val currentTrackDuration: Long
        get() = if (player != null) {
            player?.duration ?: 0L
        } else {
            0L
        }

    /**
     * Player Listener Method Implementation
     */
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                buffering = true
                AudioPlayerEventBus.instance.triggerOnBuffer()
            }

            Player.STATE_READY -> {
                buffering = false
                AudioPlayerEventBus.instance.triggerOnReady()
            }

            Player.STATE_ENDED -> {
                buffering = false
            }

            else -> {}
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            AudioPlayerEventBus.instance.triggerPlayEvent()
        } else {
            AudioPlayerEventBus.instance.triggerPauseEvent()
        }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        _currentPlayingMediaIndex.value = currentPayingMediaIndex()
        sendMediaMetadata(mediaMetadata)
    }

    /**
     * Emit event with latest metadata, called when the current playing audio is changed
     *
     * @param mediaMetadata = Metadata of the current playing song
     */
    private fun sendMediaMetadata(mediaMetadata: MediaMetadata?) {
        if (mediaMetadata == null) return
        AudioPlayerEventBus.instance.triggerOnMetadataChanged(mediaMetadata)
    }

    /**
     * Removes the previous playlist if assigned to the player and sets the new audio playlist
     *
     * @param audioList = List of audio
     */
    fun setAudioPlaylist(audioList: List<Audio>) {
        audioList.map { audio ->
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
            setMediaItemPlaylist(it)
        }
    }

    fun setMediaItemPlaylist(mediaItemList: List<MediaItem>) {
        player?.setMediaItems(mediaItemList)
        player?.prepare()
        _currentPlayingMediaIndex.value = -1
    }

    /**
     * This method will add the media items to the previous playlist if already available
     */
    fun addAudioPlaylist(audioList: List<Audio>) {
        audioList.map { audio ->
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
            addMediaItemPlaylist(it)
        }
    }

    fun addMediaItemPlaylist(mediaItemList: List<MediaItem>) {
        player?.addMediaItems(mediaItemList)
        player?.prepare()
    }

    // Testing code
    /*
    private fun buildRenderersFactory(
        context: Context, preferExtensionRenderer: Boolean,
    ): RenderersFactory {
        val extensionRendererMode =
            if (preferExtensionRenderer) DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
            else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON

        return DefaultRenderersFactory(context.applicationContext).setExtensionRendererMode(
            extensionRendererMode
        ).setEnableDecoderFallback(true)
    }

    private fun getDataSourceFactory(context: Context): DataSource.Factory =
        DefaultDataSource.Factory(context, getHttpDataSourceFactory(context))

    private fun getHttpDataSourceFactory(context: Context): HttpDataSource.Factory {
        var httpDataSourceFactory: HttpDataSource.Factory?

        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        CookieHandler.setDefault(cookieManager)
        httpDataSourceFactory = DefaultHttpDataSource.Factory()
        return httpDataSourceFactory
    }
    */
}