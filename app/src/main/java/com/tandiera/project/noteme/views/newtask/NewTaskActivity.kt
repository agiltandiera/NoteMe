package com.tandiera.project.noteme.views.newtask

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
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

    companion object {
        const val EXTRA_TASK = "extra_task"
    }

    private lateinit var addSubTaskAdapter: AddSubTaskAdapter
    private lateinit var dbTaskHelper: DbTaskHelper
    private lateinit var dbSubTaskHelper: DbTaskHelper
    private var isEdit = false
    private var delayedTime: Long = 1200
    private var task: Task? = null

    private lateinit var binding: ActivityNewTaskBinding

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
        if(intent != null) {
            task = intent.getParcelableExtra(EXTRA_TASK)
        }
        if (task != null) {
            isEdit = true
            binding.btnSubmitTask.text = "Update"

            setupView(task)
        } else {
            task = Task(mainTask = MainTask)
        }
    }

    private fun setupView(task: Task?) {
        binding.etTitleTask.setText(task?.mainTask?.title)
        binding.etAddDetailsTask.setText(task?.mainTask?.details)
        val dateString = task?.mainTask?.date
        binding.btnAddDateTask.text = DateKerjakaanku.dateFromSqlToDateViewTask(dateString.toString)

        if(task?.mainTask?.date!!.isNotEmpty()) {
            checkIsDateFilled(true)
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
                    binding.btnAddDateTask.text =
                        DateKerjakaanku.dateFromSqlToDateViewTask(dateString)

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

        if (titleTask.isEmpty()) {
            binding.etTitleTask.error = "Please field you title"
            binding.etTitleTask.requestFocus()
            return
        }

        task?.mainTask?.title = titleTask
        if (detailTask.isNotEmpty()) {
            task?.mainTask?.details = detailTask
        }

        if (isEdit) {
            val result = dbTaskHelper.updateTask(task?.mainTask)
            if (result > 0) {
                if (dataSubTask != null && dataSubTask.isNotEmpty()) {
                    var isSuccess = false
                    for (subTask: SubTask in dataSubTask) {
                        if(subTask.id != null) {
                            val resultSubTask = dbSubTaskHelper.updateSubTask(subTask)
                            isSuccess = resultSubTask > 0
                        } else {
                            subTask.idTask = task?.mainTask?.id
                            val resultSubTask = dbSubTaskHelper.insert(subTask)
                            isSuccess = resultSubTask > 0
                        }
                    }
                    if (isSuccess) {
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
        } else {
            val result = dbTaskHelper.insert(task?.mainTask)
            if (result > 0) {
                if (dataSubTask != null && dataSubTask.isNotEmpty()) {
                    var isSuccess = false
                    for (subTask: SubTask in dataSubTask) {
                        subTask.idTask = result.toInt()
                        val resultSubTask = dbSubTaskHelper.insert(subTask)
                        isSuccess = resultSubTask > 0
                    }
                    if (isSuccess) {
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
            if (isDateFilled) {
                binding.btnAddDateTask.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_btn_add_date_task)
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
            if (isEdit) {
                menuInflater.inflate(R.menu.new_task_menu, menu)
            }
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_remove_task -> {
                    AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Apakah Anda yakin akan delete data ini?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            task?.mainTask?.id?.let {
                                val result = dbTaskHelper.deleteTask(it)
                                if(result>0) {
                                    val dialogSuccess = showSuccessDialog("Data ini berhasil dihapus")
                                    Handler().postDelayed({
                                        dialogSuccess.dismiss()
                                        dialog.dismiss()
                                        finish()
                                    }, delayedTime)
                                }
                            }
                        }
                        .setNegativeButton("Tidak"), {dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }

            return super.onOptionsItemSelected(item)
        }
    }
}