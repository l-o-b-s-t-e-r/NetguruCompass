package com.compass.netguru.netgurucompass.presenters

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import com.compass.netguru.netgurucompass.utils.CompassCallback
import com.compass.netguru.netgurucompass.utils.ICompassManager
import com.compass.netguru.netgurucompass.utils.Location
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

    private val locationRequest: LocationRequest
    private var locationDisposable: Disposable? = null
    private var previousDirectionAzimuth = 0
    private var currentDirectionAzimuth = 0
    private var currentAzimuth = 0
    private val destLocation = Location(51.148829, 17.078417) //Север
    //val destLocation = Location(51.136459, 17.029354) //Северо-Запад
    //val destLocation = Location(51.104407, 16.971289) //Запад
    //val destLocation = Location(51.073940, 17.010647) //Юго-Запад
    //val destLocation = Location(51.078562, 17.085919) //Юг
    //val destLocation = Location(51.078648, 17.135571) //Юго-Восток
    //val destLocation = Location(51.101579, 17.157336) //Восток
    //val destLocation = Location(51.136371, 17.141814) //Северо-Восток

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
        view?.changeCompass(previousAzimuth, newAzimuth)
        view?.changeDirection(previousDirectionAzimuth, newAzimuth + currentDirectionAzimuth)
        currentAzimuth = newAzimuth
        previousDirectionAzimuth = newAzimuth + currentDirectionAzimuth
    }

    override fun loadCurrentLocation(rxPermissions: RxPermissions) {
        if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationDisposable = rxLocation.settings().checkAndHandleResolution(locationRequest)
                .flatMapObservable { isSuccess -> getLocationObservable(isSuccess) }
                .subscribe({ location ->
                    currentDirectionAzimuth = convertToAbsoluteAzimuth(location.bearingTo(destLocation).toInt())
                    previousDirectionAzimuth = currentAzimuth + currentDirectionAzimuth
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

    override fun dispose() {
        locationDisposable?.dispose()
    }
}