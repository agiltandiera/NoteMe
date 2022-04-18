package com.tandiera.project.noteme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    val mainTask : MainTask? = null,
     val subTask : List<SubTask>? = null,
) : Parcelable
