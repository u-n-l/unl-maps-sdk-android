package com.app.unl_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.unl_map_sdk.UnlMap

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UnlMap(applicationContext)
        setContentView(R.layout.activity_main)
    }
}