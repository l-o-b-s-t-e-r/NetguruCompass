package com.compass.netguru.netgurucompass.presenters.dialog

interface ILocationPresenter {

    interface View {

        fun latitudeIsNotValid()

        fun longitudeIsNotValid()

        fun showToast(resId: Int)
    }

    interface Actions {

        fun locationIsValid(latitude: String, longitude: String): Boolean

    }

}