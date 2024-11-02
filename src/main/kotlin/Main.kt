fun main() {
    val chatIdJsonFileStorage = ChatIdJsonFileStorage("/var/lib/gorilla-bot/chat_id.json")
    val gifStorage = GifStorage(
        "/var/lib/gorilla-bot/gif/gorilla_wins.json",
        "/var/lib/gorilla-bot/gif/get_ready_for_match.json",
    )

    val matchService = MatchService()
    val subscriptionService = SubscriptionService(chatIdJsonFileStorage)
    val gorillaBot = GorillaBot(subscriptionService, matchService)
    val notificationService = NotificationService(chatIdJsonFileStorage, gorillaBot, gifStorage)
    val scheduler = Scheduler(matchService, notificationService)

    scheduler.start()

    println("Application is ready")
}