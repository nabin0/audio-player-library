package com.github.nabin0.audioplayer.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager.NotificationListener
import com.github.nabin0.audioplayer.notification.AudioPlayerNotificationManager
import com.github.nabin0.audioplayer.view.AudioPlayerHelper

/**
 * A Class to provide required singletons for the playing of audio
 */
@SuppressLint("StaticFieldLeak")
object ExoPlayerHelper {

    @Volatile
    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    private var audioPlayerHelper: AudioPlayerHelper? = null
    private var audioPlayerNotificationManager: AudioPlayerNotificationManager? = null

    fun getExoplayerInstance(context: Context): ExoPlayer {
        if (exoPlayer == null) {
            synchronized(this) {
                exoPlayer = ExoPlayer.Builder(context).setAudioAttributes(
                    AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                        .setUsage(C.USAGE_MEDIA).build(), true
                ).setHandleAudioBecomingNoisy(true).build()
            }
        }
        return exoPlayer!!
    }

    internal fun getAudioPlayerHelperInstance(context: Context): AudioPlayerHelper {
        if (audioPlayerHelper == null) {
            synchronized(this) {
                audioPlayerHelper = AudioPlayerHelper(context)
            }
        }
        return audioPlayerHelper!!
    }

    fun getMediaSession(context: Context): MediaSession {
        if (mediaSession == null) {
            synchronized(this) {
                mediaSession = MediaSession.Builder(context, getExoplayerInstance(context)).build()
            }
        }
        return mediaSession!!
    }


    fun getNotificationManager(
        context: Context,
        notificationListener: NotificationListener,
    ): AudioPlayerNotificationManager {
        if (audioPlayerNotificationManager == null) {
            synchronized(this) {
                audioPlayerNotificationManager =
                    AudioPlayerNotificationManager(
                        context,
                        getExoplayerInstance(context),
                        notificationListener
                    )
            }
        }
        return audioPlayerNotificationManager!!
    }

    fun release() {
        exoPlayer = null
        mediaSession = null
        audioPlayerNotificationManager = null
        audioPlayerHelper = null
    }
}

