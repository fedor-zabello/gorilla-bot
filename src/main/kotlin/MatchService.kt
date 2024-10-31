import dto.SpbhlMatchDto
import org.jsoup.Jsoup
import util.MatchMapper.dtoToUpcomingMatch
import java.time.format.DateTimeFormatter
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.take
import kotlin.text.trimIndent

object MatchService {
    private val teamIds = listOf("b82f09ec-bf65-4d64-881f-0bef1598d936", "dc079cd6-d6e2-4b12-b7fa-b8676195a081")
    private val nearestMatchesCount = 3

    fun getNearestAsStrings(): List<String> {
        return getAllUpcomingAsDto()
            .map {dtoToUpcomingMatch(it)}
            .sortedBy { it.date }
            .map { getMatchMessage(it) }
            .take(nearestMatchesCount)
    }

    fun getAllUpcoming():List<UpcomingMatch> {
        return getAllUpcomingAsDto().map{ dtoToUpcomingMatch(it) }
    }

    private fun getAllUpcomingAsDto(): List<SpbhlMatchDto> {
        val matchDtoList = mutableListOf<SpbhlMatchDto>()

        teamIds.forEach() {
            val url = "https://spbhl.ru/Schedule?TeamID=$it"
            val document = Jsoup.connect(url).get()
            val matchTable = document.select("#MatchGridView")
            val matchRows = matchTable.select("tr")

            matchDtoList.addAll(matchRows.map {
                val tournament = it.select("td:nth-child(1)").text()
                val date = it.select("td:nth-child(4)").text()
                val time = it.select("td:nth-child(5)").text()
                val arena = it.select("td:nth-child(6)").text()
                val teams = it.select("td:nth-child(7)").text()
                val score = it.select("td:nth-child(8)").text()
                SpbhlMatchDto(tournament, date, time, arena, teams, score)
            })
        }

        return matchDtoList.filter { it.score == "" && it.teams != "" }
    }

    private fun getMatchMessage(match: UpcomingMatch): String {

        return """
        ${match.teams}
        дата: ${match.date.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}
        время: ${match.date.toLocalTime()}
        арена: ${match.arena}
    """.trimIndent()
    }
}