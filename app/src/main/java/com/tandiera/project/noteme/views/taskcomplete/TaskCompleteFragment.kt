package com.tandiera.project.noteme.views.taskcomplete

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.adapter.TaskAdapter
import com.tandiera.project.noteme.databinding.FragmentHomeBinding
import com.tandiera.project.noteme.databinding.FragmentTaskCompleteBinding
import com.tandiera.project.noteme.model.SubTask
import com.tandiera.project.noteme.model.Task
import com.tandiera.project.noteme.model.Tasks
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

        val tasks = context?.let { TaskRepository.getDataTasks(it) }

        if(tasks != null) {
            for (task : Task in tasks.tasks!!) {
                task.mainTask?.isComplete = true

                if(task.subTask != null) {
                    for (subTask: SubTask in task.subTask) {
                        subTask.isComplete = true
                    }
                }
            }
            
            showTaskComplete()
            val taskAdapter = TaskAdapter()
            taskAdapter.setData(tasks.tasks)

            binding.rvTaskComplete.adapter = taskAdapter
        } else {
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