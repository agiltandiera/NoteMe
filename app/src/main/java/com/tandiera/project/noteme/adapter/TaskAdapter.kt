package com.tandiera.project.noteme.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.databinding.ItemTaskBinding
import com.tandiera.project.noteme.db.DbSubTaskHelper
import com.tandiera.project.noteme.db.DbTaskHelper
import com.tandiera.project.noteme.model.SubTask
import com.tandiera.project.noteme.model.Task
import com.tandiera.project.noteme.model.Tasks
import java.util.logging.Handler

class TaskAdapter(
    val dbTaskHelper: DbTaskHelper,
    val dbSubTaskHelper: DbSubTaskHelper) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var tasks = mutableListOf<Task>()
    private var listener : ((Task) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
   }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position], listener, dbTaskHelper, dbSubTaskHelper)
    }

    fun setData(it: List<Tasks>) {
        this.tasks = tasks as MutableList<Task>
        notifyDataSetChanged()
    }

    fun deleteData(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, tasks.size)
    }

    fun onClick(listener: (Task) -> Unit) {
        this.listener = listener
    }

    inner class ViewHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            task: Task,
            listener: ((Task) -> Unit)? = null,
            dbTaskHelper: DbTaskHelper,
            dbSubTaskHelper: DbSubTaskHelper
        ) {
            binding.tvTitleTask.text = task.mainTask?.title
//            itemView.tvTitleTask.text = task.mainTask?.title
            val subTaskAdapter = SubTaskAdapter()

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
                subTaskAdapter.setData(task.subTask)

               binding.rvSubTask.adapter = subTaskAdapter
//                itemView.rvSubTask.adapter = subTaskAdapter
            } else {
                hideSubTask()
            }

            binding.btnDoneTask.setOnClickListener {
                if(task.mainTask?.isComplete!!) {
                    task.mainTask?.isComplete = false
                    val result = dbTaskHelper.updateTask(task.mainTask)
                    if(result > 0) {
                        isCompleteTask()
                        Handler().postDelayed({
                            deleteDataTask(adapterPosition)
                        }, 500)
                        if(task.subTask != null) {
                            var isSuccess = false
                            for(subTask: SubTask in task.subTask!!) {
                                subTask.isComplete = false
                                val resultSubTask = dbSubTaskHelper.updateSubTask(subTask)
                                if(resultSubTask > 0) {
                                    isSuccess = true
                                }
                            }
                            if(isSuccess) {
                                subTaskAdapter.setData(task.subTask!!)
                            }
                        }
                    }
                } else {
                    task.mainTask.isComplete = true
                    val result = dbTaskHelper.updateTask(task.mainTask)
                    if(result > 0) {
                        completeTask()
                        Handler().postDelayed({
                            deleteDataTask(adapterPosition)
                        }, 500)
                        if(task.subTask != null) {
                            var isSuccess = false
                            for(subTask: SubTask in task.subTask!!) {
                                subTask.isComplete = true
                                val resultSubTask = dbSubTaskHelper.updateSubTask(subTask)
                                if(resultSubTask > 0) {
                                    isSuccess = true
                                }
                            }
                            if(isSuccess) {
                                subTaskAdapter.setData(task.subTask!!)
                            }
                        }
                    }
                }
            }

            itemView.setOnClickListener{
                if(listener != null) {
                    listener(task)
                }
            }
            subTaskAdapter.onClick {
                if (listener != null) {
                    listener(task)
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