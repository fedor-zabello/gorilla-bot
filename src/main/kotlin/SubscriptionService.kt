class SubscriptionService(
    val chatIdJsonFileStorage: ChatIdJsonFileStorage
) {
    fun subscribe(chatId: Long) {
        println("Saving chat $chatId for subscription")
        chatIdJsonFileStorage.save(chatId)
        println("Chat $chatId saved")
    }
    fun unsubscribe(chatId: Long) {
        println("Deleting chat $chatId from subscription")
        chatIdJsonFileStorage.delete(chatId)
        println("Chat $chatId deleted")
    }
}