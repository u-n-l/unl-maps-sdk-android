package com.app.unl_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.unl_map_sdk.MyMapBox

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyMapBox(applicationContext)
        setContentView(R.layout.activity_main)
    }


}