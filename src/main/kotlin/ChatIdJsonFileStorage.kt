import com.fasterxml.jackson.core.type.TypeReference
import util.CustomJacksonMapper
import java.io.File

class ChatIdJsonFileStorage(
    dataSource: String
) {
    private val jsonMapper = CustomJacksonMapper.mapper
    private val chatIds = mutableSetOf<Long>()
    private val file = File(dataSource)

    @Synchronized
    fun findAll(): MutableSet<Long> {
        return if (chatIds.isNotEmpty()) {
            chatIds
        } else if (file.exists()) {
            jsonMapper.readValue(file, object : TypeReference<MutableSet<Long>>() {})
        } else {
            mutableSetOf()
        }
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

    private fun saveAll(chatIdSet: Set<Long>) {
        jsonMapper.writeValue(file, chatIdSet)
    }
}