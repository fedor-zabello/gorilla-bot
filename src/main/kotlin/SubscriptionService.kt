object SubscriptionService {
    fun subscribe(chatId: Long) {
        println("Saving chat $chatId for subscription")
        ChatIdJsonFileStorage.save(chatId)
        println("Chat $chatId saved")
    }
    fun unsubscribe(chatId: Long) {
        println("Deleting chat $chatId from subscription")
        ChatIdJsonFileStorage.delete(chatId)
        println("Chat $chatId deleted")
    }
}