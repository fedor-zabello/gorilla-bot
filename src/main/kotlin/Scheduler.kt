import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import util.DateTimeUtils
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

class Scheduler(
    private val matchService: MatchService,
    private val notificationService: NotificationService
) {
    private val scheduledForNotify = ConcurrentHashMap.newKeySet<SpbhlMatch>()
    private val scheduledForCheckResult = ConcurrentHashMap.newKeySet<SpbhlMatch>()

    fun start() = CoroutineScope(Dispatchers.Default).launch {
        runNotificationChecker()
        println("Scheduler is started")
    }

    private suspend fun runNotificationChecker() = coroutineScope {
        delay(5 * 1000L) // Initial delay
        while (true) {
            println("Checking upcoming matches on spbhl...")
            launch { scheduleNotifications() }
            launch { scheduleScoreChecks() }
            delay(3 * 60 * 60 * 1000) // Check every 3 hours
        }
    }

    private suspend fun scheduleNotifications() = coroutineScope {
        println("Check matches for notifications")
        matchService.getAllUpcoming()
            .filter { match ->
                match.date.toLocalDate() == LocalDate.now().plusDays(1) && !scheduledForNotify.contains(match)
            }
            .forEach { match ->
                val notificationDelay = DateTimeUtils.calculateDelayForNotification(match.date)
                if (notificationDelay > 0) {
                    scheduledForNotify.add(match)
                    launch {
                        delay(notificationDelay)
                        notificationService.notifyForUpcomingMatch(match)
                        scheduledForNotify.remove(match)
                    }
                    println("Scheduled notification for match ${match.teams} at ${match.date} with delay: $notificationDelay ms")
                }
            }
    }

    private suspend fun scheduleScoreChecks() = coroutineScope {
        println("Check for score checks")
        matchService.getAllUpcoming()
            .filter { match ->
                match.date.toLocalDate() == LocalDate.now() && !scheduledForCheckResult.contains(match)
            }
            .forEach { match ->
                val resultCheckDelay = DateTimeUtils.calculateDelayForScoreCheck(match.date)
                if (resultCheckDelay > 0) {
                    println("found match for score check")
                    scheduledForCheckResult.add(match)
                    launch {
                        delay(resultCheckDelay)
                        checkForScoreUpdates(match)
                        scheduledForCheckResult.remove(match)
                    }
                    println("Scheduled result check for match ${match.teams} at ${match.date} with delay: $resultCheckDelay ms")
                }
            }
    }

    private suspend fun checkForScoreUpdates(match: SpbhlMatch) {
        var scoreDiscovered = false
        while (!scoreDiscovered) {
            matchService.getAll().firstOrNull { updatedMatch ->
                updatedMatch.teams == match.teams
                        && updatedMatch.date == match.date
                        && updatedMatch.score != null
            }?.let { updatedMatch ->
                notificationService.notifyForResult(updatedMatch)
                scoreDiscovered = true
            }
            delay(5 * 60 * 1000) // Check every 5 minutes
        }
    }
}
