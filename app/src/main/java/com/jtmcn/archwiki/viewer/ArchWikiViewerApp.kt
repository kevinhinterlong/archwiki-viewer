package com.jtmcn.archwiki.viewer

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree


class ArchwikiViewerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}
