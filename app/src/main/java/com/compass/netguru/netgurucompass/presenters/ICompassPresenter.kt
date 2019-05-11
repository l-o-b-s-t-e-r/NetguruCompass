package com.compass.netguru.netgurucompass.presenters

import com.tbruyelle.rxpermissions2.RxPermissions

interface ICompassPresenter {

    interface View {

    }

    interface Actions {

        var view: View?

        fun loadCurrentLocation(rxPermissions: RxPermissions)

    }

}
