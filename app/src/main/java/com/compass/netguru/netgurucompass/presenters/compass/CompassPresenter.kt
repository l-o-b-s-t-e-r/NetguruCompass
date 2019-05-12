package com.compass.netguru.netgurucompass.presenters.compass

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import com.compass.netguru.netgurucompass.R
import com.compass.netguru.netgurucompass.utils.CompassCallback
import com.compass.netguru.netgurucompass.utils.ICompassManager
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CompassPresenter @Inject constructor(
    private val rxLocation: RxLocation,
    private val compassManager: ICompassManager
) : ICompassPresenter.Actions, CompassCallback {

    override var view: ICompassPresenter.View? = null
    override var targetLocation: Location? = null

    private val locationRequest: LocationRequest
    private var locationDisposable: Disposable? = null
    private var currentDirectionAzimuth: Int? = null

    init {
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(1000)

        compassManager.callback = this
    }

    override fun enableSensorsUpdates() {
        compassManager.startListenSensors()
    }

    override fun disableSensorsUpdates() {
        compassManager.stopListenSensors()
    }

    override fun onAzimuthChanged(previousAzimuth: Int, newAzimuth: Int) {
        view?.changeCompass(newAzimuth)
        if (targetLocation != null && currentDirectionAzimuth != null) {
            view?.changeDirection(newAzimuth + currentDirectionAzimuth!!)
        }
    }

    override fun loadCurrentLocation(rxPermissions: RxPermissions) {
        if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationDisposable = rxLocation.settings().checkAndHandleResolution(locationRequest)
                .flatMapObservable { isSuccess -> getLocationObservable(isSuccess) }
                .subscribe({ location ->
                    currentDirectionAzimuth = convertToAbsoluteAzimuth(location.bearingTo(targetLocation).toInt())
                }, { e ->
                    e.printStackTrace()
                })
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocationObservable(success: Boolean): Observable<Location> {
        return if (success) {
            rxLocation.location().updates(locationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        } else {
            rxLocation.location().lastLocation()
                .toObservable()
        }
    }

    private fun convertToAbsoluteAzimuth(azimuth: Int): Int {
        return if (azimuth < 0) {
            -azimuth
        } else {
            360 - azimuth
        }
    }

    override fun sensorsUnavailable() {
        view?.showToast(R.string.toast_sensors_unavailable)
    }

    override fun dispose() {
        locationDisposable?.dispose()
    }
}