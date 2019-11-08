package singletones

import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

object TickerStorage {

    var needInrease: Boolean = false
        set(value) {
            field = value
            if (period == 1L && value) {
                currentTaskName?.let { restart(it, false) }
            }
        }

    lateinit var increaseTime: (String) -> Unit

    private var currentTaskName: String? = null
        set(value) {
            field = value
            value?.let { start(it) }
        }

    private var currentTimer: Timer? = null
        get() {
            return field ?: Timer(currentTaskName)
        }

    private var period: Long = 60
    private const val delay: Long = 60

    val countdown: (String) -> Unit =  { taskName ->
        if (period == 1L) {
            if (!needInrease) {
                cancel()
            } else {
                restart(taskName, true)
            }
        }
        period--
    }

    fun start(taskName: String) {
        currentTaskName = taskName
        currentTimer?.scheduleAtFixedRate(period, delay) { countdown(taskName) }
    }

    private fun cancel() {
        currentTimer?.cancel()
    }

    private fun restart(taskName: String, needLog: Boolean = false) {
        period = 60
        needInrease = false

        if (needLog) {
            increaseTime(taskName)
        }

        currentTaskName = taskName
    }
}