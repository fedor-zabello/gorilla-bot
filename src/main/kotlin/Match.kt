import java.time.LocalDateTime

data class Match (
    val tournament: String,
    val date: LocalDateTime,
    val arena: String,
    val teams: String,
    val score: String,
)