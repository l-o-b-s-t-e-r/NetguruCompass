package com.compass.netguru.netgurucompass.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.compass.netguru.netgurucompass.App
import com.compass.netguru.netgurucompass.presenters.CompassPresenter
import com.compass.netguru.netgurucompass.presenters.ICompassPresenter
import com.compass.netguru.netgurucompass.R
import javax.inject.Inject

class CompassFragment : Fragment(), ICompassPresenter.View {

    @Inject
    lateinit var presenter: CompassPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_compass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.view = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.view = null
    }
}