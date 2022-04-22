package com.tandiera.project.noteme.views.newtask

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.tandiera.project.noteme.model.SubTask
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.adapter.AddSubTaskAdapter
import com.tandiera.project.noteme.databinding.ActivityNewTaskBinding
import com.tandiera.project.noteme.util.DateKerjakaanku
import org.jetbrains.anko.toast

class NewTaskActivity : AppCompatActivity() {

    private lateinit var addSubTaskAdapter: AddSubTaskAdapter

    private lateinit var binding : ActivityNewTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupAddSubTaskAdapter()
        onClick()
    }

    private fun setupAddSubTaskAdapter() {
        addSubTaskAdapter = AddSubTaskAdapter()
        binding.rvAddSubTask.adapter = addSubTaskAdapter
    }

    private fun onClick() {
        binding.tbNewTask.setNavigationOnClickListener {
            finish()
        }

        binding.btnAddDateTask.setOnClickListener {
            DateKerjakaanku.showDatePicker(this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val dateString = DateKerjakaanku.dateFromatSql(year, month, dayOfMonth)
                    binding.btnAddDateTask.text = DateKerjakaanku.dateFromSqlToDateViewTask(dateString)

                    checkIsDateFilled(true)
                })
        }

        binding.btnRemoveDateTask.setOnClickListener {
            binding.btnAddDateTask.text = null
            checkIsDateFilled(false)
        }

        binding.btnAddSubTask.setOnClickListener {
            val subTask = SubTask(null, null, "")
            addSubTaskAdapter.addTask(subTask)
        }
    }

    private fun checkIsDateFilled(isDateFilled: Boolean) {
        if(isDateFilled) {
            binding.btnAddDateTask.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_add_date_task)
            binding.btnAddDateTask.setPadding(24, 24, 24, 24)
            binding.btnRemoveDateTask.visibility = View.VISIBLE
        } else {
            binding.btnAddDateTask.setBackgroundResource(0)
            binding.btnAddDateTask.setPadding(0, 0, 0, 0)
            binding.btnRemoveDateTask.visibility = View.GONE
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(binding.tbNewTask)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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