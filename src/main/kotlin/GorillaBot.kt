import MatchService.getNearestAsStrings
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId

object GorillaBot {
    private val bot: Bot

    init {
        println("Initialization of Telegram bot")
        bot = bot {
            token = System.getenv("TELEGRAM_API_KEY")
            dispatch {
                command("start") {
                    println("Received ${message.text} command")
                    sendMessage(message.chat.id, "Горилла вперёд!")
                    SubscriptionService.subscribe(message.chat.id)
                }
                command("matches") {
                    println("Received ${message.text} command")
                    getNearestAsStrings().forEach { sendMessage(message.chat.id, it) }
                }
                command("subscribe") {
                    println("Received ${message.text} command")
                    SubscriptionService.subscribe(message.chat.id)
                }
                command("unsubscribe") {
                    println("Received ${message.text} command")
                    SubscriptionService.unsubscribe(message.chat.id)
                }
            }
        }
        bot.startPolling()
        println("Initialization of Telegram bot finished. Bot is polling.")
    }

    fun sendMessage(chatId: Long, text: String) {
        var result = bot.sendMessage(ChatId.fromId(chatId), text = text)
        result.fold({
            println("Message is sent to chat $chatId, userName ${it.chat.username}.")
        }, {
            println("ERROR: Failed to send message. Error $it.")
        })
    }
}