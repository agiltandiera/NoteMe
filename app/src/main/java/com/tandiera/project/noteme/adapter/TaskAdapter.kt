package com.tandiera.project.noteme.adapter

import android.graphics.Paint
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.databinding.ItemTaskBinding
import com.tandiera.project.noteme.model.Task
import com.tandiera.project.noteme.model.Tasks

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var tasks = mutableListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
   }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    fun setData(it: List<Tasks>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.tvTitleTask.text = task.mainTask?.title
//            itemView.tvTitleTask.text = task.mainTask?.title

            if(task.mainTask?.isComplete!!) {
                completeTask()
            } else {
                isCompleteTask()
            }

            if(task.mainTask?.date != null && task.mainTask.date.isNotEmpty()) {
                showDateTask()
//                itemView.tvDateTask.text = task.mainTask.date
            } else {
                hideDateTask()
            }

            if(task.subTask != null) {
                showSubTasks()
                val subTaskAdapter = SubTaskAdapter()
                subTaskAdapter.setData(task.subTask)

               binding.rvSubTask.adapter = subTaskAdapter
//                itemView.rvSubTask.adapter = subTaskAdapter
            } else {
                hideSubTask()
            }

            binding.btnDoneTask.setOnClickListener {
                if(task.mainTask?.isComplete!!) {
                    isCompleteTask()
                    task.mainTask?.isComplete = false
                } else {
                    completeTask()
                    task.mainTask.isComplete = true
                }
            }

        }

        private fun completeTask() {
            binding.btnDoneTask.setImageResource(R.drawable.ic_complete_task)
            binding.tvTitleTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        private fun isCompleteTask() {
            binding.btnDoneTask.setImageResource(R.drawable.ic_done_task)
            binding.tvTitleTask.paintFlags = Paint.ANTI_ALIAS_FLAG
        }

        private fun hideSubTask() {
            binding.lineTask.visibility = View.GONE
            binding.rvSubTask.visibility = View.GONE
        }

        private fun showSubTasks() {
            binding.lineTask.visibility = View.VISIBLE
            binding.rvSubTask.visibility = View.VISIBLE
        }

        private fun hideDateTask() {
            binding.containerDateTask.visibility = View.GONE
        }

        private fun showDateTask() {
            binding.containerDateTask.visibility = View.VISIBLE
        }

    }

}