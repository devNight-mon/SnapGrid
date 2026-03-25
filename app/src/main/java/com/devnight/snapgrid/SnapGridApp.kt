package com.devnight.snapgrid

import android.app.Application

class SnapGridApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: SnapGridApp
            private set
    }
}