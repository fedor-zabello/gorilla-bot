package service

import model.SpbhlMatch
import util.MessageGenerator
import util.SpbhlMatchMapper
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.take

class MatchService(
    private val spbhlMatchMapper: SpbhlMatchMapper,
    private val messageGenerator: MessageGenerator,
    private val spbhClient: SpbhClient
) {
    private val nearestMatchesCount = 3

    fun getNearestAsStrings(): List<String> {
        return getAllUpcoming()
            .sortedBy { it.date }
            .map { messageGenerator.getUpcomingMatchMessage(it) }
            .take(nearestMatchesCount)
    }

    fun getAllUpcoming():List<SpbhlMatch> {
        return spbhClient.getAllMatches().filter { it.score == "" }.map{ spbhlMatchMapper.dtoToSpbhlMatch(it) }
    }

    fun getAll(): List<SpbhlMatch> {
        return spbhClient.getAllMatches().map{ spbhlMatchMapper.dtoToSpbhlMatch(it) }
    }

    fun getLastWithResult(): String {
        val latsMatchWithResultDto = spbhClient.getAllMatches()
            .filter { it.score.isNotEmpty() }
            .map{ spbhlMatchMapper.dtoToSpbhlMatch(it) }
            .maxByOrNull { it.date }

        return latsMatchWithResultDto?.let { messageGenerator.getMatchResultMessage(it) }
            ?: "Не удалось определить результат последнего матча"
    }
}