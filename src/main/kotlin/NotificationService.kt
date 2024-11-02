import util.MessageGenerator.getFinishedMatchMessage
import util.MessageGenerator.getNotificationForUpcomingMatch

class NotificationService(
    private val chatIdJsonFileStorage: ChatIdJsonFileStorage,
    private val gorillaBot: GorillaBot,
    private val gifStorage: GifStorage
) {
    fun notifyForUpcomingMatch(match: SpbhlMatch) {
        val subscribersChatId = chatIdJsonFileStorage.findAll()
        subscribersChatId.forEach {
            gorillaBot.sendMessage(it, getNotificationForUpcomingMatch(match))
            gorillaBot.sendGifSilently(it, gifStorage.findAnyUpcomingGifUrl())
        }
    }
    fun notifyForResult(match: SpbhlMatch) {
        match.score ?: return

        val subscribersChatId = chatIdJsonFileStorage.findAll()
        subscribersChatId.forEach { gorillaBot.sendMessage(it, getFinishedMatchMessage(match)) }
    }
}