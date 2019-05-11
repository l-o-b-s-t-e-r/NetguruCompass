package com.compass.netguru.netgurucompass.presenters

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CompassPresenter @Inject constructor(private val rxLocation: RxLocation) :
    ICompassPresenter.Actions {

    private val locationRequest: LocationRequest
    private var locationDisposable: Disposable? = null
    override var view: ICompassPresenter.View? = null

    init {
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setNumUpdates(1)
    }

    override fun loadCurrentLocation(rxPermissions: RxPermissions) {
        if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationDisposable = rxLocation.settings().checkAndHandleResolution(locationRequest)
                .flatMapObservable { isSuccess -> getLocationObservable(isSuccess) }
                .subscribe({ location ->
                    Log.e("MyLocation", "${location.latitude} - ${location.longitude}")
                }, { e ->
                    Log.e("MyLocation", "ERROR")
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
}