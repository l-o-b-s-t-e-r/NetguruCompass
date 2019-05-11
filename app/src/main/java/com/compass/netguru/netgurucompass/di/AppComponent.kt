package com.compass.netguru.netgurucompass.di

import com.compass.netguru.netgurucompass.ui.CompassFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(fragment: CompassFragment)

}