package com.kent.lab.application

import android.app.Application
import android.content.Context
import com.kent.lab.FlipperInitializer

class KLabApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FlipperInitializer.init(this)
    }
}