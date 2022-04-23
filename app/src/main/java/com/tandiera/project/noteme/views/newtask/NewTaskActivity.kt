package com.tandiera.project.noteme.views.newtask

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.tandiera.project.noteme.model.SubTask
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.adapter.AddSubTaskAdapter
import com.tandiera.project.noteme.databinding.ActivityNewTaskBinding
import com.tandiera.project.noteme.db.DbSubTaskHelper
import com.tandiera.project.noteme.db.DbTaskHelper
import com.tandiera.project.noteme.model.MainTask
import com.tandiera.project.noteme.model.Task
import com.tandiera.project.noteme.util.DateKerjakaanku
import org.jetbrains.anko.toast

class NewTaskActivity : AppCompatActivity() {

    private lateinit var addSubTaskAdapter: AddSubTaskAdapter
    private lateinit var dbTaskHelper : DbTaskHelper
    private lateinit var dbSubTaskHelper: DbTaskHelper
    private var isEdit = false
    private var delayedTime : Long = 1200
    private var task : Task? = null

    private lateinit var binding : ActivityNewTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
        setupActionBar()
        setupAddSubTaskAdapter()
        onClick()
    }

    private fun setup() {
        dbTaskHelper = DbTaskHelper.getInstance(this)
        dbSubTaskHelper = DbSubTaskHelper.getInstance(this)

        getDataExtra()
    }

    private fun getDataExtra() {
        if(task != null) {

        } else {
            task = Task(mainTask = MainTask)
        }
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

                    task?.mainTask?.date = dateString
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

        binding.btnSubmitTask.setOnClickListener {
            submitDataToDatabase()
        }
    }

    private fun submitDataToDatabase() {
        val titleTask = binding.etTitleTask.text.toString()
        val detailTask = binding.etAddDetailsTask.text.toString()
        val dataSubTask = addSubTaskAdapter.getData()

        if(titleTask.isEmpty()) {
            binding.etTitleTask.error = "Please field you title"
            binding.etTitleTask.requestFocus()
            return
        }

        task?.mainTask?.title = titleTask
        if(detailTask.isNotEmpty()) {
            task?.mainTask?.details = detailTask
        }

        if(isEdit) {
            // edit
        } else {
            val result = dbTaskHelper.insert(task?.mainTask)
            if(result>0) {
                if(dataSubTask  != null && dataSubTask.isNotEmpty()) {
                    var isSuccess = false
                    for(subTask : SubTask in dataSubTask) {
                        subTask.idTask = result.toInt()
                        val resultSubTask = dbSubTaskHelper.insert(subTask)
                        isSuccess = resultSubTask>0
                    }
                    if(isSuccess) {
                        val dialog = showSuccessDialog("success add database")
                        Handler().postDelayed({
                            dialog.dismiss
                        }, 1200)
                    } else {
                        val dialog = showFailedDialog("Failed to addd database")
                        Handler().postDelayed({
                            dialog.dismiss()
                        }, delayedTime)
                }

            } else {
                val dialog = showFailedDialog("Failed to addd database")
                Handler().postDelayed({
                    dialog.dismiss()
                    finish()
                }, delayedTime)
            }
        }
    }

    private fun showSuccessDialog(s: String): Any {
        return AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage(desc)
            .show()
    }

    private fun showFailedDialog(s: String): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Failed")
            .setMessage(desc)
            .show()
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