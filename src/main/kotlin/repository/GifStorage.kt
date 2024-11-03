package repository

import com.fasterxml.jackson.core.type.TypeReference
import util.CustomJacksonMapper
import java.io.File

class GifStorage(
    winGifsPath: String,
    upcomingGameGifsPath: String
) {
    private var winGifUrls = mutableListOf<String>()
    private val upcomingGifUrls = mutableListOf<String>()

    init {
        winGifUrls.addAll(CustomJacksonMapper.mapper.readValue(File(winGifsPath), object : TypeReference<MutableList<String>>() {}))
        upcomingGifUrls.addAll(CustomJacksonMapper.mapper.readValue(File(upcomingGameGifsPath), object : TypeReference<MutableSet<String>>() {}))
    }

    fun findAnyWinUrl(): String {
        return winGifUrls.random()
    }

    fun findAnyUpcomingGifUrl(): String {
        return upcomingGifUrls.random()
    }
}