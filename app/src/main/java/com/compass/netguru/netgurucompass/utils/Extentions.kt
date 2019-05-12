package com.compass.netguru.netgurucompass.utils

import android.location.Location
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast

fun Location(lat: Double, lng: Double): Location {
    return Location("").apply {
        latitude = lat
        longitude = lng
    }
}

fun EditText.setOnTextChangeListener(action: () -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action.invoke()
        }
    })
}

fun Fragment.showLongToast(resId: Int) {
    Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
}