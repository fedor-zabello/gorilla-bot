package util

import UpcomingMatch
import dto.SpbhlMatchDto
import util.DateTimeParser.parseToZonedDateTime

object MatchMapper {
    fun dtoToUpcomingMatch(dto: SpbhlMatchDto): UpcomingMatch {
        val tournament = dto.tournament
        val date = parseToZonedDateTime(dto.date, dto.time)
        val arena = dto.arena
        val teams = dto.teams
        return UpcomingMatch(tournament, date, arena, teams)
    }
}