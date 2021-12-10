package com.example.pdp_ai.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.pdp_ai.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val top = AnimationUtils.loadAnimation(this, R.anim.top)
        val bottom  = AnimationUtils.loadAnimation(this, R.anim.bottom)

        imageView.startAnimation(bottom)
        textView.startAnimation(top)

        val splashTime = 3000
        Handler().postDelayed({
            startActivity(Intent(this, ChatScreen::class.java))
            finish()

        }, splashTime.toLong())

    }
}

