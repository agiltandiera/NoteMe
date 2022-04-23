package com.tandiera.project.noteme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    var mainTask : MainTask? = null,
    var subTask : List<SubTask>? = null,
) : Parcelable
