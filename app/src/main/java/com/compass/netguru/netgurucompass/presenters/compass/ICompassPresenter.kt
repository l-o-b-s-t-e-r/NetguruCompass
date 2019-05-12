package com.compass.netguru.netgurucompass.presenters.compass

import android.location.Location
import com.tbruyelle.rxpermissions2.RxPermissions

interface ICompassPresenter {

    interface View {

        fun changeCompass(newAzimuth: Int)

        fun changeDirection(newAzimuth: Int)

        fun showToast(resId: Int)

    }

    interface Actions {

        var targetLocation: Location?

        var view: View?

        fun enableSensorsUpdates()

        fun disableSensorsUpdates()

        fun loadCurrentLocation(rxPermissions: RxPermissions)

        fun dispose()

    }

}
