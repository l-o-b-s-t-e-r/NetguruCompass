package com.compass.netguru.netgurucompass.presenters.dialog

import com.compass.netguru.netgurucompass.R

class LocationPresenter(private val view: ILocationPresenter.View) :
    ILocationPresenter.Actions {

    override fun locationIsValid(latitude: String, longitude: String): Boolean {
        if (latitude.isEmpty()) {
            view.showToast(R.string.toast_field_is_empty)
            view.latitudeIsNotValid()
            return false
        }

        if (longitude.isEmpty()) {
            view.showToast(R.string.toast_field_is_empty)
            view.longitudeIsNotValid()
            return false
        }

        val latitudeFloat = latitude.toFloatOrNull()
        if (latitudeFloat == null) {
            view.showToast(R.string.toast_illegal_format)
            view.latitudeIsNotValid()
            return false
        }

        val longitudeFloat = longitude.toFloatOrNull()
        if (longitudeFloat == null) {
            view.showToast(R.string.toast_illegal_format)
            view.longitudeIsNotValid()
            return false
        }

        if (latitudeFloat !in -90.0f..90.0f) {
            view.showToast(R.string.toast_latitude_range)
            view.latitudeIsNotValid()
            return false
        }

        if (longitudeFloat !in -180.0f..180.0f) {
            view.showToast(R.string.toast_longitude_range)
            view.longitudeIsNotValid()
            return false
        }

        return true
    }

}