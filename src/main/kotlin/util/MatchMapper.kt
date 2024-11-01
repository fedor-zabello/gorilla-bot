package util

import Match
import SpbhlMatchDto
import util.DateTimeUtils.parseToLocalDateTime

object MatchMapper {
    fun dtoToUpcomingMatch(dto: SpbhlMatchDto): Match {
        val tournament = dto.tournament
        val date = parseToLocalDateTime(dto.date, dto.time)
        val arena = dto.arena
        val teams = dto.teams
        val score = dto.score
        return Match(tournament, date, arena, teams, score)
    }
}