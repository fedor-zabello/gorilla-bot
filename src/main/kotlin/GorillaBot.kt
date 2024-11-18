import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ParseMode.MARKDOWN_V2
import com.github.kotlintelegrambot.entities.TelegramFile
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import service.MatchService
import service.SubscriptionService
import util.MessageGenerator

class GorillaBot(
    private val subscriptionService: SubscriptionService,
    private val matchService: MatchService,
    private val messageGenerator: MessageGenerator
) {
    private val bot: Bot

    init {
        println("Initialization of Telegram bot")
        bot = bot {
            token = System.getenv("TELEGRAM_API_KEY")
            dispatch {
                command("start") {
                    println("Received ${message.text} command")
                    sendMessage(message.chat.id, "Горилла вперёд\\! \uD83E\uDD8D")
                    sendMessage(message.chat.id, messageGenerator.getGreetingMessage())
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

                command("settings") {
                    if (subscriptionService.isSubscribed(message.chat.id)) {
                        val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                            listOf(
                                InlineKeyboardButton.CallbackData(
                                    text = "Выключить уведомления",
                                    callbackData = "disableSubscription"
                                )
                            ),
                        )
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = "Уведомления включены. Бот будет присылать уведомления о предстоящих и о результатах завершившихся игр",
                            replyMarkup = inlineKeyboardMarkup,
                        )
                    } else {
                        val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                            listOf(
                                InlineKeyboardButton.CallbackData(
                                    text = "Включить уведомления",
                                    callbackData = "enableSubscription"
                                )
                            )
                        )
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = "Уведомления выключены",
                            replyMarkup = inlineKeyboardMarkup,
                        )
                    }

                }

                callbackQuery("disableSubscription") {
                    println("Received ${callbackQuery.data} command")

                    val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                    subscriptionService.unsubscribe(chatId)
                    sendMessage(chatId, "Уведомления выключены")
                }

                callbackQuery("enableSubscription") {
                    println("Received ${callbackQuery.data} callback query")

                    val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                    subscriptionService.subscribe(chatId)
                    sendMessage(chatId, "Бот будет присылать уведомления об играх")
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
            parseMode = MARKDOWN_V2,
        )
        result.fold({
            println("Message is sent to chat $chatId, userName ${it.chat.username}.")
        }, {
            println("ERROR: Failed to send message. Error $it.")
        })
    }

    fun sendGifSilently(chatId: Long, gifSrc: String) {
        val telegramFile = TelegramFile.ByUrl(gifSrc)
        bot.sendAnimation(
            chatId = ChatId.fromId(chatId),
            animation = telegramFile,
            disableNotification = true
        )
    }
}
