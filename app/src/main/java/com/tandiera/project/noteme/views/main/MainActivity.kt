package com.tandiera.project.noteme.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.databinding.ActivityMainBinding
import com.tandiera.project.noteme.views.home.HomeFragment
import com.tandiera.project.noteme.views.newtask.NewTaskActivity
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        binding.btnAddTask.setOnClickListener {
            startActivity<NewTaskActivity>()
        }
    }

    private fun setupBottomNavigation() {
        binding.btmNavMain.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.action_home -> {
                    openFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.action_taskComplete -> {
//                    openFragment(TaskCompleteFragment())
//                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun openFragment(fragment : Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameMain, fragment)
            .addToBackStack(null)
            .commit()
    }
}