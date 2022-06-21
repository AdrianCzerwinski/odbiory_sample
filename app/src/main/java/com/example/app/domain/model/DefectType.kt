package com.example.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DefectType(
    val id: String = "",
    val name: String = ""
) : Parcelable