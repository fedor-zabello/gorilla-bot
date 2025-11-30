import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import model.SpbhlMatch
import service.MatchService
import service.NotificationService
import util.DateTimeUtils
import util.DelayUtils
import kotlin.time.Duration.Companion.seconds

class Scheduler(
    private val matchService: MatchService,
    private val notificationService: NotificationService
) {
    private val scheduledForNotify = ConcurrentHashMap.newKeySet<SpbhlMatch>()
    private val scheduledForCheckResult = ConcurrentHashMap.newKeySet<SpbhlMatch>()

    fun start() =
        CoroutineScope(Dispatchers.Default).launch {
            runNotificationChecker()
            println("Scheduler is started")
        }

    private suspend fun runNotificationChecker() = coroutineScope {
        delay(5.seconds) // Initial delay
        while (isActive) {
            println("Checking upcoming matches on spbhl...")
            val upcomingMatches = matchService.getAllUpcoming()
            launch { scheduleNotifications(upcomingMatches) }
            launch { scheduleScoreChecks(upcomingMatches) }

            delay(DelayUtils.delayWithJitter(6.hours, 30.minutes))
        }
    }

    private suspend fun scheduleNotifications(upcomingMatches: List<SpbhlMatch>) = coroutineScope {
        println("Checking upcoming matches for notifications")
        upcomingMatches
            .filter { match ->
                match.date.toLocalDate() == LocalDate.now().plusDays(1) &&
                        !scheduledForNotify.contains(match)
            }
            .forEach { match ->
                val notificationDelay = DateTimeUtils.calculateDelayForNotification(match.date)
                if (notificationDelay > 0) {
                    scheduledForNotify.add(match)
                    launch {
                        delay(notificationDelay)
                        try {
                            notificationService.notifyForUpcomingMatch(match)
                        } catch (e: Exception) {
                            notificationService.notifyAdmin(
                                "Error while trying to notify for upcoming match. Match $match. Error: ${e.message}"
                            )
                        }
                        scheduledForNotify.remove(match)
                    }
                    println(
                        "Scheduled notification for match ${match.teams} at ${match.date} with delay: $notificationDelay ms"
                    )
                }
            }
    }

    private suspend fun scheduleScoreChecks(upcomingMatches: List<SpbhlMatch>) = coroutineScope {
        println("Checking today's matches for score updates")
        upcomingMatches
            .filter { match ->
                match.date.toLocalDate() == LocalDate.now() &&
                        !scheduledForCheckResult.contains(match)
            }
            .forEach { match ->
                val resultCheckDelay = DateTimeUtils.calculateDelayForScoreCheck(match.date)
                if (resultCheckDelay > 0) {
                    println("found match for score check")
                    scheduledForCheckResult.add(match)
                    launch {
                        delay(resultCheckDelay)
                        try {
                            checkForScoreUpdates(match)
                        } catch (e: Exception) {
                            notificationService.notifyAdmin(
                                "Error while trying to check and send match score. Match $match. Error: ${e.message}"
                            )
                        }
                        scheduledForCheckResult.remove(match)
                    }
                    println(
                        "Scheduled result check for match ${match.teams} at ${match.date} with delay: $resultCheckDelay ms"
                    )
                }
            }
    }

    private suspend fun checkForScoreUpdates(match: SpbhlMatch) {
        var scoreDiscovered = false
        while (!scoreDiscovered) {
            matchService.getAll()
                .firstOrNull { updatedMatch ->
                    updatedMatch.teams == match.teams &&
                            updatedMatch.date == match.date &&
                            updatedMatch.score != null
                }
                ?.let { updatedMatch ->
                    notificationService.notifyForResult(updatedMatch)
                    scoreDiscovered = true
                }
            delay(DelayUtils.delayWithJitter(5.minutes, 5.minutes))
        }
    }
}
