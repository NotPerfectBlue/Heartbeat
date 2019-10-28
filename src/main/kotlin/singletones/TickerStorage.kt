package singletones

import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TickerStorage : ConcurrentHashMap<String, Timer>()