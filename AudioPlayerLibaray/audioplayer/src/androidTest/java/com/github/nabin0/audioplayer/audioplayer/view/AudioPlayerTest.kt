package com.github.nabin0.audioplayer.audioplayer.view

import android.content.Context
import android.view.View
import androidx.media3.exoplayer.ExoPlayer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioPlayerTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var mContext: Context

    @MockK
    lateinit var audioPlayerHelper: com.github.nabin0.audioplayer.view.AudioPlayerHelper

    private lateinit var audioPlayer: com.github.nabin0.audioplayer.view.AudioPlayer

    @MockK
    lateinit var simpleExoPlayer: ExoPlayer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mContext = ApplicationProvider.getApplicationContext()
        every { audioPlayerHelper.player }.returns(simpleExoPlayer)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer =
                com.github.nabin0.audioplayer.view.AudioPlayer(mContext.applicationContext)
        }
        every { audioPlayerHelper.initializePlayer(mContext) }.returns(Unit)
    }

    @Test
    fun test_initializePlayerView() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            audioPlayer.initializePlayer()
        }
        Assert.assertNotNull(audioPlayer)
        Assert.assertTrue(audioPlayer is View)
    }


}