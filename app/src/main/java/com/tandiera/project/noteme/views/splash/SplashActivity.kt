package com.tandiera.project.noteme.views.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.databinding.ActivitySplashBinding
import com.tandiera.project.noteme.views.main.MainActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Buat function handler
        Handler(Looper.getMainLooper()).postDelayed({
            // use anko
            startActivity<MainActivity>()
        }, 1200)
    }
}