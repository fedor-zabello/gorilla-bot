package datastorage

import util.createObjectMapper
import java.io.File

class JsonFileStorage<T>(dataSource: File, private val clazz: Class<T>) {
    private val jsonMapper = createObjectMapper()
    val file: File = dataSource

    fun findAll(): MutableSet<T> {
        return if (file.exists()) {
            val list: List<T> =
                jsonMapper.readValue(file, jsonMapper.typeFactory.constructCollectionType(List::class.java, clazz))
            return list.toMutableSet()
        } else mutableSetOf()
    }

    fun saveAll(chatIds: Set<T>) {
        jsonMapper.writeValue(file, chatIds)
    }

    fun save(obj: T) {
        var objectSet = findAll()
        objectSet.add(obj)
        saveAll(objectSet)
    }
}