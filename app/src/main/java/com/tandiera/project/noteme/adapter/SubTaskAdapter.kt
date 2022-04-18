package com.tandiera.project.noteme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.model.SubTask
import com.tandiera.project.noteme.model.Task

class SubTaskAdapter : RecyclerView.Adapter<SubTaskAdapter.ViewHolder> () {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(subTask: SubTask) {
            itemView.tvTitleSubTask.text = subTask.title

            itemView.btnDoneSubTask.setOnClickListener {
                if(subTask.isComplete) {
                    isCompleteSubTask()
                    subTask.isComplete = false
                } else {
                    completeSubTask()
                    subTask.isComplete = true
                }
            }
        }

        private fun completeSubTask() {
            itemView.btnDoneSubTaks.setImageResource(R.drawable.ic_complete_task)
        }

        private fun isCompleteSubTask() {
            itemView.btnDoneSubTaks.setImageResource(R.drawable.ic_done_task)
        }

    }

    private lateinit var subTasks :  List<SubTask>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) {
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sub_task, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subTasks[position])
    }

    override fun getItemCount(): Int {
        subTasks.size
    }

    fun setData(subTasks: List<SubTask>) {
        this.subTasks = subTasks
        notifyDataSetChanged()
    }
}