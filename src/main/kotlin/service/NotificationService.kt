package service

import model.SpbhlMatch
import repository.ChatIdRepository
import repository.GifStorage
import util.MessageGenerator
import util.SpbhlMatchMapper

class NotificationService(
    private val chatIdJsonFileStorage: ChatIdRepository,
    private val gorillaBot: GorillaBot,
    private val gifStorage: GifStorage,
    private val spbhlMatchMapper: SpbhlMatchMapper,
    private val messageGenerator: MessageGenerator
) {
    private val adminChatId = 127845863L

    fun notifyForUpcomingMatch(match: SpbhlMatch) {
        val message = messageGenerator.getNotificationForUpcomingMatch(match)
        val gifUrl = gifStorage.findAnyUpcomingGifUrl()

        notifySubscribers(message, gifUrl)
    }

    fun notifyForResult(match: SpbhlMatch) {
        match.score ?: return
        val matchResult = spbhlMatchMapper.spbhlMatchToMatchResult(match)

        var gifUrl: String? = null
        val message = when {
            matchResult.gorillaWon() -> {
                gifUrl = gifStorage.findAnyWinUrl()
                messageGenerator.getGorillaWonMessage(match)
            }
            matchResult.isDraw() -> messageGenerator.getDrawMessage(match)
            else -> messageGenerator.getDefeatMessage(match)
        }

        notifySubscribers(message, gifUrl)
    }

    fun notifyAdmin(message: String) {
        gorillaBot.sendMessage(adminChatId, message)
    }

    private fun notifySubscribers(message: String, gifUrl: String?) {
        chatIdJsonFileStorage.findAll().forEach { chatId ->
            gifUrl?.let { gorillaBot.sendGifSilently(chatId, it) }
            gorillaBot.sendMessage(chatId, message)
        }
    }
}