package util

import model.SpbhlMatch
import java.time.format.DateTimeFormatter

object MessageGenerator {
    fun getGreetingMessage(): String {
        return """
            Что умеет этот бот: 
            - показывает три ближайших матча команд Горилла и Горилла-2
            - показывает результат последней игры
            - уведомляет о предстоящей игре
            - уведомляет о результатах завершившегося матча
            По умолчанию уведомления включены. Их можно отключить командой `unsubscribe`.
            Все данные с сайта [https://spbhl.ru](https://spbhl.ru/)
        """.trimIndent().replace("-", "\\-").replace(".", "\\.")
    }

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

    fun getGorillaWonMessage(match: SpbhlMatch): String {
        return getMessageForEndedMatch(match, "*__Победа__*")
    }

    fun getDrawMessage(match: SpbhlMatch): String {
        return getMessageForEndedMatch(match, "*__Ничья__*")
    }

    fun getDefeatMessage(match: SpbhlMatch): String {
        return getMessageForEndedMatch(match, "*__Поражение__*")
    }

    private fun getMessageForEndedMatch(match: SpbhlMatch, result: String): String {
        return """
            $result
            ${match.teams}
            счёт: ${match.score}
        """.trimIndent().replace("-", "\\-")
    }
}