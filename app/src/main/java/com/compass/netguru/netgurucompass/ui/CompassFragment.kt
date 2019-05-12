package com.compass.netguru.netgurucompass.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.compass.netguru.netgurucompass.App
import com.compass.netguru.netgurucompass.R
import com.compass.netguru.netgurucompass.presenters.CompassPresenter
import com.compass.netguru.netgurucompass.presenters.ICompassPresenter
import com.compass.netguru.netgurucompass.utils.startRotateAnimation
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_compass.*
import javax.inject.Inject


class CompassFragment : Fragment(), ICompassPresenter.View {

    companion object {
        private const val ANIMATION_DURATION = 200L
    }

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
    }

    @SuppressLint("CheckResult")
    override fun onStart() {
        super.onStart()
        presenter.enableSensorsUpdates()
        rxPermissions
            .request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { granted ->
                if (granted) {
                    presenter.loadCurrentLocation(rxPermissions)
                }
            }
    }

    override fun onStop() {
        super.onStop()
        presenter.disableSensorsUpdates()
        presenter.dispose()
    }

    override fun changeCompass(previousAzimuth: Int, newAzimuth: Int) {
        if (Math.abs(newAzimuth- previousAzimuth) > 180)
            return

        direction.startRotateAnimation(previousAzimuth, newAzimuth, ANIMATION_DURATION, LinearInterpolator())
    }

    override fun changeDirection(previousAzimuth: Int, newAzimuth: Int) {
        if (Math.abs(newAzimuth- previousAzimuth) > 180)
            return

        arrow.startRotateAnimation(previousAzimuth, newAzimuth, ANIMATION_DURATION, LinearInterpolator())
    }

    override fun onDestroyView() {
        presenter.view = null
        super.onDestroyView()
    }
}