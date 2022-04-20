package com.tandiera.project.noteme.adapter

import android.graphics.Paint
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.model.SubTask

class AddSubTaskAdapter : RecyclerView.Adapter<AddSubTaskAdapter.ViewHolder>() {
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(subTask: SubTask) {
            if(subTask.title != null) {
                itemView.etTitleSubTask.setText(subTask.title)
            }

            if(subTask.isComplete) {
                completeTask()
            } else {
                inCompleteTask()
            }

            itemView.btnRemoveSubTask.setOnClickListener {
                deleteTask(adapterPosition)
            }

            itemView.etTitleSubask.addTextChangedListener(object: TextWatcher)
            // implement
        }

        private fun inCompleteTask() {
            itemView.btnCompleteSubTask.setImageResource(R.drawable.ic_done_task)
            itemView.etTitleSubTask.paintFlags = Paint.ANTI_ALIAS_FLAG
        }

        private fun completeTask() {
            itemView.btnCompleteSubTask.setImageResource(R.drawable.ic_complete_task)
            itemView.etTitleSubTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

    }

    private var listAddSubTask = mutableListOf<SubTask>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add_sub_task, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listAddSubTask[position])
    }

    override fun getItemCount(): Int = listAddSubTask.size

    fun addTask(subTask: SubTask) {
        listAddSubTask.add(subTask)
        notifyItemInserted(listAddSubTask.size-1)
    }

    fun deleteTask(position: Int) {
        listAddSubTask.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listAddSubTask.size)
    }

    fun update(subTask: SubTask, position: Int) {
        listAddSubTask[position] = subTask
    }
}