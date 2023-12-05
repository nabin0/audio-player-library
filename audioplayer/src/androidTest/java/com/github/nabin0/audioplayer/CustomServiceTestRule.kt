package com.github.nabin0.audioplayer

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.media3.session.MediaSessionService
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ServiceTestRule
import java.util.concurrent.TimeoutException

class CustomServiceTestRule : ServiceTestRule() {

    override fun bindService(intent: Intent): IBinder? {
        val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val service = super.bindService(intent)

        // Cancel any notifications posted by the service
        val mediaSessionService = service as MediaSessionService
        mediaSessionService.stopForeground(true)

        return service

    }

    @Throws(TimeoutException::class)
    override fun startService(intent: Intent) {
        val service = super.startService(intent)
        // Allow some time for the service to be started
        Thread.sleep(2000)
        return service
    }
}