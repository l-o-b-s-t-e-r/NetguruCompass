package com.compass.netguru.netgurucompass.presenters

import com.tbruyelle.rxpermissions2.RxPermissions

interface ICompassPresenter {

    interface View {

        fun changeCompass(previousAzimuth: Int, newAzimuth: Int)

        fun changeDirection(previousAzimuth: Int, newAzimuth: Int)

    }

    interface Actions {

        var view: View?

        fun enableSensorsUpdates()

        fun disableSensorsUpdates()

        fun loadCurrentLocation(rxPermissions: RxPermissions)

        fun dispose()

    }

}
