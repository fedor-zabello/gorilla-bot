package util

import java.time.format.DateTimeFormatter

object MessageGenerator {
    fun getMatchResultMessage(match: SpbhlMatch): String {
        return """
            *${match.teams}*
            счёт: ${match.score}
            дата: ${match.date.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}
        """.trimIndent().replace("-", "\\-")
    }

    fun getUpcomingMatchMessage(match: SpbhlMatch): String {
        return """
            *${match.teams}*
            дата: ${match.date.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}
            время: ${match.date.toLocalTime()}
            арена: ${match.arena}
        """.trimIndent().replace("-", "\\-")
    }

    fun getNotificationForUpcomingMatch(match: SpbhlMatch): String {
        return """
            *Завтра игра\!*
            ${match.teams}
            дата: ${match.date.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}
            время: ${match.date.toLocalTime()}
            арена: ${match.arena}
        """.trimIndent().replace("-", "\\-")
    }

    fun getFinishedMatchMessage(match: SpbhlMatch): String {
        var teams = match.teams.split(" - ")
        var score = parseScore(match.score!!)
        if (score == null) {
            throw IllegalArgumentException("Failed to parse score for ${match.teams}, score ${match.score}")
        }
        var homeTeam = teams[0]
        var awayTeam = teams[1]
        var homeTeamScore = score.first
        var awayTeamScore = score.second

        if (homeTeamScore == awayTeamScore) {
            return """
                *__Ничья__*
                ${match.teams}
                счёт: ${match.score}
            """.trimIndent().replace("-", "\\-")
        } else if (homeTeamScore > awayTeamScore && homeTeam.contains("Горилла")
            || awayTeamScore > homeTeamScore && awayTeam.contains("Горилла")
        ) {
            return """
                *__Победа__*
                ${match.teams}
                счёт: ${match.score}
            """.trimIndent().replace("-", "\\-")
        } else {
            return """
                *__Поражение__*
                ${match.teams}
                счёт: ${match.score}
            """.trimIndent().replace("-", "\\-")
        }
    }

    private fun parseScore(score: String): Pair<Int, Int>? {
        val regexp = Regex("""(\d+)\s*:\s*(\d+)""")
        val matchResult = regexp.find(score)

        return matchResult?.let {
            val (homeTeamScore, awayTeamScore) = it.destructured
            homeTeamScore.toInt() to awayTeamScore.toInt()
        }
    }
}