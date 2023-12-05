package com.github.nabin0.audioplayer.audioplayer.events

import androidx.media3.common.MediaMetadata
import com.github.nabin0.audioplayer.events.AudioPlayerEventBus
import com.github.nabin0.audioplayer.events.AudioPlayerEventListener
import io.mockk.MockKAnnotations
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AudioPlayerEventBusTest {

    @MockK
    private lateinit var mockListener: com.github.nabin0.audioplayer.events.AudioPlayerEventListener

    private lateinit var audioPlayerEventBus: com.github.nabin0.audioplayer.events.AudioPlayerEventBus

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        audioPlayerEventBus = com.github.nabin0.audioplayer.events.AudioPlayerEventBus.instance
        every { mockListener.onPlay() }.returns(Unit)
        every { mockListener.onPause() }.returns(Unit)
        every { mockListener.onBuffer() }.returns(Unit)
        every { mockListener.onReady() }.returns(Unit)
        every { mockListener.onMetadataChanged(any()) }.returns(Unit)
        every { mockListener.onUpdateSeekbar(any(), any()) }.returns(Unit)
    }

    @Test
    fun test_registerAndTriggerPlayEvent() {
        audioPlayerEventBus.registerReceiver(mockListener)
        audioPlayerEventBus.triggerPlayEvent()
        verify { mockListener.onPlay() }
    }

    @Test
    fun test_unregisterAndTriggerPauseEvent() {
        audioPlayerEventBus.registerReceiver(mockListener)
        audioPlayerEventBus.unregisterReceiver(mockListener)

        audioPlayerEventBus.triggerPauseEvent()
        verify { mockListener wasNot called }
    }

    @Test
    fun test_triggerOnBuffer() {
        audioPlayerEventBus.registerReceiver(mockListener)
        audioPlayerEventBus.triggerOnBuffer()
        verify { mockListener.onBuffer() }
    }

    @Test
    fun test_triggerOnReady() {
        audioPlayerEventBus.registerReceiver(mockListener)
        audioPlayerEventBus.triggerOnReady()
        verify { mockListener.onReady() }
    }

    @Test
    fun test_triggerOnMetadataChanged() {
        audioPlayerEventBus.registerReceiver(mockListener)
        val mockMediaMetadata = mockk<MediaMetadata>()
        audioPlayerEventBus.triggerOnMetadataChanged(mockMediaMetadata)

        verify { mockListener.onMetadataChanged(mockMediaMetadata) }
    }

    @Test
    fun test_triggerUpdateSeekBar() {
        audioPlayerEventBus.registerReceiver(mockListener)
        audioPlayerEventBus.onUpdateSeekBar(123, 5000)
        verify { mockListener.onUpdateSeekbar(123, 5000) }
    }
}
