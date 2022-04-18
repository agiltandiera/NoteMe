package com.tandiera.project.noteme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.model.Task

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(task: Task) {
            itemView.tvTitleTask.text = task.mainTask?.title

            if(task.mainTask.date != null && task.mainTask.date.isNotEmpty()) {
                showDateTask()
                itemView.tvDateTask.text = task.mainTask.date
            } else {
                hideDateTask()
            }

            if(task.subTask != null) {
                showSubTasks()
            } else {
                hideSubTask()
            }

            itemView.btnDoneTask.setOnClickListener {
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
            itemView.btnDoneTask.setImageResource(R.drawable.ic_complete_task)
        }

        private fun isCompleteTask() {
            itemView.btnDoneTask.setImageResource(R.drawable.ic_done_task)
        }

        private fun hideSubTask() {
            itemView.lineTask.visibility = View.GONE
            itemView.rvSubTask.visibility = View.GONE
        }

        private fun showSubTasks() {
            itemView.lineTask.visibility = View.VISIBLE
            itemView.rvSubTask.visibility = View.VISIBLE
        }

        private fun hideDateTask() {
            itemView.containerDateTask.visibility = View.GONE
        }

        private fun showDateTask() {
            itemView.containerDateTask.visibility = View.VISIBLE
        }

    }

    private lateinit var tasks : List<Task>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun setData(tasks: List<Task>) {
        this.tasks = tasks
    }
}