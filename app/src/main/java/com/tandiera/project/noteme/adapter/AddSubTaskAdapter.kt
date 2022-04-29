package com.tandiera.project.noteme.adapter

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tandiera.project.noteme.R
import com.tandiera.project.noteme.databinding.ItemAddSubTaskBinding
import com.tandiera.project.noteme.db.DbSubTaskHelper
import com.tandiera.project.noteme.model.SubTask

class AddSubTaskAdapter(private val dbSubTaskHelper: DbSubTaskHelper) : RecyclerView.Adapter<AddSubTaskAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAddSubTaskBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(subTask: SubTask) {
            if(subTask.title != null) {
                binding.etTitleSubTask.setText(subTask.title)
            }

            if(subTask.isComplete) {
                completeTask()
            } else {
                inCompleteTask()
            }

            binding.btnRemoveSubTask.setOnClickListener {
                if(subTask.id != null) {
                    val result = dbSubTaskHelper.deleteSubTask(subTask.id)
                    if(result > 0) {
                        deleteTask(adapterPosition)
                    }
                }
            }

            binding.etTitleSubTask.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    subTask.title = s.toString()
                    update(subTask, adapterPosition)
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            binding.btnCompleteSubTask.setOnClickListener{
                if(subTask.isComplete) {
                    subTask.isComplete = false
                    val result = dbSubTaskHelper.updateSubTask(subTask)
                    if(result > 0) {
                        inCompleteTask()
                    }
                } else {
                    subTask.isComplete = true
                    val result = dbSubTaskHelper.updateSubTask(subTask)
                    if(result>0) {
                        completeTask()
                    }
                }
            }
        }

        private fun inCompleteTask() {
            binding.btnCompleteSubTask.setImageResource(R.drawable.ic_done_task)
            binding.etTitleSubTask.paintFlags = Paint.ANTI_ALIAS_FLAG
        }

        private fun completeTask() {
            binding.btnCompleteSubTask.setImageResource(R.drawable.ic_complete_task)
            binding.etTitleSubTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

    }

    private var listAddSubTask = mutableListOf<SubTask>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddSubTaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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

    fun getData(): List<SubTask>? {
        return if (listAddSubTask.size > 0){
            listAddSubTask
        }else{
            null
        }
    }

    fun setData(subTask: List<SubTask>) {
        listAddSubTask = subTask as MutableList<SubTask>
    }
}