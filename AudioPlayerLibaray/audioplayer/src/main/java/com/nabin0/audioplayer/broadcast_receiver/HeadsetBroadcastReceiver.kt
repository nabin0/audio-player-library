package com.vl.viewlift.playersdk.audioplayer.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nabin0.audioplayer.service.AudioPlayerService
import com.vl.viewlift.playersdk.audioplayer.utils.Constants

/**
 * Class to manage player when headset is plugged in and out
 *
 *  If headset is plugged-in current playing audio will be paused else it will be resumed
 */
class HeadsetBroadcastReceiver : BroadcastReceiver() {
    private var headsetPluggedIn = false
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AudioPlayerService::class.java)
        val action = intent.action
        if (action != null) {
            when (action) {
                Intent.ACTION_HEADSET_PLUG -> {
                    val headSetState = intent.getIntExtra("state", -1)
                    if (headSetState == 0 && headsetPluggedIn) {
                        // Toast.makeText(context, "Headset Plugged Out", Toast.LENGTH_SHORT).show()
                        headsetPluggedIn = false
                        serviceIntent.putExtra(Constants.HEADSET_ACTION, Constants.PAUSE)
                        context.startService(serviceIntent)
                    } else if (headSetState == 1 && !headsetPluggedIn) {
                        // Toast.makeText(context, "Headset Plugged In", Toast.LENGTH_SHORT).show()
                        serviceIntent.putExtra(Constants.HEADSET_ACTION, Constants.PLAY)
                        context.startService(serviceIntent)
                        headsetPluggedIn = true
                    }
                }
            }
        }
    }
}