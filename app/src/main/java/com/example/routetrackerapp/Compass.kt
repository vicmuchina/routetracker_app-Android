package com.example.routetrackerapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import kotlinx.android.synthetic.main.activity_compass.*

class Compass : AppCompatActivity() ,SensorEventListener {

    //record the compass angle picture angle turned
    private var currentDegree = 0f

    //device sensor manager
    private var mSensorManager : SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //hide action bar
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        initData()
    }

    private fun initData() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        @Suppress("DEPRECATION")
        mSensorManager?.registerListener(this,
                mSensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME
        )
        
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val degree = Math.round(event?.values?.get(0)!!)

        currentDegree = (-degree) .toFloat()

        val rotateAnimation = RotateAnimation(
                currentDegree,
                (-degree).toFloat(),
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        )

        rotateAnimation.duration = 5
        rotateAnimation.fillAfter = true

        compass.startAnimation(rotateAnimation)


        degrees.text = degree.toString() + " degrees from North clockwise"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}