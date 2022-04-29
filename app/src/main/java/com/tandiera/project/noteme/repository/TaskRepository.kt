package com.tandiera.project.noteme.repository

import android.content.Context
import com.google.gson.Gson
import com.tandiera.project.noteme.db.DbSubTaskHelper
import com.tandiera.project.noteme.db.DbTaskHelper
import com.tandiera.project.noteme.model.MainTask
import com.tandiera.project.noteme.model.Task
import com.tandiera.project.noteme.model.Tasks
import java.io.IOException

object TaskRepository {

    fun getDataTasks(context: Context?): Tasks?{
        val json: String?
        try {
            val inputStream = context?.assets?.open("tasks.json")
            json = inputStream?.bufferedReader().use { it?.readText() }
        }catch (e: IOException){
            e.printStackTrace()
            return null
        }

        return Gson().fromJson(json, Tasks::class.java)
    }

    fun getDataTaskFromDatabase(dbTaskHelper: DbTaskHelper, dbSubTaskHelper: DbSubTaskHelper)
            : List<Task>?{
        val tasks = mutableListOf<Task>()

        val mainTasks = dbTaskHelper.getAllTask()
        tasks.clear()

        if (mainTasks != null){
            for (mainTask: MainTask in mainTasks){
                val task = Task()
                task.mainTask = mainTask

                val subTasks = dbSubTaskHelper.getAllSubTask(mainTask.id)
                if (subTasks != null && subTasks.isNotEmpty()){
                    task.subTask = subTasks
                }

                tasks.add(task)
            }
        }else{
            return null
        }

        return tasks
    }

    fun getDataTaskCompleteFromDatabase(dbTaskHelper: DbTaskHelper, dbSubTaskHelper: DbSubTaskHelper)
            : List<Task>?{
        val tasks = mutableListOf<Task>()

        val mainTasks = dbTaskHelper.getAllTaskComplete()
        tasks.clear()

        if (mainTasks != null){
            for (mainTask: MainTask in mainTasks){
                val task = Task()
                task.mainTask = mainTask

                val subTasks = dbSubTaskHelper.getAllSubTask(mainTask.id)
                if (subTasks != null && subTasks.isNotEmpty()){
                    task.subTask = subTasks
                }

                tasks.add(task)
            }
        }else{
            return null
        }

        return tasks
    }
}