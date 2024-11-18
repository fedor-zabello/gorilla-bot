package repository

interface ChatIdRepository {
    fun findAll(): MutableSet<Long>
    fun save(chatId: Long)
    fun delete(chatId: Long)
    fun exists(chatId: Long): Boolean
}