package com.tandiera.project.noteme.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tandiera.project.noteme.db.DbContract.MySubTask.Companion.SUB_TASK_ID
import com.tandiera.project.noteme.db.DbContract.MySubTask.Companion.SUB_TASK_IS_COMPLETE
import com.tandiera.project.noteme.db.DbContract.MySubTask.Companion.SUB_TASK_TASK_ID
import com.tandiera.project.noteme.db.DbContract.MySubTask.Companion.SUB_TASK_TITLE
import com.tandiera.project.noteme.db.DbContract.MySubTask.Companion.TABLE_SUB_TASK
import com.tandiera.project.noteme.model.SubTask

class DbSubTaskHelper(context: Context?) {
    companion object {
        private const val TABLE_NAME =TABLE_SUB_TASK
        private lateinit var dbHelper: DbHelper
        private lateinit var database: SQLiteDatabase
        private var instance : DbSubTaskHelper? = null

        fun getInstance(context: Context?): DbSubTaskHelper {
            if(instance == null) {
                synchronized(SQLiteOpenHelper::class) {
                    if(instance == null) {
                        instance = DbSubTaskHelper(context)
                    }
                }
            }
            return  instance as DbSubTaskHelper
        }
    }

    init {
        dbHelper = DbHelper(context)
    }

    private fun open() {
        database = dbHelper.writableDatabase
    }

    private fun close() {
        dbHelper.close()
        if (database.isOpen) {
            database.close()
        }
    }

    fun insert(subTask: SubTask?) {
        open()
        val values = ContentValues()
        values.put(SUB_TASK_TITLE,subTask?.title)
        values.put(SUB_TASK_TASK_ID,subTask?.idTask)
        values.put(SUB_TASK_IS_COMPLETE,subTask?.title)

        val result = database.insert(TABLE_NAME, null, values)
        close()
        return result
    }

    fun getAllSubTask(idTask: Int?) {
        open()
        val subTasks = mutableListOf<SubTask>()
        val query = "SELECT * FROM $TABLE_NAME WHERE $SUB_TASK_TASK_ID = $idTask"
        val cursor = database.rawQuery(query, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(SUB_TASK_ID))
                val idTaskSubTask = cursor.getInt(cursor.getColumnIndexOrThrow(SUB_TASK_TASK_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(SUB_TASK_TITLE))
                val isComplete =
                    cursor.getString(cursor.getColumnIndexOrThrow(SUB_TASK_IS_COMPLETE))
                var isCompleteSubTask: Boolean
                isCompleteSubTask = isComplete == 1

                subTasks.add(SubTask(id, idTask, title, isCompleteSubTask))
            }
        } else {
            return null
        }
        cursor.close()
        close()
    }

    fun updateSubTask(subTask: SubTask?) : Int {
        val values = ContentValues()
        values.put(SUB_TASK_TITLE,subTask?.title)
        values.put(SUB_TASK_TASK_ID,subTask?.idTask)
        values.put(SUB_TASK_IS_COMPLETE,subTask?.title)

        val result = database.update(TABLE_NAME, null, values, "$SUB_TASK_ID = ${subTask?.id}", null)
        close()
        return result
    }

    fun deleteSubTask(id: Int): Int {
        open()
        val result = database.delete(TABLE_NAME, "$SUB_TASK_ID = $id")
        close()
        return result
    }
}