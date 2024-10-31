import datastorage.MatchJsonFileStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

object Scheduler {
    private val scheduledForNotify = ConcurrentHashMap.newKeySet<UpcomingMatch>()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            // Launch separate jobs
            launch { dailyDataUpdate() }
            launch { runNotificationChecker() }
        }
    }

    private suspend fun dailyDataUpdate() = coroutineScope {
        delay(60 * 1000L)
        while (true) {
            val matches = MatchService.getAllUpcoming()
            MatchJsonFileStorage.saveAll(matches.toSet())
            delay(24 * 60 * 60 * 1000L)
        }
    }

    private suspend fun runNotificationChecker() = coroutineScope {
        delay(90 * 1000L)
        while (true) {
            println("Checking database for objects to schedule notifications...")

            val matches = MatchService.getAllUpcoming()
            matches.filter { match ->
                match.date.toLocalDate() == LocalDate.now().plusDays(1)
                        && !scheduledForNotify.contains(match)
            }.forEach {
                var delay = calculateDelay(it.date)
                if (delay > 0) {
                    launch {
                        delay(delay)
                        NotificationService.notifyForUpcomingMatch(it)
                        scheduledForNotify.remove(it)
                    }
                    scheduledForNotify.add(it)
                    println("Scheduled notification for object ID: $it with delay: $delay ms")
                }
            }

            delay(3 * 60 * 60 * 1000) // Check every 3 hours
        }
    }

    private fun calculateDelay(matchDateTime: LocalDateTime): Long {
        var notificationTime = matchDateTime.minusDays(1).withHour(11)
        return Duration.between(LocalDateTime.now(), notificationTime).toMillis()
    }

}