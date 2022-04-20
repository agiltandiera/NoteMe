package com.tandiera.project.noteme.views.taskcomplete

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.adapter.TaskAdapter
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_complete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tasks : Tasks? = TaskRepository.getDataTasks(context)

        if(tasks != null) {
            for (task : Task in tasks.tasks!!) {
                task.mainTask?.isComplete = true

                if(task.subTask != null) {
                    for (subTask: subTask in task.subTask) {
                        subTask.isComplete = true
                    }
                }
            }
            
            showTaskComplete()
            val taskAdapter = TaskAdapter()
            taskAdapter.setData(tasks.tasks)

            rvTaskComplete.adapter = taskAdapter
        } else {
            hideTaskComplete()
        }
    }

    private fun hideTaskComplete() {
        rvTaskComplete.visibilty = View.GONE
        layoutEmptyTaskComplete.visibilty = View.VISIBLE
        fabDeleteTaskComplete.visibilty = View.GONE
    }

    private fun showTaskComplete() {
        rvTaskComplete.visibilty = View.VISIBLE
        layoutEmptyTaskComplete.visibilty = View.GONE
        fabDeleteTaskComplete.visibilty = View.VISIBLE
    }
}