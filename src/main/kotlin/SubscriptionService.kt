import datastorage.ChatIdJsonFileStorage

object SubscriptionService {
    fun subscribe(chatId: Long) {
        ChatIdJsonFileStorage.save(chatId)
    }

    fun unsubscribe(chatId: Long) {
        ChatIdJsonFileStorage.delete(chatId)
    }
}