package com.tandiera.project.noteme.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.databinding.ItemSubTaskBinding
import com.tandiera.project.noteme.model.SubTask

class SubTaskAdapter : RecyclerView.Adapter<SubTaskAdapter.ViewHolder> () {

    private lateinit var subTasks :  List<SubTask>
    private lateinit var listener: (View) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = ItemSubTaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subTasks[position], listener)
    }

    override fun getItemCount(): Int = subTasks.size

    fun setData(subTasks: List<SubTask>) {
        this.subTasks = subTasks
        notifyDataSetChanged()
    }

    fun onClick(listener: (View) -> Unit) {
        this.listener = listener
    }

    class ViewHolder(val binding: ItemSubTaskBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(subTask: SubTask, listener: (View) -> Unit) {
            binding.tvTitleSubTask.text = subTask.title

            if(subTask.isComplete) {
                completeSubTask()
            } else {
                isCompleteSubTask()
            }

            binding.btnDoneSubTask.setOnClickListener {
                if(subTask.isComplete) {
                    isCompleteSubTask()
                    subTask.isComplete = false
                } else {
                    completeSubTask()
                    subTask.isComplete = true
                }
            }

            itemView.setOnClickListener {
                listener(it)
            }

        }

        private fun completeSubTask() {
            binding.btnDoneSubTask.setImageResource(R.drawable.ic_complete_task)
            binding.tvTitleSubTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        private fun isCompleteSubTask() {
            binding.btnDoneSubTask.setImageResource(R.drawable.ic_done_task)
            binding.tvTitleSubTask.paintFlags = Paint.ANTI_ALIAS_FLAG
        }

    }

}