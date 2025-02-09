package com.example.hospitalapplication

import android.app.Application
import android.content.res.Configuration
import com.example.hospitalapplication.utils.MySharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application() {
    override fun onCreate() {
        super.onCreate()
        MySharedPreferences.init(this)
    }
}