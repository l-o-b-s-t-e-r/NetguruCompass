package com.compass.netguru.netgurucompass.di

import android.app.Application
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

}