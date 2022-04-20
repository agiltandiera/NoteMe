package com.tandiera.project.noteme.views.newtask

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.util.DateKerjakaanku
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

        btnAddDateTask.setOnClickListener {
            DateKerjakaanku.showDatePicker(this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val dateString = DateKerjakaanku.dateFromatSql(year, month, dayOfMonth)
                    btnAddDateTask.text = DateKerjakaanku.dateFromSqlToDateViewTask(dateString)

                    checkIsDateFilled(true)
                })
        }

        btnRemoveDateTask.setOnClickListener {
            btnAddDateTask.text = null
            checkIsDateFilled(false)
        }
    }

    private fun checkIsDateFilled(isDateFilled: Boolean) {
        if(isDateFilled) {
            btnAddDateTask.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_add_date_task)
            btnAddDateTask.setPadding(24, 24, 24)
            btnRemoveDateTask.visibility = View.VISIBLE
        } else {
            btnAddDateTask.setBackgroundResource(0)
            btnAddDateTask.setPadding(0, 0, 0)
            btnRemoveDateTask.visibility = View.GONE
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