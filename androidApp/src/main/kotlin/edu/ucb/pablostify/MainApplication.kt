package edu.ucb.pablostify

import android.app.Application
import edu.ucb.pablostify.di.initKoinAndroid

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
    }
}
