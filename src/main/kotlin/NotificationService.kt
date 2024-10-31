import datastorage.ChatIdJsonFileStorage

object NotificationService {
    fun notifyForUpcomingMatch(match: UpcomingMatch) {
        val subscribersChatId = ChatIdJsonFileStorage.findAll()
        subscribersChatId.forEach { GorillaBot.sendMessage(it, match.toString()) }
    }
}