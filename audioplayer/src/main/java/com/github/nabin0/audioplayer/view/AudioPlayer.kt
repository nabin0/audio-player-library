package com.github.nabin0.audioplayer.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaMetadata
import androidx.mediarouter.app.MediaRouteButton
import com.bumptech.glide.Glide
import com.github.nabin0.audioplayer.R
import com.github.nabin0.audioplayer.events.AudioPlayerEventBus
import com.github.nabin0.audioplayer.events.AudioPlayerEventListener
import com.github.nabin0.audioplayer.models.Audio
import com.github.nabin0.audioplayer.provider.AudioChromeCastFeature
import com.github.nabin0.audioplayer.utils.ExoPlayerHelper
import com.github.nabin0.audioplayer.utils.PlaybackMode
import com.github.nabin0.audioplayer.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * AudioPlayer is the custom view for the management of audio with all controls given
 *
 * It provides methods to control creation and destruction of audioplayer instance and foreground service
 *
 *  @param context context of application or activity or FragmentActivity
 *
 */
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
class AudioPlayer : FrameLayout, AudioPlayerEventListener {

    private var activityInstance: FragmentActivity? = null
    private var mContext: Context = context
    private lateinit var audioPlayerHelper: AudioPlayerHelper
    private var inflatedResourceId: Int? = null
    private var audioChromeCastFeature: AudioChromeCastFeature? = null
    var currentPlayingMediaIndex: StateFlow<Int> = MutableStateFlow(-1)

    // Views
    private var albumImage: ImageView? = null
    private var playButton: ImageButton? = null
    private var pauseButton: ImageButton? = null
    private var previousButton: ImageButton? = null
    private var nextButton: ImageButton? = null
    private var audioSeekBar: SeekBar? = null
    private var textCurrentPosition: TextView? = null
    private var textDuration: TextView? = null
    private var progressBar: ProgressBar? = null
    private var textAudioTitle: TextView? = null
    private var textArtist: TextView? = null
    private var playBackModeButton: ImageButton? = null
    private var mediaRouteButton: MediaRouteButton? = null

