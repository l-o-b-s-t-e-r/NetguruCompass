package com.compass.netguru.netgurucompass.di

import android.app.Application
import android.hardware.SensorManager
import android.support.v4.content.ContextCompat
import com.compass.netguru.netgurucompass.utils.CompassManager
import com.compass.netguru.netgurucompass.utils.ICompassManager
import com.patloew.rxlocation.RxLocation
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule (private val app: Application) {

    @Provides
    @Singleton
    fun provideApplications() = app

    @Provides
    @Singleton
    fun provideRxLocations(app: Application) = RxLocation(app)

    @Provides
    @Singleton
    fun provideSensorManager(app: Application): SensorManager? {
        return ContextCompat.getSystemService(app, SensorManager::class.java)
    }

    @Provides
    @Singleton
    fun provideCompassManager(sensorManager: SensorManager?): ICompassManager {
        return CompassManager(sensorManager)
    }
}