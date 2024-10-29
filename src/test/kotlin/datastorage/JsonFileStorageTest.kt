package datastorage

import UpcomingMatch
import java.io.File
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonFileStorageTest {
    @Test
    fun `Save match to storage and find it in storage`() {
        var matchStorage = JsonFileStorage(File("src/test/resources/match.json"), UpcomingMatch::class.java)
        var match = getMatch()
        var matches = mutableSetOf<UpcomingMatch>(match)

        matchStorage.saveAll(matches)
        var foundMatch = matchStorage.findAll().first()

        assertEquals(match.tournament, foundMatch.tournament)
        assertEquals(match.arena, foundMatch.arena)
        assertEquals(match.teams, foundMatch.teams)
        assertEquals(match.date.toInstant(), foundMatch.date.toInstant())
    }

    private fun getMatch(): UpcomingMatch {
        return UpcomingMatch(
            tournament = "Старт 1A",
            date = ZonedDateTime.of(2024, 11, 14, 20, 15, 0, 0, ZoneId.of("GMT+3")),
            arena = "ОЗБ",
            teams = "Горилла - Мегавольт",
        )
    }
}