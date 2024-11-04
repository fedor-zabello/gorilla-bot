import repository.ChatIdJsonFileStorage
import repository.GifStorage
import service.MatchService
import service.NotificationService
import service.SubscriptionService
import util.MessageGenerator
import util.SpbhlMatchMapper

fun main() {
    val chatIdJsonFileStorage = ChatIdJsonFileStorage("/var/lib/gorilla-bot/chat_id.json")
    val gifStorage = GifStorage(
        "/var/lib/gorilla-bot/gif/gorilla_wins.json",
        "/var/lib/gorilla-bot/gif/get_ready_for_match.json",
    )

    val messageGenerator = MessageGenerator()
    val spbhlMatchMapper = SpbhlMatchMapper()
    val matchService = MatchService(spbhlMatchMapper, messageGenerator)
    val subscriptionService = SubscriptionService(chatIdJsonFileStorage)
    val gorillaBot = GorillaBot(subscriptionService, matchService, messageGenerator)
    val notificationService = NotificationService(chatIdJsonFileStorage, gorillaBot, gifStorage, spbhlMatchMapper, messageGenerator)
    val scheduler = Scheduler(matchService, notificationService)

    scheduler.start()

    println("Application is ready")
}