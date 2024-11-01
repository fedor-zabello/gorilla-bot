fun main() {
    val chatIdJsonFileStorage = ChatIdJsonFileStorage("/var/lib/gorilla-bot/chat_id.json")
    val matchService = MatchService()
    val subscriptionService = SubscriptionService(chatIdJsonFileStorage)
    val gorillaBot = GorillaBot(subscriptionService, matchService)
    val notificationService = NotificationService(chatIdJsonFileStorage, gorillaBot)
    val scheduler = Scheduler(matchService, notificationService)

    scheduler.start()

    println("Application is ready")
}