package com.tandiera.project.noteme.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tandiera.project.noteme.adapter.TaskAdapter
import com.tandiera.project.noteme.databinding.FragmentHomeBinding
import com.tandiera.project.noteme.db.DbSubTaskHelper
import com.tandiera.project.noteme.db.DbTaskHelper
import com.tandiera.project.noteme.model.Task
import com.tandiera.project.noteme.repository.TaskRepository
import com.tandiera.project.noteme.views.newtask.NewTaskActivity
import com.tandiera.project.noteme.views.newtask.NewTaskActivity.Companion.EXTRA_TASK

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbTaskHelper: DbTaskHelper
    private lateinit var dbSubTaskHelper: DbSubTaskHelper
    private lateinit var taskAdapter : TaskAdapter

    //private lateinit var taskAdapter: TaskAdapter

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
        onClick()
    }

    private fun onClick() {
        taskAdapter.onClick {
            context?.startActivity<NewTaskActivity>(EXTRA_TASK to it)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataTask()
    }

    private fun getDataTask() {
        val tasks = TaskRepository.getDataTaskFromDatabase(dbTaskHelper, dbSubTaskHelper)

        if(tasks != null && tasks.isNotEmpty()) {
            showTasks()
            taskAdapter.setData(tasks)

            binding.rvTask.adapter = taskAdapter
        }else{
            hideTasks()
        }
    }

    private fun setup() {
        dbTaskHelper = DbTaskHelper.getInstance(context)
        dbSubTaskHelper = DbSubTaskHelper.getInstance(context)
        taskAdapter = TaskAdapter()
    }

    private fun hideTasks() {
        binding.rvTask.visibility = View.GONE
        //binding.layoutEmptyTask.= View.VISIBLE
    }

    private fun showTasks() {
        binding.rvTask.visibility = View.VISIBLE
        //binding.layoutEmptyTask.visibility = View.GONE
    }
}