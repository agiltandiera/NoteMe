package com.tandiera.project.noteme.views.taskcomplete

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tandiera.project.noteme.adapter.TaskAdapter
import com.tandiera.project.noteme.databinding.FragmentTaskCompleteBinding
import com.tandiera.project.noteme.db.DbSubTaskHelper
import com.tandiera.project.noteme.db.DbTaskHelper
import com.tandiera.project.noteme.model.SubTask
import com.tandiera.project.noteme.model.Task
import com.tandiera.project.noteme.repository.TaskRepository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskCompleteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskCompleteFragment : Fragment() {

    private var _binding: FragmentTaskCompleteBinding? = null
    private val binding get() = _binding!!

    private var tasks: List<Task>? = null

    private lateinit var dbTaskHelper: DbTaskHelper
    private lateinit var dbSubTaskHelper: DbSubTaskHelper
    private lateinit var taskAdapter : TaskAdapter

    private val tasks : List<Task>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskCompleteBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
        onClick()
    }

    private fun onClick() {
        binding.fabDeleteTaskComplete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete All Data")
                .setMessage("Apakah anda yakin menghapus semua data?")
                .setPositiveButton("Yes") { dialog, _ ->
                    if(tasks != null) {
                        val result = dbTaskHelper.deleteAllTaskComplete()
                        if(result > 0) {
                            val dialogDeleteSuccess = showSuccessDeleteAllTaskDialog()
                            Handler().postDelayed({
                                dialog.dismiss()
                                dialogDeleteSuccess.dismiss()
                            }, 1200)
                        }
                    }
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showSuccessDeleteAllTaskDialog(): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle("Success")
            .setMessage("Berhasil menghapus semua data")
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

        if(tasks != null && tasks.isNotEmpty()) {
            showTaskComplete()
            taskAdapter.setData(tasks)

            binding.rvTask.adapter = taskAdapter
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