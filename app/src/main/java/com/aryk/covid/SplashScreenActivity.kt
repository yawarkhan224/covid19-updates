package com.aryk.covid

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SplashScreenActivity : AppCompatActivity() {
    companion object {
        private const val SPLASH_SCREEN_DELAY_MS = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                // close this activity
            }, SPLASH_SCREEN_DELAY_MS
        )
    }
}
