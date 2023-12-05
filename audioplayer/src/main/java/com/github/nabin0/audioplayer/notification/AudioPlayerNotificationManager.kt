package com.github.nabin0.audioplayer.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import androidx.media3.ui.PlayerNotificationManager.NotificationListener
import java.lang.reflect.Modifier.PRIVATE

@VisibleForTesting(otherwise = PRIVATE)
internal const val NOTIFICATION_ID = 12

@VisibleForTesting(otherwise = PRIVATE)
internal const val NOTIFICATION_CHANNEL_NAME = "nabin0.audioplayer.channel_"

@VisibleForTesting(otherwise = PRIVATE)
internal const val NOTIFICATION_CHANNEL_ID = "nabin0.audioplayer.channel_id_"

/**
 * It is responsible to create and manage Media3 notification for foreground service
 */
@UnstableApi
class AudioPlayerNotificationManager(
    private val context: Context,
    @get:VisibleForTesting(otherwise = PRIVATE)
    internal val exoPlayer: ExoPlayer,
    private val notificationListener: NotificationListener,
) {
    @VisibleForTesting(otherwise = PRIVATE)
    internal var playerNotificationManager: PlayerNotificationManager? = null

    @VisibleForTesting(otherwise = PRIVATE)
    internal var notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    fun startNotification(mediaSession: MediaSession, mediaSessionService: MediaSessionService) {
        buildNotification(mediaSession = mediaSession)
        startForegroundNotificationService(mediaSessionService)
    }

    @VisibleForTesting(otherwise = PRIVATE)
    internal fun startForegroundNotificationService(mediaSessionService: MediaSessionService) {
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        } else {
            Notification.Builder(context).setCategory(Notification.CATEGORY_SERVICE).build()
        }
        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    @VisibleForTesting(otherwise = PRIVATE)
    internal fun buildNotification(mediaSession: MediaSession) {
        playerNotificationManager = PlayerNotificationManager.Builder(
            context,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        )
            .setMediaDescriptionAdapter(
                AudioPlayerNotificationAdapter(
                    context = context,
                    pendingIntent = mediaSession.sessionActivity
                )
            )
            .setSmallIconResourceId(androidx.media3.session.R.drawable.media3_notification_small_icon)
            .setNotificationListener(notificationListener)
            .build()
            .also {
                it.setMediaSessionToken(mediaSession.sessionCompatToken)
                it.setUseFastForwardActionInCompactView(false)
                it.setUseRewindActionInCompactView(false)
                it.setUseNextActionInCompactView(true)
                it.setUsePreviousActionInCompactView(true)
                it.setUseNextActionInCompactView(true)
                it.setUsePreviousAction(true)
                it.setUseNextAction(true)
                it.setUseStopAction(false)
                it.setPriority(NotificationCompat.PRIORITY_LOW)
                it.setPlayer(exoPlayer)
            }
    }


    fun stopNotification() {
        playerNotificationManager?.setPlayer(null)
    }

    @VisibleForTesting(otherwise = PRIVATE)
    @RequiresApi(Build.VERSION_CODES.O)
    internal fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
}