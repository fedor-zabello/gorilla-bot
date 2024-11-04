package service

import model.SpbhlMatch
import repository.ChatIdRepository
import repository.GifStorage
import util.MessageGenerator.getDefeatMessage
import util.MessageGenerator.getDrawMessage
import util.MessageGenerator.getGorillaWonMessage
import util.MessageGenerator.getNotificationForUpcomingMatch
import util.SpbhlMatchMapper

class NotificationService(
    private val chatIdJsonFileStorage: ChatIdRepository,
    private val gorillaBot: GorillaBot,
    private val gifStorage: GifStorage
) {
    private val adminChatId = 127845863L

    fun notifyForUpcomingMatch(match: SpbhlMatch) {
        val message = getNotificationForUpcomingMatch(match)
        val gifUrl = gifStorage.findAnyUpcomingGifUrl()

        notifySubscribers(message, gifUrl)
    }

    fun notifyForResult(match: SpbhlMatch) {
        match.score ?: return
        val matchResult = SpbhlMatchMapper.spbhlMatchToMatchResult(match)

        var gifUrl: String? = null
        val message = when {
            matchResult.gorillaWon() -> {
                gifUrl = gifStorage.findAnyWinUrl()
                getGorillaWonMessage(match)
            }
            matchResult.isDraw() -> getDrawMessage(match)
            else -> getDefeatMessage(match)
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