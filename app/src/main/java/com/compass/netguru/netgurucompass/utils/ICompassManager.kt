package com.compass.netguru.netgurucompass.utils

interface ICompassManager {

    var callback: CompassCallback?

    fun startListenSensors()

    fun stopListenSensors()

}