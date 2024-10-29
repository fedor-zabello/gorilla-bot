import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId

class GorillaBot {
    private val matchService = MatchService()
    private val bot: Bot

    init {
        bot = bot {
            token = System.getenv("TELEGRAM_API_KEY")
            dispatch {
                command("start") {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Горилла вперёд!")
                }
                command("matches") {
                    matchService.getNearestAsStrings().forEach { bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = it) }
                }
            }
        }
        bot.startPolling()
    }
}