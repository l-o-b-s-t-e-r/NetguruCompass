package com.compass.netguru.netgurucompass.ui.dialog

import android.location.Location

interface DialogCallback {

    fun setNewLocation(location: Location)

}