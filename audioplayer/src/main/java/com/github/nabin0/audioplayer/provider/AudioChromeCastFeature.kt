package com.github.nabin0.audioplayer.provider

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.util.UnstableApi
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.CastState
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.images.WebImage
import com.github.nabin0.audioplayer.view.AudioPlayer
import com.github.nabin0.audioplayer.view.extended.ExpandedControlsActivity


@UnstableApi
object AudioChromeCastFeature {

    private var castContext: CastContext? = null
    private var mCastSession: CastSession? = null
    private var mSessionManagerListener: SessionManagerListener<CastSession>? = null
    private var streamingURL: String? = null
    private var thumbImageUrl: String? = null
    private var audioTitle: String? = null
    private var audioArtist: String? = null
    lateinit var activityInstance: FragmentActivity


    fun init(activityInstance: FragmentActivity) {
        AudioChromeCastFeature.activityInstance = activityInstance
        castContext = CastContext.getSharedInstance(AudioChromeCastFeature.activityInstance)
        mCastSession = castContext!!.sessionManager.currentCastSession

        castContext?.addCastStateListener { state ->
            when (state) {
                CastState.CONNECTED -> {
                    Log.d("TAG", "connected state $state")
                }

                CastState.CONNECTING -> {
                    Log.d("TAG", "connecting state $state")
                }

                CastState.NO_DEVICES_AVAILABLE -> {
                    Log.d("TAG", "No device available state $state")
                }

                CastState.NOT_CONNECTED -> {
                    Log.d("TAG", "not connected state $state")
                }
            }
        }

    }

    private var isChromeCastEnable = false

    fun isChromeCastEnable() = isChromeCastEnable

    fun setChromeCast(feature: Boolean, receiverId: String?) {
        isChromeCastEnable = feature
    }

    fun setUpMediaRouteButton(mediaRouteButton: MediaRouteButton) {
        CastButtonFactory.setUpMediaRouteButton(activityInstance, mediaRouteButton)
    }

    fun registerSessionListener() {
        mSessionManagerListener?.let {
            castContext?.sessionManager?.addSessionManagerListener(
                it, CastSession::class.java
            )
        }
    }

    fun unregisterSessionListener() {
        mSessionManagerListener?.let {
            castContext?.sessionManager?.removeSessionManagerListener(
                it, CastSession::class.java
            )
        }
    }

    fun setupCastListener(audioPlayer: AudioPlayer) {
        mSessionManagerListener = object : SessionManagerListener<CastSession> {
            override fun onSessionEnded(session: CastSession, error: Int) {
                onApplicationDisconnected()
            }

            override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
                onApplicationConnected(session)
            }

            override fun onSessionResumeFailed(session: CastSession, error: Int) {
                onApplicationDisconnected()
            }

            override fun onSessionStarted(session: CastSession, sessionId: String) {
                onApplicationConnected(session)
            }

            override fun onSessionStartFailed(session: CastSession, error: Int) {
                onApplicationDisconnected()
            }

            override fun onSessionStarting(session: CastSession) {}
            override fun onSessionEnding(session: CastSession) {}
            override fun onSessionResuming(session: CastSession, sessionId: String) {}
            override fun onSessionSuspended(session: CastSession, reason: Int) {
                Toast.makeText(activityInstance, reason.toString(), Toast.LENGTH_LONG).show()
            }

            private fun onApplicationConnected(castSession: CastSession) {
                mCastSession = castSession
                // if (audioPlayer.isPlaying())
                loadRemoteMedia(true)
                return
            }

            private fun onApplicationDisconnected() {
            }
        }
    }

    fun isConnected() = mCastSession?.isConnected ?: false

    fun loadRemoteMedia(autoPlay: Boolean) {
        if (mCastSession == null) {
            return
        }
        val remoteMediaClient = mCastSession!!.remoteMediaClient ?: return
        remoteMediaClient.registerCallback(object : RemoteMediaClient.Callback() {
            override fun onStatusUpdated() {
                val intent = Intent(activityInstance, ExpandedControlsActivity::class.java)
                activityInstance.startActivity(intent)
                remoteMediaClient.unregisterCallback(this)
            }
        })

        remoteMediaClient.load(
            MediaLoadRequestData.Builder()
                .setMediaInfo(buildMediaInfo())
                .setAutoplay(autoPlay)
                .build()
        )
    }


    private fun buildMediaInfo(): MediaInfo? {
        val mediaMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        mediaMetadata.putString(MediaMetadata.KEY_TITLE, audioTitle ?: "Unknown")
        mediaMetadata.putString(MediaMetadata.KEY_ARTIST, audioArtist ?: "Unknown");
        thumbImageUrl?.let {

            mediaMetadata.addImage(WebImage(Uri.parse(it)))
        }

        val mediainfo = streamingURL?.let {
            MediaInfo.Builder(it)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("audio/mp3")
                .setMetadata(mediaMetadata)
                .build()
        }
        return mediainfo
    }

    fun setUpAudioDetails(
        streamingURL: String?,
        thumbImageUrl: String?,
        audioTitle: String?,
        artist: String?,
    ) {
        AudioChromeCastFeature.streamingURL = streamingURL
        AudioChromeCastFeature.thumbImageUrl = thumbImageUrl
        AudioChromeCastFeature.audioTitle = audioTitle
        audioArtist = artist
    }

}