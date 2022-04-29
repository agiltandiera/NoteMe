package com.tandiera.project.noteme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubTask(
    val id : Int? = null,
    var idTask : Int? = null,
    var title : String? = "",
    var isComplete : Boolean = false
) : Parcelable
