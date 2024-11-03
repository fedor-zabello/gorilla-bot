package util

import model.SpbhlMatch
import model.SpbhlMatchDto
import util.DateTimeUtils.parseToLocalDateTime

object SpbhlMatchMapper {
    fun dtoToMatch(dto: SpbhlMatchDto): SpbhlMatch {
        val tournament = dto.tournament
        val date = parseToLocalDateTime(dto.date, dto.time)
        val arena = dto.arena
        val teams = dto.teams
        val score = if (dto.score.isNotEmpty()) dto.score else null
        return SpbhlMatch(tournament, date, arena, teams, score)
    }
}