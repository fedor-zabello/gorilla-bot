package datastorage

import com.fasterxml.jackson.core.type.TypeReference
import util.CustomJacksonMapper
import java.io.File

object MatchJsonFileStorage {
    private val jsonMapper = CustomJacksonMapper.mapper
    val file = File("match.json")

    fun findAll(): MutableSet<UpcomingMatch> {
        return if (file.exists()) {
            return jsonMapper.readValue(file, object : TypeReference<MutableSet<UpcomingMatch>>() {})
        } else mutableSetOf()
    }

    fun saveAll(objectSet: Set<UpcomingMatch>) {
        jsonMapper.writeValue(file, objectSet)
    }
}