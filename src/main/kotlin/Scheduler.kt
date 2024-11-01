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

    fun start() {
        CoroutineScope(Dispatchers.Default).launch {
            launch { runNotificationChecker() }
        }
        println("Scheduler is started")
    }

    private suspend fun runNotificationChecker() = coroutineScope {
        delay(90 * 1000L)
        while (true) {
            println("Checking upcoming matches on spbhl...")

            val matches = matchService.getAllUpcoming()

            matches.filter { match ->
                match.date.toLocalDate() == LocalDate.now().plusDays(1)
                        && !scheduledForNotify.contains(match)
            }.forEach {
                var delay = DateTimeUtils.calculateDelayForNotification(it.date)
                if (delay > 0) {
                    launch {
                        delay(delay)
                        notificationService.notifyForUpcomingMatch(it)
                        scheduledForNotify.remove(it)
                    }
                    scheduledForNotify.add(it)
                    println("Scheduled notification for match ${it.teams} match time ${it.date} with delay: $delay ms")
                }
            }

            matches.filter { match ->
                match.date.toLocalDate() == LocalDate.now()
                        && !scheduledForCheckResult.contains(match)
            }.forEach { match ->
                var delay = DateTimeUtils.calculateDelayForScoreCheck(match.date)
                if (delay > 0) {
                    launch {
                        delay(delay)
                        var scoreDiscovered = false
                        while (!scoreDiscovered) {
                            matchService.getAll().filter { updatedMatch ->
                                updatedMatch.teams == match.teams
                                        && updatedMatch.date == match.date
                                        && updatedMatch.score != ""
                            }.forEach {
                                notificationService.notifyForResult(it)
                                scoreDiscovered = true
                            }
                            delay(5 * 60 * 1000)
                        }
                        scheduledForCheckResult.remove(match)
                    }
                    scheduledForCheckResult.add(match)
                    println("Scheduled notification for result of the match ${match.teams} match time ${match.date} with delay: $delay ms")
                }
            }

            delay(3 * 60 * 60 * 1000) // Check every 3 hours
        }
    }
}