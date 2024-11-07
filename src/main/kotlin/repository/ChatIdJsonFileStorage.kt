package repository

import com.fasterxml.jackson.core.type.TypeReference
import util.CustomJacksonMapper
import java.io.File

class ChatIdJsonFileStorage(
    dataSource: String
) : ChatIdRepository {
    private val jsonMapper = CustomJacksonMapper.mapper
    private val chatIds = mutableSetOf<Long>()
    private val file = File(dataSource)

    @Synchronized
    override fun findAll(): MutableSet<Long> {
        return if (chatIds.isNotEmpty()) {
            chatIds
        } else if (file.exists()) {
            chatIds.addAll(jsonMapper.readValue(file, object : TypeReference<MutableSet<Long>>() {}))
            chatIds
        } else {
            mutableSetOf()
        }
    }

    @Synchronized
    override fun save(chatId: Long) {
        var chatIdSet = findAll()
        chatIdSet.add(chatId)
        saveAll(chatIdSet)
    }

    @Synchronized
    override fun delete(chatId: Long) {
        var chatIdSet = findAll()
        chatIdSet.remove(chatId)
        saveAll(chatIdSet)
    }

    private fun saveAll(chatIdSet: Set<Long>) {
        jsonMapper.writeValue(file, chatIdSet)
    }
}