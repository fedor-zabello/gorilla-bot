import org.jsoup.Jsoup
import util.MatchMapper.dtoToMatch
import util.MessageGenerator.getMatchResultMessage
import util.MessageGenerator.getUpcomingMatchMessage
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.take

class MatchService {
    private val teamIds = listOf("b82f09ec-bf65-4d64-881f-0bef1598d936", "dc079cd6-d6e2-4b12-b7fa-b8676195a081")
    private val nearestMatchesCount = 3

    fun getNearestAsStrings(): List<String> {
        return getAllUpcoming()
            .sortedBy { it.date }
            .map { getUpcomingMatchMessage(it) }
            .take(nearestMatchesCount)
    }

    fun getAllUpcoming():List<SpbhlMatch> {
        return getAllAsDto().filter { it.score == "" }.map{ dtoToMatch(it) }
    }

    fun getAll(): List<SpbhlMatch> {
        return getAllAsDto().map{ dtoToMatch(it) }
    }

    fun getLastWithResult(): String {
        var matchDto =  getAllAsDto().filter { it.score != "" }.sortedBy { it.date }.last()
        return getMatchResultMessage(dtoToMatch(matchDto))
    }

    private fun getAllAsDto(): List<SpbhlMatchDto> {
        val matchDtoList = mutableListOf<SpbhlMatchDto>()

        teamIds.forEach {
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

        return matchDtoList.filter { it.teams != "" }
    }
}