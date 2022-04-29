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
import androidx.core.content.ContextCompat
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.adapter.AddSubTaskAdapter
import com.tandiera.project.noteme.databinding.ActivityNewTaskBinding
import com.tandiera.project.noteme.db.DbSubTaskHelper
import com.tandiera.project.noteme.db.DbTaskHelper
import com.tandiera.project.noteme.model.MainTask
import com.tandiera.project.noteme.model.Task
import com.tandiera.project.noteme.util.DateKerjaanku

class NewTaskActivity : AppCompatActivity() {

        companion object {
            const val EXTRA_TASK = "extra_task"
        }

        private lateinit var binding: ActivityNewTaskBinding

        private lateinit var addSubTaskAdapter: AddSubTaskAdapter
        private lateinit var dbTaskHelper: DbTaskHelper
        private lateinit var dbSubTaskHelper: DbSubTaskHelper
        private var isEdit = false
        private var delayedTime: Long = 1200
        private var task: Task? = null

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
        addSubTaskAdapter = AddSubTaskAdapter(dbSubTaskHelper)

        getDataExtra()
    }

    private fun getDataExtra() {
        if(intent != null){
            task = intent.getParcelableExtra(EXTRA_TASK)
        }
        if (task != null){
            isEdit = true
            binding.btnSubmitTask.text = getString(R.string.update)

            setupView(task)
        }else{
            task = Task(mainTask = MainTask())
        }
    }

    private fun setupView(task: Task?) {
        binding.etTitleTask.setText(task?.mainTask?.title)
        binding.etAddDetailsTask.setText(task?.mainTask?.details)
        val dateString = task?.mainTask?.date

        if (dateString != null){
            binding.btnAddDateTask.text = DateKerjaanku.dateFromSqlToDateViewTask(dateString)
            checkIsDateFilled(true)
        }
    }

    private fun setupAddSubTaskAdapter() {
        task?.subTask?.let { addSubTaskAdapter.setData(it) }
        binding.rvAddSubTask.adapter = addSubTaskAdapter
    }

    private fun onClick() {
        binding.tbNewTask.setNavigationOnClickListener {
            finish()
        }

        binding.btnAddDateTask.setOnClickListener {
            DateKerjaanku.showDatePicker(this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val dateString = DateKerjaanku.dateFormatSql(year, month, dayOfMonth)
                    binding.btnAddDateTask.text = DateKerjaanku.dateFromSqlToDateViewTask(dateString)

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
        val dataSubTasks = addSubTaskAdapter.getData()

        if (titleTask.isEmpty()){
            binding.etTitleTask.error = getString(R.string.required_field)
            binding.etTitleTask.requestFocus()
            return
        }

        task?.mainTask?.title = titleTask

        if (detailTask.isNotEmpty()){
            task?.mainTask?.details = detailTask
        }

        if (isEdit){
            val result = dbTaskHelper.updateTask(task?.mainTask)
            if (result > 0){
                if (dataSubTasks != null && dataSubTasks.isNotEmpty()){
                    var isSuccess = false
                    for (subTask: SubTask in dataSubTasks){
                        if (subTask.id != null){
                            val resultSubTask = dbSubTaskHelper.updateSubTask(subTask)
                            isSuccess = resultSubTask > 0
                        }else{
                            subTask.idTask = task?.mainTask?.id
                            val resultSubTask = dbSubTaskHelper.insert(subTask)
                            isSuccess = resultSubTask > 0
                        }
                    }
                    if (isSuccess){
                        val dialog = showSuccessDialog(getString(R.string.sucess_update_data_to_database))
                        Handler().postDelayed({
                            dialog.dismiss()
                        }, 1200)
                    }else{
                        val dialog = showFailedDialog(getString(R.string.failed_update_data_to_database))
                        Handler().postDelayed({
                            dialog.dismiss()
                        }, delayedTime)
                    }
                }
                val dialog = showSuccessDialog(getString(R.string.sucess_update_data_to_database))
                Handler().postDelayed({
                    dialog.dismiss()
                    finish()
                }, 1200)
            }else{
                val dialog = showFailedDialog(getString(R.string.failed_update_data_to_database))
                Handler().postDelayed({
                    dialog.dismiss()
                }, delayedTime)
            }
        }else {
            val result = dbTaskHelper.insert(task?.mainTask)
            if (result > 0){
                if (dataSubTasks != null && dataSubTasks.isNotEmpty()){
                    var isSuccess = false
                    for (subTask: SubTask in dataSubTasks){
                        subTask.idTask = result.toInt()
                        val resultSubTask = dbSubTaskHelper.insert(subTask)
                        isSuccess = resultSubTask > 0
                    }
                    if (isSuccess){
                        val dialog = showSuccessDialog(getString(R.string.sucess_add_data_to_database))
                        Handler().postDelayed({
                            dialog.dismiss()
                        }, 1200)
                    }else{
                        val dialog = showFailedDialog(getString(R.string.failed_add_data_to_database))
                        Handler().postDelayed({
                            dialog.dismiss()
                        }, delayedTime)
                    }
                }
                val dialog = showSuccessDialog(getString(R.string.sucess_add_data_to_database))
                Handler().postDelayed({
                    dialog.dismiss()
                    finish()
                }, 1200)
            }else{
                val dialog = showFailedDialog(getString(R.string.failed_add_data_to_database))
                Handler().postDelayed({
                    dialog.dismiss()
                }, delayedTime)
            }
        }
    }

    private fun showSuccessDialog(desc: String): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage(desc)
            .show()
    }

    private fun showFailedDialog(desc: String): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Failed")
            .setMessage(desc)
            .show()
    }

    private fun checkIsDateFilled(isDateFilled: Boolean) {
        if(isDateFilled){
            binding.btnAddDateTask.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_add_date_task)
            binding.btnAddDateTask.setPadding(24, 24, 24, 24)
            binding.btnRemoveDateTask.visibility = View.VISIBLE
        }else{
            binding.btnAddDateTask.setBackgroundResource(0)
            binding.btnAddDateTask.setPadding(0, 0, 0, 0)
            binding.btnRemoveDateTask.visibility = View.GONE
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.tbNewTask)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit){
            menuInflater.inflate(R.menu.new_task_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_remove_task -> {
                AlertDialog.Builder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure want to delete?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        task?.mainTask?.id?.let {
                            val result = dbTaskHelper.deleteTask(it)
                            if (result > 0){
                                val dialogSuccess = showSuccessDialog("Task successfully deleted")
                                Handler().postDelayed({
                                    dialogSuccess.dismiss()
                                    dialog.dismiss()
                                    finish()
                                }, delayedTime)
                            }
                        }
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}