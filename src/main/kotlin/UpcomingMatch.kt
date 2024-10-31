import java.time.LocalDateTime

data class UpcomingMatch (
    val tournament: String,
    val date: LocalDateTime,
    val arena: String,
    val teams: String,
)