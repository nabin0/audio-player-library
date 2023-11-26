package com.github.nabin0.audioplayer.audioplayer.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import androidx.media3.ui.PlayerNotificationManager.NotificationListener
import androidx.test.core.app.ApplicationProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AudioPlayerNotificationManagerTest {
    private lateinit var mContext: Context

    private lateinit var audioPlayerNotificationManager: com.github.nabin0.audioplayer.notification.AudioPlayerNotificationManager

    @MockK
    lateinit var simpleExoPlayer: ExoPlayer

    @MockK
    lateinit var playerNotificationManager: PlayerNotificationManager

    @MockK
    lateinit var mediaSession: MediaSession

    @MockK
    lateinit var mediaSessionService: MediaSessionService

    @MockK
    lateinit var notificationListenerService: NotificationListener

    @MockK
    lateinit var notificationManager: NotificationManagerCompat

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mContext = ApplicationProvider.getApplicationContext()
        every { simpleExoPlayer.playWhenReady }.returns(false)
        every { simpleExoPlayer.seekTo(any()) }.returns(Unit)
        every { simpleExoPlayer.stop() }.returns(Unit)
        audioPlayerNotificationManager =
            com.github.nabin0.audioplayer.notification.AudioPlayerNotificationManager(
                mContext,
                simpleExoPlayer,
                notificationListenerService
            )
        every { playerNotificationManager.setPlayer(any()) }.returns(Unit)
    }

    @Test
    fun test_createNotificationChannel() {
        val channel = NotificationChannel(
            com.github.nabin0.audioplayer.notification.NOTIFICATION_CHANNEL_ID,
            com.github.nabin0.audioplayer.notification.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        every { notificationManager.createNotificationChannel(channel) }.returns(Unit)
        audioPlayerNotificationManager.notificationManager = notificationManager
        audioPlayerNotificationManager.createNotificationChannel()

        verify {
            notificationManager.createNotificationChannel(channel)
        }
    }

    @Test
    fun test_stopNotification() {
        audioPlayerNotificationManager.playerNotificationManager = playerNotificationManager
        audioPlayerNotificationManager.stopNotification()
        verify { playerNotificationManager.setPlayer(null) }
    }

    @Test
    fun test_startForegroundNotificationService() {
        every { mediaSessionService.startForeground(any(), any()) }.returns(Unit)
        audioPlayerNotificationManager.startForegroundNotificationService(mediaSessionService)
        verify {
            mediaSessionService.startForeground(com.github.nabin0.audioplayer.notification.NOTIFICATION_ID, match {
                it.category == Notification.CATEGORY_SERVICE
            })
        }
    }

    @Test(expected = IllegalStateException::class)
    fun test_buildNotification() {
        val compatSessionToken = mockk<MediaSessionCompat.Token>()
        every { mediaSession.sessionActivity }.returns(null)
        every { mediaSession.sessionCompatToken }.returns(compatSessionToken)
        assertTrue(audioPlayerNotificationManager.playerNotificationManager == null)
        audioPlayerNotificationManager.buildNotification(mediaSession)
        assertNotNull(audioPlayerNotificationManager.playerNotificationManager)
    }
}