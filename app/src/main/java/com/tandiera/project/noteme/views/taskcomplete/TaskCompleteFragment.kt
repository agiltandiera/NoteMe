package com.tandiera.project.noteme.views.taskcomplete

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tandiera.project.noteme.adapter.TaskAdapter
import com.tandiera.project.noteme.databinding.FragmentTaskCompleteBinding
import com.tandiera.project.noteme.db.DbSubTaskHelper
import com.tandiera.project.noteme.db.DbTaskHelper
import com.tandiera.project.noteme.model.Task
import com.tandiera.project.noteme.repository.TaskRepository

class TaskCompleteFragment : Fragment() {

    private var _binding: FragmentTaskCompleteBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbTaskHelper: DbTaskHelper
    private lateinit var dbSubTaskHelper: DbSubTaskHelper
    private lateinit var taskAdapter: TaskAdapter
    private var tasks: List<Task>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
        onClick()
    }

    private fun onClick() {
        binding.fabDeleteTaskComplete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete All Tasks")
                .setMessage("Are you sure want to delete?")
                .setPositiveButton("Yes"){ dialog, _ ->
                    if (tasks != null){
                        val result = dbTaskHelper.deleteAllTaskComplete()
                        if (result > 0){
                            val dialogDeleteSuccess = showSuccessDeleteAllTaskDialog()
                            Handler().postDelayed({
                                dialog.dismiss()
                                dialogDeleteSuccess.dismiss()
                                taskAdapter.deleteAllDataTask()
                            }, 1200)
                        }
                    }
                }
                .setNegativeButton("No"){ dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showSuccessDeleteAllTaskDialog(): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle("Success")
            .setMessage("All task successfully deleted")
            .show()
    }

    private fun setup() {
        dbTaskHelper = DbTaskHelper.getInstance(context)
        dbSubTaskHelper = DbSubTaskHelper.getInstance(context)
        taskAdapter = TaskAdapter(dbTaskHelper, dbSubTaskHelper)
    }

    override fun onResume() {
        super.onResume()
        getDataTask()
    }

    private fun getDataTask() {
        tasks = TaskRepository.getDataTaskCompleteFromDatabase(dbTaskHelper, dbSubTaskHelper)

        if (tasks != null && tasks!!.isNotEmpty()){
            showTaskComplete()
            taskAdapter.setData(tasks!!)

            binding.rvTaskComplete.adapter = taskAdapter
        }else{
            hideTaskComplete()
        }
    }

    private fun hideTaskComplete() {
        binding.rvTaskComplete.visibility = View.GONE
        binding.layoutEmptyTaskComplete.visibility = View.VISIBLE
        binding.fabDeleteTaskComplete.visibility = View.GONE
    }

    private fun showTaskComplete() {
        binding.rvTaskComplete.visibility = View.VISIBLE
        binding.layoutEmptyTaskComplete.visibility = View.GONE
        binding.fabDeleteTaskComplete.visibility = View.VISIBLE
    }
}
