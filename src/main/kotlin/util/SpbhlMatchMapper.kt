package util

import model.SpbhlMatch
import model.SpbhlMatchDto
import model.SpbhlMatchResult
import util.DateTimeUtils.parseToLocalDateTime

class SpbhlMatchMapper {
    fun dtoToSpbhlMatch(dto: SpbhlMatchDto): SpbhlMatch? {
        try {
            val tournament = dto.tournament
            val date = parseToLocalDateTime(dto.date, dto.time)
            val arena = dto.arena
            val teams = dto.teams
            val score = if (dto.score.isNotEmpty()) dto.score else null
            return SpbhlMatch(tournament, date, arena, teams, score)
        } catch (e: IllegalArgumentException) {
            println("Failed to map matchDto $dto. ${e.message}")
            return null
        }
    }

    fun spbhlMatchToMatchResult(match: SpbhlMatch): SpbhlMatchResult {
        match.score ?: throw IllegalArgumentException("Can't map match result ${match.teams} because score is null")

        var teams = parseTeams(match.teams)
        var score = parseScore(match.score)

        teams ?: throw IllegalArgumentException("Failed to parse teams for ${match.teams}, score ${match.score}")
        score ?: throw IllegalArgumentException("Failed to parse score for ${match.teams}, score ${match.score}")

        return SpbhlMatchResult(teams, score)
    }

    private fun parseTeams(teams: String): Pair<String, String>? {
        val regexp = Regex("""([\p{L}\d\s-]+) - ([\p{L}\d\s-]+)""")
        val matchResult = regexp.find(teams)

        return matchResult?.let {
            val (homeTeam, awayTeam) = it.destructured
            homeTeam to awayTeam
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