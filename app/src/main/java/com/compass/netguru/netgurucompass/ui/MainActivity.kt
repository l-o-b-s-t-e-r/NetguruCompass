package com.compass.netguru.netgurucompass.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.compass.netguru.netgurucompass.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.rootContainer, CompassFragment())
            .commit()
    }
}
