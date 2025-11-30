package util

import kotlin.time.Duration
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

object DelayUtils {
    fun delayWithJitter(base: Duration, jitter: Duration): Duration =
        base + Random.nextLong(jitter.inWholeMilliseconds).milliseconds
}

