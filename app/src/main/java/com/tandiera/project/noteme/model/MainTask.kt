package com.tandiera.project.noteme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainTask(
    val id: Int? = null,
    val title : String? = null,
    val details : String? = null,
    val date : String? = null,
    val isComplete : Boolean? = null
) : Parcelable
