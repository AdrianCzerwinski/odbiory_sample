package com.example.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    var id: String = "",
    var name: String = ""
): Parcelable
