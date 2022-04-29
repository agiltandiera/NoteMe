package com.tandiera.project.noteme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainTask(
    val id: Int? = null,
    var title : String? = "",
    var details : String? = "",
    var date : String? = "",
    var isComplete : Boolean? = false
) : Parcelable
