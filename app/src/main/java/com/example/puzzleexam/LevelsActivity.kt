package com.example.puzzleexam

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

class LevelsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels)


    }

    fun Level_1(view: View) {
        val Level1Intent = Intent(this, LevelActivity1::class.java)
        @SuppressLint("ResourceType")
        val animAlpha: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha)
        view.startAnimation(animAlpha)

        startActivity(Level1Intent)
    }
}
