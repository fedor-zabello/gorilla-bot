import repository.ChatIdJsonFileStorage
import service.MatchService
import service.NotificationService
import service.SpbhlClient
import service.SubscriptionService
import util.MessageGenerator
import util.SpbhlMatchMapper

fun main() {
    val chatIdJsonFileStorage = ChatIdJsonFileStorage("/var/lib/gorilla-bot/ ")

    val messageGenerator = MessageGenerator()
    val spbhlMatchMapper = SpbhlMatchMapper()
    val spbhlClient = SpbhlClient()
    val matchService = MatchService(spbhlMatchMapper, messageGenerator, spbhlClient)
    val subscriptionService = SubscriptionService(chatIdJsonFileStorage)
    val gorillaBot = GorillaBot(subscriptionService, matchService, messageGenerator)
    val notificationService =
        NotificationService(chatIdJsonFileStorage, gorillaBot, spbhlMatchMapper, messageGenerator)
    val scheduler = Scheduler(matchService, notificationService)

    scheduler.start()

    println("Application is ready")
}