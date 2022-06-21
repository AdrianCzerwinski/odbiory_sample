package com.example.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Defect(
    var id: String = "",
    var type: String = "",
    var checkType: String? = CheckType.PARTIAL.type,
    var photos: Map<String, Boolean> = mapOf(Pair("empty", false)),
    var contractor: String = "",
    var status: Boolean = false,
    var comment: String = "",
    var project: String = "",
    var location: String = "",
    var date: String = "",
    var photoNames: List<String> = listOf("")
): Parcelable



