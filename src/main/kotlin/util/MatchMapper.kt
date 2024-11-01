package util

import SpbhlMatch
import SpbhlMatchDto
import util.DateTimeUtils.parseToLocalDateTime

object MatchMapper {
    fun dtoToMatch(dto: SpbhlMatchDto): SpbhlMatch {
        val tournament = dto.tournament
        val date = parseToLocalDateTime(dto.date, dto.time)
        val arena = dto.arena
        val teams = dto.teams
        val score = dto.score
        return SpbhlMatch(tournament, date, arena, teams, score)
    }
}