package utils

import com.beust.klaxon.Json

data class UserInfos(
    @Json(ignored = true)
    var currentQuestion: Int,
    var level: Int,
    var points: Int,
    var pseudo: String
)