package com.compass.netguru.netgurucompass.ui.dialog

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.BounceInterpolator
import android.widget.EditText
import android.widget.TextView
import com.compass.netguru.netgurucompass.R
import com.compass.netguru.netgurucompass.presenters.dialog.ILocationPresenter
import com.compass.netguru.netgurucompass.presenters.dialog.LocationPresenter
import com.compass.netguru.netgurucompass.utils.Location
import com.compass.netguru.netgurucompass.utils.setOnTextChangeListener
import com.compass.netguru.netgurucompass.utils.showLongToast
import kotlinx.android.synthetic.main.fragment_set_location.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.support.v4.withArguments

class LocationDialog : DialogFragment(), ILocationPresenter.View {

    companion object {
        private const val TAG_INVALID_INPUT = "tag_invalid_input"
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"

        fun newInstance(location: Location?): DialogFragment {
            return LocationDialog().withArguments(
                Pair(LATITUDE, location?.latitude?.toString() ?: ""),
                Pair(LONGITUDE, location?.longitude?.toString() ?: "")
            )
        }
    }

    private lateinit var presenter: ILocationPresenter.Actions
    private lateinit var callback: DialogCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = LocationPresenter(this)
        if (parentFragment is DialogCallback) {
            callback = parentFragment as DialogCallback
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_set_location, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editLatitude.setText(arguments?.getString(LATITUDE), TextView.BufferType.EDITABLE)
        editLongitude.setText(arguments?.getString(LONGITUDE), TextView.BufferType.EDITABLE)

        btnApply.setOnClickListener {
            if (presenter.locationIsValid(editLatitude.text.toString(), editLongitude.text.toString())) {
                callback.setNewLocation(
                    Location(
                        editLatitude.text.toString().toDouble(),
                        editLongitude.text.toString().toDouble()
                    )
                )

                dismiss()
            }
        }

        editLatitude.setOnTextChangeListener {
            if (editLatitude.tag == TAG_INVALID_INPUT) {
                releaseField(editLatitude)
            }
        }

        editLongitude.setOnTextChangeListener {
            if (editLongitude.tag == TAG_INVALID_INPUT) {
                releaseField(editLongitude)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onResume() {
        super.onResume()
        ObjectAnimator.ofPropertyValuesHolder(
            icon_top,
            PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f),
            PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f)
        ).apply {
            duration = 1500
            interpolator = BounceInterpolator()
        }.start()
    }

    override fun latitudeIsNotValid() {
        editLatitude.backgroundResource = R.drawable.edit_text_error_background
        editLatitude.tag = TAG_INVALID_INPUT
    }

    override fun longitudeIsNotValid() {
        editLongitude.backgroundResource = R.drawable.edit_text_error_background
        editLongitude.tag = TAG_INVALID_INPUT
    }

    override fun showToast(resId: Int) {
        showLongToast(resId)
    }

    private fun releaseField(field: EditText) {
        field.apply {
            tag = null
            backgroundResource = R.drawable.edit_text_background
        }
    }
}