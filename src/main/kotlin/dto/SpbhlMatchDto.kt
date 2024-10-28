package dto

data class SpbhlMatchDto(
    val tournament: String,
    val date: String,
    val time: String,
    val arena: String,
    val teams: String,
    val score: String,
)
