package com.compass.netguru.netgurucompass.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.compass.netguru.netgurucompass.R
import com.compass.netguru.netgurucompass.ui.compass.CompassFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.rootContainer, CompassFragment())
            .commit()
    }
}
