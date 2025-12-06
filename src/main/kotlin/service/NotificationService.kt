package service

import GorillaBot
import model.SpbhlMatch
import repository.ChatIdRepository
import util.MessageGenerator
import util.SpbhlMatchMapper

class NotificationService(
    private val chatIdJsonFileStorage: ChatIdRepository,
    private val gorillaBot: GorillaBot,
    private val spbhlMatchMapper: SpbhlMatchMapper,
    private val messageGenerator: MessageGenerator
) {
    private val adminChatId = 127845863L

    fun notifyForUpcomingMatch(match: SpbhlMatch) {
        println("Notifying all subscribers about upcoming match $match")

        val message = messageGenerator.getNotificationForUpcomingMatch(match)
        notifySubscribers(message)
    }

    fun notifyForResult(match: SpbhlMatch) {
        match.score ?: return
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        val message = when {
            matchResult.gorillaWon() -> {
                messageGenerator.getGorillaWonMessage(match)
            }

            matchResult.isDraw() -> messageGenerator.getDrawMessage(match)
            else -> messageGenerator.getDefeatMessage(match)
        }

        notifySubscribers(message)
    }

    fun notifyAdmin(message: String) {
        gorillaBot.sendMessage(adminChatId, message)
    }

    private fun notifySubscribers(message: String) {
        chatIdJsonFileStorage.findAll().forEach { chatId ->
            gorillaBot.sendMessage(chatId, message)
        }
    }
}
