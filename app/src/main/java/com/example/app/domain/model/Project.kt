package com.example.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Project(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList(),
    var creationDate: String = ""
): Parcelable