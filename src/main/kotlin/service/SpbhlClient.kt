package service

import model.SpbhlMatchDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class SpbhlClient(
        private val spbhlUrl: String = "https://spbhl.ru",
        private val teamIds: List<String> =
                listOf(
                        "b82f09ec-bf65-4d64-881f-0bef1598d936",
                        "dc079cd6-d6e2-4b12-b7fa-b8676195a081"
                ),
) {

    fun getAllMatches(): List<SpbhlMatchDto> {
        val matchDtoList = mutableListOf<SpbhlMatchDto>()

        try {
            teamIds.forEach { teamId ->
                val url = "$spbhlUrl/Schedule?TeamID=$teamId"
                val document = allMatchesFromSpbhl(url)
                val matchTable = document.select("#MatchGridView")
                val matchRows = matchTable.select("tr")

                matchDtoList.addAll(
                        matchRows.map {
                            val tournament = it.select("td:nth-child(1)").text()
                            val date = it.select("td:nth-child(4)").text()
                            val time = it.select("td:nth-child(5)").text()
                            val arena = it.select("td:nth-child(6)").text()
                            val teams = it.select("td:nth-child(7)").text()
                            val score = it.select("td:nth-child(8)").text()
                            SpbhlMatchDto(tournament, date, time, arena, teams, score)
                        }
                )
            }
        } catch (e: Exception) {
            println("ERROR: error while getting matches from spbhl. ${e.message}")
        }

        return matchDtoList.filter { matchDto ->
            matchDto.teams.isNotEmpty() && matchDto.date.isNotEmpty() && matchDto.time.isNotEmpty()
        }
    }

    private fun allMatchesFromSpbhl(url: String): Document {
        return Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0 Safari/537.36")
            .referrer("https://google.com")
            .timeout(10000)
            .followRedirects(true)
            .ignoreContentType(true)
            .get()
    }

}
