package com.nomade.miremis

import android.app.Application
import com.bugfender.android.BuildConfig
import com.bugfender.sdk.Bugfender

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Bugfender.init(this, "7eGFNcYKmYrczgwn8OqkC2CmiYUGh4iC", BuildConfig.DEBUG, true)
        Bugfender.enableCrashReporting()
        Bugfender.enableUIEventLogging(this)
        //Bugfender.enableLogcatLogging() // optional, if you want logs automatically collected from logcat
    }
}