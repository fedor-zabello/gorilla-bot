package service

import repository.ChatIdRepository

class SubscriptionService(
    val chatIdJsonFileStorage: ChatIdRepository
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