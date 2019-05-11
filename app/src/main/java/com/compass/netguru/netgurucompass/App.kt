package com.compass.netguru.netgurucompass

import android.support.multidex.MultiDexApplication
import com.compass.netguru.netgurucompass.di.AppComponent
import com.compass.netguru.netgurucompass.di.AppModule

class App : MultiDexApplication() {

    companion object {
        lateinit var instance: App
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }
}