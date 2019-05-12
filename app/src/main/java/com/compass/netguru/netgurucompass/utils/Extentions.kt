package com.compass.netguru.netgurucompass.utils

import android.location.Location
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.RotateAnimation

fun Location(lat: Double, lng: Double): Location {
    return Location("").apply {
        latitude = lat
        longitude = lng
    }
}

fun View.startRotateAnimation(fromDegrees: Int, toDegrees: Int, duration: Long, interpolator: Interpolator, fillAfter: Boolean = true) {
    val rotateAnimation = RotateAnimation(
        -fromDegrees.toFloat(),
        -toDegrees.toFloat(),
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    ).also {
        it.duration = duration
        it.interpolator = interpolator
        it.fillAfter = fillAfter
    }

    this.startAnimation(rotateAnimation)
}