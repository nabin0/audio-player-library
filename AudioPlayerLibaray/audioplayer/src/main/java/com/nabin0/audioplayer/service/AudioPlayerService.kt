package com.nabin0.audioplayer.service

import android.content.Intent
import android.content.IntentFilter
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.vl.viewlift.playersdk.audioplayer.broadcast_receiver.HeadsetBroadcastReceiver
import com.vl.viewlift.playersdk.audioplayer.notification.AudioPlayerNotificationManager
import com.vl.viewlift.playersdk.audioplayer.utils.Constants
import com.vl.viewlift.playersdk.audioplayer.utils.Constants.APP_IN_BACKGROUND
import com.vl.viewlift.playersdk.audioplayer.utils.Constants.STOP_FOREGROUND_SERVICE
import com.vl.viewlift.playersdk.audioplayer.utils.ExoPlayerHelper

/**
 * Service class that manages the foreground service (Media Notification) for the VLAudioplayer
 *
 * Should be used when the notification is required
 *
 */
class AudioPlayerService : MediaSessionService(), PlayerNotificationManager.NotificationListener {
    private lateinit var mediaSession: MediaSession
    private lateinit var notificationManager: AudioPlayerNotificationManager
    private lateinit var exoPlayer: ExoPlayer

    private val headsetReceiver = HeadsetBroadcastReceiver()

    companion object {
        var isServiceRunning = false
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession =
        mediaSession

    @UnstableApi
    override fun onCreate() {
        super.onCreate()

        /** Registering Headset receiver that manages player instance when headset event occurs */
        val headSetIntentFilter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
        headSetIntentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
        registerReceiver(headsetReceiver, headSetIntentFilter)

        exoPlayer = ExoPlayerHelper.getExoplayerInstance(this)
        mediaSession = ExoPlayerHelper.getMediaSession(this)
        notificationManager = ExoPlayerHelper.getNotificationManager(
            this, this@AudioPlayerService
        )
        isServiceRunning = true
    }

    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isServiceRunning = true
        /**Show Notification only when App is not in background and stop foreground is not called*/
        val shouldStartNotification =
            (intent?.action != STOP_FOREGROUND_SERVICE) && (intent?.action != APP_IN_BACKGROUND)
        if (shouldStartNotification) {
            notificationManager.startNotification(
                mediaSession = mediaSession, mediaSessionService = this
            )
        }

        if (intent?.hasExtra(Constants.HEADSET_ACTION) == true) {
            when (intent.getStringExtra(Constants.HEADSET_ACTION)) {
                Constants.PLAY -> {
                    exoPlayer.play()
                }

                Constants.PAUSE -> {
                    exoPlayer.pause()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopService() {
        mediaSession.release()
        notificationManager.stopNotification()
        this@AudioPlayerService.stopForeground(true)
        stopSelf()
        isServiceRunning = false
    }

    override fun onDestroy() {
        stopService()
        unregisterReceiver(headsetReceiver)
        super.onDestroy()
    }


}