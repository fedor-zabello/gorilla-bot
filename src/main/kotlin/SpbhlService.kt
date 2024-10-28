import dto.SpbhlMatchDto
import org.jsoup.Jsoup
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.take
import kotlin.text.trimIndent

class SpbhlService {
    val gorillaTeamId = "b82f09ec-bf65-4d64-881f-0bef1598d936"
    val upcomingGamesCount = 3

    fun getUpcomingGames(): List<String> {
        val url = "https://spbhl.ru/Schedule?TeamID=$gorillaTeamId"
        val document = Jsoup.connect(url).get()
        val matchTable = document.select("#MatchGridView")
        val matchRows = matchTable.select("tr")

        return matchRows.map {
            val tournament = it.select("td:nth-child(1)").text()
            val date = it.select("td:nth-child(4)").text()
            val time = it.select("td:nth-child(5)").text()
            val stadium = it.select("td:nth-child(6)").text()
            val teams = it.select("td:nth-child(7)").text()
            val score = it.select("td:nth-child(8)").text()
            SpbhlMatchDto(tournament, date, time, stadium, teams, score)
        }
            .filter { it.score == "" && it.teams != "" }
            .map { getMatchMessage(it) }
            .take(upcomingGamesCount)
    }

    private fun getMatchMessage(spbhlMatchDto: SpbhlMatchDto): String {
        return """
        ${spbhlMatchDto.teams}
        дата: ${spbhlMatchDto.date}
        время: ${spbhlMatchDto.time}
        арена: ${spbhlMatchDto.arena}
    """.trimIndent()
    }
}