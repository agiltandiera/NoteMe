package com.tandiera.project.noteme.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.btmNavMain
    }
}