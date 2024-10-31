import MatchService.getNearestAsStrings
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId

object GorillaBot {
    private val bot: Bot

    init {
        bot = bot {
            token = System.getenv("TELEGRAM_API_KEY")
            dispatch {
                command("start") {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Горилла вперёд!")
                    SubscriptionService.subscribe(message.chat.id)
                }
                command("matches") {
                    getNearestAsStrings().forEach { bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = it) }
                }
                command("subscribe") {
                    SubscriptionService.subscribe(message.chat.id)
                }
                command("unsubscribe") {
                    SubscriptionService.unsubscribe(message.chat.id)
                }
            }
        }
        bot.startPolling()
    }

    fun sendMessage(chatId: Long, text: String) {
        bot.sendMessage(ChatId.fromId(chatId), text = text)
    }
}