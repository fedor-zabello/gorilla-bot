object NotificationService {
    fun notifyForUpcomingMatch(match: Match) {
        val subscribersChatId = ChatIdJsonFileStorage.findAll()
        subscribersChatId.forEach { GorillaBot.sendMessage(it, match.toString()) }
    }
    fun notifyForResult(match: Match) {
        val subscribersChatId = ChatIdJsonFileStorage.findAll()
        subscribersChatId.forEach { GorillaBot.sendMessage(it, match.toString()) }
    }
}