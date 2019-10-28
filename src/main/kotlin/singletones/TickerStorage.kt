package singletones

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.scheduleAtFixedRate

object TickerStorage : ConcurrentHashMap<String, Timer>() {

    var needInrease: Boolean = false
        set(value) {
            field = value
            if (period == 1L && value) {
                currentTaskName?.let { rerun(it, false) }
            }
        }

    lateinit var increaseTime: (String) -> Unit

    private var period: Long = 60
    private var currentTaskName: String? = null
        set(value) {
            field = value
            value?.let { start(it) }
        }

    private const val delay: Long = 60

    val countdown: (String) -> Unit =  { taskName ->
        if (period == 1L) {
            if (!needInrease) {
                cancel()
            } else {
                rerun(taskName, true)
            }
        }
        period--
    }

    fun start(taskName: String) {
        val timer = this[taskName]
        timer?.scheduleAtFixedRate(period, delay) { countdown(taskName) }
    }

    fun addIfAbsent(taskName: String) {
        if (!containsKey(taskName)) {
            this[taskName] = Timer(taskName)
        }
    }

    fun addIfAbsent(tasks: Sequence<String>) {
        for (task in tasks) {
            addIfAbsent(task)
        }
    }

    private fun cancel() {
        this[currentTaskName]?.cancel()
    }

    private fun rerun(taskName: String, needLog: Boolean = false) {
        period = 60
        needInrease = false

        if (needLog) {
            increaseTime(taskName)
        }

        this[taskName] = Timer(taskName)
        currentTaskName = taskName
    }
}