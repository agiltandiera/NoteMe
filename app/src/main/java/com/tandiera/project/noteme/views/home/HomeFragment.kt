package com.tandiera.project.noteme.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tandiera.project.noteme.adapter.TaskAdapter
import com.tandiera.project.noteme.databinding.FragmentHomeBinding
import com.tandiera.project.noteme.repository.TaskRepository

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

        val tasks = context?.let { TaskRepository.getDataTasks(it) }

        if(tasks != null) {
            showTasks()
            val taskAdapter = TaskAdapter()
            tasks.tasks?.let { taskAdapter.setData(it) }

            binding.rvTask.adapter = taskAdapter
        }else{
            hideTasks()
        }
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