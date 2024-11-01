class NotificationService(
    private val chatIdJsonFileStorage: ChatIdJsonFileStorage,
    private val gorillaBot: GorillaBot
) {
    fun notifyForUpcomingMatch(match: SpbhlMatch) {
        val subscribersChatId = chatIdJsonFileStorage.findAll()
        subscribersChatId.forEach { gorillaBot.sendMessage(it, match.toString()) }
    }
    fun notifyForResult(match: SpbhlMatch) {
        val subscribersChatId = chatIdJsonFileStorage.findAll()
        subscribersChatId.forEach { gorillaBot.sendMessage(it, match.toString()) }
    }
}