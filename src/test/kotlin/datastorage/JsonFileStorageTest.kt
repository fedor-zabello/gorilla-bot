package datastorage

import UpcomingMatch
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonFileStorageTest {

    @Test
    fun `Save match to storage and find it in storage`() {
        var match = getMatch()
        var matches = mutableSetOf<UpcomingMatch>(match)

        MatchJsonFileStorage.saveAll(matches)
        var foundMatch = MatchJsonFileStorage.findAll().first()

        assertEquals(match, foundMatch)
    }

    private fun getMatch(): UpcomingMatch {
        return UpcomingMatch(
            tournament = "Старт 1A",
            date = LocalDateTime.of(2024, 11, 14, 20, 15, 0, 0),
            arena = "ОЗБ",
            teams = "Горилла - Мегавольт",
        )
    }
}