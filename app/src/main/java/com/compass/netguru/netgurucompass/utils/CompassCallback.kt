package com.compass.netguru.netgurucompass.utils

interface CompassCallback {
    
    fun onAzimuthChanged(previousAzimuth: Int, newAzimuth: Int)
    
}