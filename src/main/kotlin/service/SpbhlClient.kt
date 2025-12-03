package service

import model.SpbhlMatchDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class SpbhlClient(
        private val spbhlUrl: String = "https://spbhl.ru",
        private val teamIds: List<String> =
                listOf(
                        "b82f09ec-bf65-4d64-881f-0bef1598d936", // Gorilla
//                        "dc079cd6-d6e2-4b12-b7fa-b8676195a081", // Gorilla-2
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
        println("Get all matches from website $url")

        val connection = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
            .header("Accept-Language", "en-US,en;q=0.9,ru;q=0.8")
            .header("Accept-Encoding", "gzip, deflate, br, zstd")
            .header("Connection", "keep-alive")
            .header("Upgrade-Insecure-Requests", "1")
            .header("Sec-Fetch-Dest", "document")
            .header("Sec-Fetch-Mode", "navigate")
            .header("Sec-Fetch-Site", "none")
            .header("Sec-Fetch-User", "?1")
            .header("Cache-Control", "max-age=0")
            .referrer("https://www.google.com/")
            .timeout(20000)
            .followRedirects(true)
            .ignoreContentType(true)

        return connection.get()
    }

}
