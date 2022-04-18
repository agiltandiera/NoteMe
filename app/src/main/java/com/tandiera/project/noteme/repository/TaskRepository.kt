package com.tandiera.project.noteme.repository

import android.content.Context
import com.google.gson.Gson
import com.tandiera.project.noteme.model.Tasks
import java.io.IOException

object TaskRepository {

    fun getDataTasks(context: Context) : Tasks? {
        val json : String?
        try {
            val inputStream = context?.assets?.open("tasks.json")
            json = inputStream?.bufferedReader().use {it?.readText()}
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return Gson().fromJson(json, Tasks::class.java)
    }
}