    init {
        // Register to listen the updates from audio player instance to update the view
        AudioPlayerEventBus.instance.registerReceiver(this)
    }

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


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun initializePlayerView(layoutResourceId: Int?) {
        audioPlayerHelper = ExoPlayerHelper.getAudioPlayerHelperInstance(mContext)

        val layoutId = layoutResourceId ?: R.layout.audio_player_control
        inflatedResourceId = layoutResourceId
        LayoutInflater.from(mContext).inflate(layoutId, this)

        albumImage = findViewById(R.id.albumImage)
        previousButton = findViewById(R.id.previousButton)
        nextButton = findViewById(R.id.nextButton)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        audioSeekBar = findViewById(R.id.audioSeekBar)
        textCurrentPosition = findViewById(R.id.textCurrentPosition)
        textDuration = findViewById(R.id.textAudioDuration)
        progressBar = findViewById(R.id.progressBar)
        textAudioTitle = findViewById(R.id.textAudioTitle)
        textArtist = findViewById(R.id.textArtist)
//        mediaRouteButton = findViewById(R.id.mediaRouteButton)
        playBackModeButton = findViewById(R.id.playBackModeButton)

        val playbackMode = audioPlayerHelper.getCurrentPlaybackMode()
        setPlaybackModeButtonResource(playbackMode)

        playBackModeButton?.setOnClickListener {
            val newPlayBackMode = when (audioPlayerHelper.getCurrentPlaybackMode()) {
                PlaybackMode.NO_REPEAT -> {
                    PlaybackMode.SHUFFLE
                }

                PlaybackMode.SHUFFLE -> {
                    PlaybackMode.REPEAT_PLAYLIST
                }

                PlaybackMode.REPEAT_PLAYLIST -> {
                    PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY
                }

                PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY -> PlaybackMode.NO_REPEAT
            }
            audioPlayerHelper.setPlaybackMode(newPlayBackMode)
            setPlaybackModeButtonResource(newPlayBackMode)

        }

        if (activityInstance != null)
            AudioChromeCastFeature.init(activityInstance as AppCompatActivity)
        if (AudioChromeCastFeature.isChromeCastEnable()) {
            audioChromeCastFeature = AudioChromeCastFeature.apply {
                setupCastListener(this@AudioPlayer)
                registerSessionListener()
            }
        }

        mediaRouteButton?.let {
            AudioChromeCastFeature.setUpMediaRouteButton(it)
        }

        if (audioPlayerHelper.buffering) {
            progressBar?.visibility = View.VISIBLE
        }

        playButton?.setOnClickListener {
            audioPlayerHelper.togglePlayPauseEvent()
        }

        pauseButton?.setOnClickListener {
            audioPlayerHelper.togglePlayPauseEvent()
        }

        nextButton?.setOnClickListener {
            audioPlayerHelper.playNextFromList()
        }

        previousButton?.setOnClickListener {
            audioPlayerHelper.playPreviousFromList()
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

        val mediaMetadata = audioPlayerHelper.player?.mediaMetadata
        setMediaMetadata(mediaMetadata)
    }

    private fun setPlaybackModeButtonResource(playbackMode: PlaybackMode) {
        val playbackModeResource = when (playbackMode) {
            PlaybackMode.SHUFFLE -> R.drawable.shuffle_24
            PlaybackMode.NO_REPEAT -> R.drawable.arrow_right_alt_24
            PlaybackMode.REPEAT_CURRENT_AUDIO_INFINITELY -> R.drawable.repeat_one_24
            PlaybackMode.REPEAT_PLAYLIST -> R.drawable.repeat_24
        }
        playBackModeButton?.setImageResource(playbackModeResource)
    }

    fun setActivityInstance(activityInstance: FragmentActivity) {
        this.activityInstance = activityInstance
    }

    fun setCustomLayout(layoutResourceId: Int) {
        this.removeAllViewsInLayout()
        initializePlayerView(layoutResourceId)
    }

    fun setAutoPlayEnabled(boolean: Boolean) {
        audioPlayerHelper.setAutoPlayEnabled(boolean)
    }

    fun prepare() {
        audioPlayerHelper.prepare()
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

    fun currentPlayingAudioIndex(): Int {
        return audioPlayerHelper.currentPayingMediaIndex()
    }

    fun startForegroundService() {
        audioPlayerHelper.startForegroundService()
    }

    fun destroy() {
        removeCallbacks()
        audioPlayerHelper.destroy()
    }

    fun removeCallbacks() {
        AudioPlayerEventBus.instance.unregisterReceiver(this)
    }

    fun setMediaMetadata(mediaMetadata: MediaMetadata?) {
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
        if (audioPlayerHelper.player?.hasPreviousMediaItem() == true) {
            previousButton?.isEnabled = true
            previousButton?.visibility = View.VISIBLE
        } else {
            previousButton?.isEnabled = false
            previousButton?.visibility = View.INVISIBLE
        }

        if (audioPlayerHelper.player?.hasNextMediaItem() == true) {
            nextButton?.isEnabled = true
            nextButton?.visibility = View.VISIBLE
        } else {
            nextButton?.isEnabled = false
            nextButton?.visibility = View.INVISIBLE
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

        val currentMediaItem = audioPlayerHelper.player?.currentMediaItem

        currentMediaItem?.let { mediaItem ->
            audioChromeCastFeature?.let { chromeCastFeature ->
                mediaItem.localConfiguration?.uri?.let {
                    chromeCastFeature.setUpAudioDetails(
                        streamingURL = it.toString(),
                        thumbImageUrl = mediaMetadata.artworkUri.toString(),
                        audioTitle = mediaMetadata.title.toString(),
                        artist = mediaMetadata.artist.toString()
                    )
                }
            }
        }

    }

    fun isPlaying(): Boolean = audioPlayerHelper.player?.isPlaying ?: false

    fun playMediaItemByIndex(index: Int) {
        audioPlayerHelper.playMediaItemByIndex(index)

    }

    override fun onDetachedFromWindow() {
        AudioPlayerEventBus.instance.unregisterReceiver(this)
        super.onDetachedFromWindow()
    }

    private fun togglePlayPauseButtonResource() {
        if (audioPlayerHelper.player?.isPlaying == true) {
            playButton?.visibility = View.GONE
            pauseButton?.visibility = View.VISIBLE
        } else {
            pauseButton?.visibility = View.GONE
            playButton?.visibility = View.VISIBLE
        }
    }

    fun setChromecastEnabled(value: Boolean, receiverId: String? = null) {
        AudioChromeCastFeature.setChromeCast(true, null)

        if (AudioChromeCastFeature.isChromeCastEnable()) {
            audioChromeCastFeature = AudioChromeCastFeature.apply {
                setupCastListener(this@AudioPlayer)
                registerSessionListener()
            }
        }

        mediaRouteButton?.let {
            AudioChromeCastFeature.setUpMediaRouteButton(it)
        }

    }

    override fun onBuffer() {
        progressBar?.visibility = View.VISIBLE
        togglePlayPauseButtonResource()
    }

    override fun onReady() {
        progressBar?.visibility = View.INVISIBLE
        togglePlayPauseButtonResource()
    }

    override fun onMetadataChanged(mediaMetadata: MediaMetadata) {
        setMediaMetadata(mediaMetadata)
    }

    override fun onUpdateSeekbar(currentPosition: Long, totalDuration: Long) {
        audioSeekBar?.max = totalDuration.toInt()
        audioSeekBar?.progress = currentPosition.toInt()
        textCurrentPosition?.text = Utils.formatTime(currentPosition)
        textDuration?.text = Utils.formatTime(totalDuration)
    }

    override fun onPlay() {
        togglePlayPauseButtonResource()
    }

    override fun onPause() {
        togglePlayPauseButtonResource()
    }

}