package com.example.puzzleexam

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
    }

    fun Play (view: View){
        val PlayIntent = Intent(this,LevelsActivity::class.java)
        @SuppressLint("ResourceType")
        val animAlpha: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha)
        view.startAnimation(animAlpha)

        startActivity(PlayIntent)
    }

}
