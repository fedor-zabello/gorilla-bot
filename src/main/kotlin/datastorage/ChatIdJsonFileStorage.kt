package datastorage

import com.fasterxml.jackson.core.type.TypeReference
import util.CustomJacksonMapper
import java.io.File

object ChatIdJsonFileStorage {
    private val jsonMapper = CustomJacksonMapper.mapper
    private val file = File("chat_id.json")

    @Synchronized
    fun findAll(): MutableSet<Long> {
        return if (file.exists()) {
            return jsonMapper.readValue(file, object : TypeReference<MutableSet<Long>>() {})
        } else mutableSetOf()
    }

    @Synchronized
    fun saveAll(chatIdSet: Set<Long>) {
        jsonMapper.writeValue(file, chatIdSet)
    }

    @Synchronized
    fun save(chatId: Long) {
        var chatIdSet = findAll()
        chatIdSet.add(chatId)
        saveAll(chatIdSet)
    }

    @Synchronized
    fun delete(chatId: Long) {
        var chatIdSet = findAll()
        chatIdSet.remove(chatId)
        saveAll(chatIdSet)
    }
}