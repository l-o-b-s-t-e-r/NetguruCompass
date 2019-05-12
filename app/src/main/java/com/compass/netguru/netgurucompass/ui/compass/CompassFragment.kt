package com.compass.netguru.netgurucompass.ui.compass

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.compass.netguru.netgurucompass.App
import com.compass.netguru.netgurucompass.R
import com.compass.netguru.netgurucompass.presenters.compass.CompassPresenter
import com.compass.netguru.netgurucompass.presenters.compass.ICompassPresenter
import com.compass.netguru.netgurucompass.ui.dialog.DialogCallback
import com.compass.netguru.netgurucompass.ui.dialog.LocationDialog
import com.compass.netguru.netgurucompass.utils.showLongToast
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_compass.*
import javax.inject.Inject


class CompassFragment : Fragment(), ICompassPresenter.View,
    DialogCallback {

    @Inject
    lateinit var presenter: CompassPresenter
    lateinit var rxPermissions: RxPermissions

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.appComponent.inject(this)
        rxPermissions = RxPermissions(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_compass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.view = this
        btnSetTargetLocation.setOnClickListener {
            rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        LocationDialog.newInstance(presenter.targetLocation)
                            .show(childFragmentManager, LocationDialog::class.java.simpleName)
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.enableSensorsUpdates()
    }

    override fun onStop() {
        super.onStop()
        presenter.disableSensorsUpdates()
        presenter.dispose()
    }

    override fun changeCompass(newAzimuth: Int) {
        compass.rotateArrow(newAzimuth)
    }

    override fun changeDirection(newAzimuth: Int) {
        compass.rotateDirection(newAzimuth)
    }

    @SuppressLint("CheckResult")
    override fun setNewLocation(location: Location) {
        presenter.dispose()
        presenter.targetLocation = location
        presenter.loadCurrentLocation(rxPermissions)
    }

    override fun showToast(resId: Int) {
        showLongToast(resId)
    }

    override fun onDestroyView() {
        presenter.view = null
        super.onDestroyView()
    }
}