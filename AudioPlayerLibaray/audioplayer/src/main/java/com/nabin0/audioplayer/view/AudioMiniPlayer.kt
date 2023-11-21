package com.nabin0.audioplayer.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.media3.common.MediaMetadata
import com.bumptech.glide.Glide
import com.nabin0.audio_player.R
import com.vl.viewlift.playersdk.audioplayer.events.AudioPlayerEventBus
import com.vl.viewlift.playersdk.audioplayer.events.AudioPlayerEventListener
import com.vl.viewlift.playersdk.audioplayer.models.Audio
import com.vl.viewlift.playersdk.audioplayer.utils.ExoPlayerHelper
import com.vl.viewlift.playersdk.audioplayer.view.AudioPlayerHelper

/**
 * Default view class for the bottom mini player for the audioPlayer listens to the Events Emitted by AudioPlayerHelper same as the AudioPlayer class
 *
 * It also provides method to change layout, use setCustomLayout(layoutResourceId: Int) parameter layoutResourceId is id of the resource file you want to apply
 */
class AudioMiniPlayer : FrameLayout, AudioPlayerEventListener {
    private var mContext: Context
    private lateinit var audioPlayerHelper: AudioPlayerHelper

    private var albumImage: ImageView? = null
    private var playButton: AppCompatImageButton? = null
    private var pauseButton: AppCompatImageButton? = null
    private var nextButton: AppCompatImageButton? = null
    private var audioSeekBar: SeekBar? = null
    private var textAudioTitle: TextView? = null
    private var textArtist: TextView? = null
    private var progressBar: ProgressBar? = null

    constructor(context: Context) : super(context) {
        this.mContext = context
        initializePlayerView(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.mContext = context
        initializePlayerView(null)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        this.mContext = context
        initializePlayerView(null)
    }

    init {
        AudioPlayerEventBus.instance.registerReceiver(this)
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun initializePlayerView(layoutResourceId: Int?) {
        audioPlayerHelper = ExoPlayerHelper.getAudioPlayerHelperInstance(mContext)
        val layoutId = layoutResourceId ?: R.layout.audio_mini_player_layout
        LayoutInflater.from(mContext).inflate(layoutId, this)

        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        audioSeekBar = findViewById(R.id.audioSeekBar)
        albumImage = findViewById(R.id.albumImage)
        nextButton = findViewById(R.id.nextButton)
        textAudioTitle = findViewById(R.id.textAudioTitle)
        textArtist = findViewById(R.id.textArtist)
        progressBar = findViewById(R.id.progressBar)

        playButton?.setOnClickListener {
            audioPlayerHelper.togglePlayPauseEvent()
        }

        pauseButton?.setOnClickListener {
            audioPlayerHelper.togglePlayPauseEvent()
        }

        nextButton?.setOnClickListener {
            audioPlayerHelper.playNextFromList()
        }

        audioSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    audioPlayerHelper.progressSeekTo(p1.toLong())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        if (audioPlayerHelper.buffering) {
            progressBar?.visibility = View.VISIBLE
            playButton?.visibility = View.GONE
            pauseButton?.visibility = View.GONE
        }

        val mediaMetadata = audioPlayerHelper.player?.mediaMetadata
        setMediaMetadata(mediaMetadata)
    }

    private fun setMediaMetadata(mediaMetadata: MediaMetadata?) {
        if (mediaMetadata == null) return
        if (mediaMetadata.title != null) {
            textAudioTitle?.text = mediaMetadata.title
        } else {
            textAudioTitle?.text = "Unknown"
        }
        if (mediaMetadata.artist != null) {
            textArtist?.text = mediaMetadata.artist
        } else {
            textArtist?.text = "Unknown"
        }

        if (audioPlayerHelper.player?.hasNextMediaItem() == true) {
            nextButton?.isEnabled = true
            nextButton?.visibility = View.VISIBLE
        } else {
            nextButton?.isEnabled = false
            nextButton?.visibility = View.GONE
        }

        albumImage?.let {
            try {
                Glide.with(mContext).load(mediaMetadata.artworkUri)
                    .error(R.drawable.audio_placeholder).into(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        togglePlayPauseButtonResource()
    }

    fun setCustomLayout(layoutResourceId: Int) {
        this.removeAllViewsInLayout()
        initializePlayerView(layoutResourceId)
    }

    fun setAutoPlayEnabled(boolean: Boolean) {
        audioPlayerHelper.setAutoPlayEnabled(boolean)
    }

    fun prepare(audioList: List<Audio>) {
        audioPlayerHelper.prepare()
    }

    fun currentPlayingAudioIndex(): Int {
        return audioPlayerHelper.currentPayingMediaIndex()
    }

    fun initializePlayer() {
        audioPlayerHelper.initializePlayer(mContext)
    }

    fun setPlaylist(audioList: List<Audio>) {
        audioPlayerHelper.setAudioPlaylist(audioList)
    }

    fun addAudioList(audioList: List<Audio>) {
        audioPlayerHelper.addAudioPlaylist(audioList)
    }

    fun isPlaying(): Boolean = audioPlayerHelper.player?.isPlaying ?: false
    private fun togglePlayPauseButtonResource() {
        if (audioPlayerHelper.player?.isPlaying == true) {
            playButton?.visibility = View.GONE
            pauseButton?.visibility = View.VISIBLE
        } else {
            pauseButton?.visibility = View.GONE
            playButton?.visibility = View.VISIBLE
        }
    }

    fun removeCallbacks() {
        AudioPlayerEventBus.instance.unregisterReceiver(this)
    }

    override fun onBuffer() {
        progressBar?.visibility = View.VISIBLE
        togglePlayPauseButtonResource()
    }

    override fun onReady() {
        progressBar?.visibility = View.GONE
        togglePlayPauseButtonResource()
    }

    override fun onMetadataChanged(mediaMetadata: MediaMetadata) {
        setMediaMetadata(mediaMetadata)
    }

    override fun onUpdateSeekbar(currentPosition: Long, totalDuration: Long) {
        audioSeekBar?.max = totalDuration.toInt()
        audioSeekBar?.progress = currentPosition.toInt()
    }

    override fun onPlay() {
        togglePlayPauseButtonResource()
    }

    override fun onPause() {
        togglePlayPauseButtonResource()
    }

    fun destroy() {
        audioPlayerHelper.destroy()
    }

    override fun onDetachedFromWindow() {
        AudioPlayerEventBus.instance.unregisterReceiver(this)
        super.onDetachedFromWindow()
    }

}