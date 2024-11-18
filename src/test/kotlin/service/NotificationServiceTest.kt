package service

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import model.SpbhlMatch
import model.SpbhlMatchResult
import org.junit.jupiter.api.BeforeEach
import repository.ChatIdRepository
import repository.GifStorage
import util.MessageGenerator
import util.SpbhlMatchMapper
import java.time.LocalDateTime
import kotlin.test.Test

class NotificationServiceTest {
    private val chatIdJsonFileStorage = mockk<ChatIdRepository>()
    private val gorillaBot = mockk<GorillaBot>()
    private val gifStorage = mockk<GifStorage>()
    private val spbhlMatchMapper = mockk<SpbhlMatchMapper>()
    private val messageGenerator = mockk<MessageGenerator>()
    private val notificationService = NotificationService(
        chatIdJsonFileStorage,
        gorillaBot,
        gifStorage,
        spbhlMatchMapper,
        messageGenerator
    )

    private val chatIsSet = mutableSetOf<Long>(111L, 222L, 333L, 444L, 555L)

    @BeforeEach
    fun init() {
        every { chatIdJsonFileStorage.findAll() } returns chatIsSet
        every { gorillaBot.sendGifSilently(any(), any()) } just Runs
        every { gorillaBot.sendMessage(any(), any()) } just Runs
    }

    @Test
    fun `Test notify all subscribers for upcoming match`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.now().plusDays(1),
            "ОЗЦ",
            "Горилла - Самураи Старт",
            null
        )
        val message = "match is scheduled for tomorrow"
        val gifUrl = "https://gif.com"

        every { messageGenerator.getNotificationForUpcomingMatch(any()) } returns message
        every { gifStorage.findAnyUpcomingGifUrl() } returns gifUrl

        notificationService.notifyForUpcomingMatch(match)

        chatIsSet.forEach { chatId ->
            verify(exactly = 1) { gorillaBot.sendGifSilently(chatId, gifUrl) }
            verify(exactly = 1) { gorillaBot.sendMessage(chatId, message) }
        }
    }

    @Test
    fun `Test notify for win`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.now().plusDays(1),
            "ОЗЦ",
            "Горилла - Самураи Старт",
            "4 - 2"
        )
        val result = SpbhlMatchResult("Горилла" to "Самураи Старт", 4 to 2)
        val message = "Gorilla Won"
        val gifUrl = "https://gif.com"

        every { spbhlMatchMapper.spbhlMatchToMatchResult(match) } returns result
        every { gifStorage.findAnyWinUrl() } returns gifUrl
        every { messageGenerator.getGorillaWonMessage(match) } returns message

        notificationService.notifyForResult(match)

        verify(exactly = 1) { messageGenerator.getGorillaWonMessage(match) }
        chatIsSet.forEach { chatId ->
            verify(exactly = 1) { gorillaBot.sendGifSilently(chatId, gifUrl) }
            verify(exactly = 1) { gorillaBot.sendMessage(chatId, message) }
        }
    }

    @Test
    fun `Test notify for draw`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.now().plusDays(1),
            "ОЗЦ",
            "Горилла - Самураи Старт",
            "4 - 4"
        )
        val result = SpbhlMatchResult("Горилла" to "Самураи Старт", 4 to 4)
        val message = "It's a draw"
        val gifUrl = "https://gif.com"

        every { spbhlMatchMapper.spbhlMatchToMatchResult(match) } returns result
        every { gifStorage.findAnyWinUrl() } returns gifUrl
        every { messageGenerator.getDrawMessage(match) } returns message

        notificationService.notifyForResult(match)

        verify(exactly = 1) { messageGenerator.getDrawMessage(match) }
        chatIsSet.forEach { chatId ->
            verify(exactly = 0) { gorillaBot.sendGifSilently(chatId, gifUrl) }
            verify(exactly = 1) { gorillaBot.sendMessage(chatId, message) }
        }
    }

    @Test
    fun `Test notify for defeat`() {
        val match = SpbhlMatch(
            "TSP CUP - 2024 (Старт A) Групповой этап",
            LocalDateTime.now().plusDays(1),
            "ОЗЦ",
            "Горилла - Самураи Старт",
            "2 - 4"
        )
        val result = SpbhlMatchResult("Горилла" to "Самураи Старт", 2 to 4)
        val message = "defeat"
        val gifUrl = "https://gif.com"

        every { spbhlMatchMapper.spbhlMatchToMatchResult(match) } returns result
        every { gifStorage.findAnyWinUrl() } returns gifUrl
        every { messageGenerator.getDefeatMessage(match) } returns message

        notificationService.notifyForResult(match)

        verify(exactly = 1) { messageGenerator.getDefeatMessage(match) }
        chatIsSet.forEach { chatId ->
            verify(exactly = 0) { gorillaBot.sendGifSilently(chatId, gifUrl) }
            verify(exactly = 1) { gorillaBot.sendMessage(chatId, message) }
        }
    }

    @Test
    fun `Test admin notification`() {
        val message = "something happened"

        notificationService.notifyAdmin(message)

        verify { gorillaBot.sendMessage(127845863L, message) }
    }
}