package datastorage

import kotlin.test.Test
import kotlin.test.assertEquals

class JsonFileStorageTest {
    @Test
    fun `Save chatId to storage and find it in storage`() {
        ChatIdJsonFileStorage.save(12345)
        val foundChatId = ChatIdJsonFileStorage.findAll().first()

        assertEquals(12345, foundChatId)
    }
}