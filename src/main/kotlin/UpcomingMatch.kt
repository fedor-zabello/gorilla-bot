import java.time.ZonedDateTime

data class UpcomingMatch (
    val tournament: String,
    val date: ZonedDateTime,
    val arena: String,
    val teams: String,
)