package com.github.nabin0.audioplayer.audioplayer.broadcast_receiver

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.nabin0.audioplayer.service.AudioPlayerService
import com.github.nabin0.audioplayer.utils.Constants
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HeadsetBroadcastReceiverTest {

    @MockK
    private lateinit var mContext: Context

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        val mockResources = mockk<Resources>()
        every { mContext.resources }.returns(mockResources)
        every { mContext.packageName }.returns("com.nabin0.playersdk.audioplayer")
        every { mContext.startService(any()) }.returns(null)
    }

    @Test
    fun test_HeadsetPluggedIn() {
        val intent = Intent(mContext, com.github.nabin0.audioplayer.service.AudioPlayerService::class.java)
        intent.action = Intent.ACTION_HEADSET_PLUG
        intent.putExtra("state", 1)
        val receiver = com.github.nabin0.audioplayer.broadcast_receiver.HeadsetBroadcastReceiver()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            receiver.onReceive(mContext, intent)
        }

        val serviceIntent = Intent(mContext, com.github.nabin0.audioplayer.service.AudioPlayerService::class.java)
        serviceIntent.putExtra(com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION, com.github.nabin0.audioplayer.utils.Constants.PLAY)

        verify {
            mContext.startService(match {
                it.action == serviceIntent.action &&
                it.hasExtra(com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION) == serviceIntent.hasExtra(
                    com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION) &&
                it.getStringExtra(com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION) == serviceIntent.getStringExtra(
                    com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION)
            })
        }
    }

    @Test
    fun test_HeadsetPluggedOut() {
        // Turn variable: headsetPluggedIn to true
        val headSetPluggedInIntent = Intent(mContext, com.github.nabin0.audioplayer.service.AudioPlayerService::class.java)
        headSetPluggedInIntent.action = Intent.ACTION_HEADSET_PLUG
        headSetPluggedInIntent.putExtra("state", 1)
        val receiver = com.github.nabin0.audioplayer.broadcast_receiver.HeadsetBroadcastReceiver()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            receiver.onReceive(mContext, headSetPluggedInIntent)
        }

        // Test Headset Plugged Out
        val intent = Intent(mContext, com.github.nabin0.audioplayer.service.AudioPlayerService::class.java)
        intent.action = Intent.ACTION_HEADSET_PLUG
        intent.putExtra("state", 0)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            receiver.onReceive(mContext, intent)
        }

        val serviceIntent = Intent(mContext, com.github.nabin0.audioplayer.service.AudioPlayerService::class.java)
        serviceIntent.putExtra(com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION, com.github.nabin0.audioplayer.utils.Constants.PAUSE)

        verify {
            mContext.startService(match {
                it.action == serviceIntent.action &&
                it.hasExtra(com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION) == serviceIntent.hasExtra(
                    com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION) &&
                it.getStringExtra(com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION) == serviceIntent.getStringExtra(
                    com.github.nabin0.audioplayer.utils.Constants.HEADSET_ACTION)
            })
        }
    }

}