import util.MessageGenerator.getFinishedMatchMessage
import util.MessageGenerator.getNotificationForUpcomingMatch

class NotificationService(
    private val chatIdJsonFileStorage: ChatIdJsonFileStorage,
    private val gorillaBot: GorillaBot
) {
    fun notifyForUpcomingMatch(match: SpbhlMatch) {
        val subscribersChatId = chatIdJsonFileStorage.findAll()
        subscribersChatId.forEach { gorillaBot.sendMessage(it, getNotificationForUpcomingMatch(match)) }
    }
    fun notifyForResult(match: SpbhlMatch) {
        val subscribersChatId = chatIdJsonFileStorage.findAll()
        subscribersChatId.forEach { gorillaBot.sendMessage(it, getFinishedMatchMessage(match)) }
    }
}