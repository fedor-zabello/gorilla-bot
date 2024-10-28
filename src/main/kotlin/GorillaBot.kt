import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId

class GorillaBot {
    val spbhlService = SpbhlService()
    val bot = bot {
        token = System.getenv("TELEGRAM_API_KEY")
        dispatch {
            command("start") {
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Горилла вперёд!")
            }
            command("matches") {
                spbhlService.getUpcomingGames().forEach { bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = it) }
            }
        }
    }

    fun startPolling() {
        bot.startPolling()
    }
}