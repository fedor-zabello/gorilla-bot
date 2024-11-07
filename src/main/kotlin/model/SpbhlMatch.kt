package model

import java.time.LocalDateTime

data class SpbhlMatch(
    val tournament: String,
    val date: LocalDateTime,
    val arena: String,
    val teams: String,
    val score: String?,
)