import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode.MARKDOWN_V2

class GorillaBot(
    private val subscriptionService: SubscriptionService,
    private val matchService: MatchService
) {
    private val bot: Bot

    init {
        println("Initialization of Telegram bot")
        bot = bot {
            token = System.getenv("TELEGRAM_API_KEY")
            dispatch {
                command("start") {
                    println("Received ${message.text} command")
                    sendMessage(message.chat.id, "Горилла вперёд!")
                    subscriptionService.subscribe(message.chat.id)
                }
                command("upcoming") {
                    println("Received ${message.text} command")
                    matchService.getNearestAsStrings().forEach { sendMessage(message.chat.id, it) }
                }
                command("last_result") {
                    println("Received ${message.text} command")
                    sendMessage(message.chat.id, matchService.getLastWithResult())
                }
                command("subscribe") {
                    println("Received ${message.text} command")
                    subscriptionService.subscribe(message.chat.id)
                }
                command("unsubscribe") {
                    println("Received ${message.text} command")
                    subscriptionService.unsubscribe(message.chat.id)
                }
            }
        }
        bot.startPolling()
        println("Initialization of Telegram bot finished. Bot is polling.")
    }

    fun sendMessage(chatId: Long, text: String) {
        var result = bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = text,
            parseMode = MARKDOWN_V2,)
        result.fold({
            println("Message is sent to chat $chatId, userName ${it.chat.username}.")
        }, {
            println("ERROR: Failed to send message. Error $it.")
        })
    }
}