package com.tandiera.project.noteme.views.newtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tandiera.project.noteme.R
import org.jetbrains.anko.toast

class NewTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        setupActionBar()
        onClick()
    }

    private fun onClick() {
        tbNewTask.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(tbNewTask)
        supportActionBar?.title = ""
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_task_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_remove_task -> {
                toast("Remove Task")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}