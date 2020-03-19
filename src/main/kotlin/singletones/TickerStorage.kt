package singletones

import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

object TickerStorage {

    var needIncrease: Boolean = false
        set(value) {
            field = value
            if (period == 1L && value) {
                currentTaskName?.let { restart(it, false) }
            }
        }

    var currentTaskName: String? = null
        set(value) {
            field = value
            value?.let { start(it) }
        }

    lateinit var increaseTime: (String) -> Unit

    private var currentTimer: Timer? = null
        get() {
            return field ?: Timer(currentTaskName)
        }

    private var period: Long = 60

    private const val delay: Long = 60

    private fun countdown(taskName: String) {
        if (period == 1L) {
            if (!needIncrease) {
                cancel()
            } else {
                restart(taskName, true)
            }
        }
        period--
    }

    private fun start(taskName: String) {
        currentTimer?.scheduleAtFixedRate(period, delay) { countdown(taskName) }
    }

    private fun cancel() {
        currentTimer?.cancel()
    }

    private fun restart(taskName: String, needLog: Boolean = false) {

        period = 60
        needIncrease = false

        if (needLog) {
            increaseTime(taskName)
        }

        currentTaskName = taskName
    }
}