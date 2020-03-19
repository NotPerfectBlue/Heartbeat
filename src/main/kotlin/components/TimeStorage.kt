package components

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "TimeStorage",
    storages = [
        Storage(StoragePathMacros.CACHE_FILE)
    ])
class TimeStorage : PersistentStateComponent<TimeStorage> {

    private val timeMap: HashMap<String, Int> = hashMapOf()

    operator fun get(key: String): Int? = timeMap[key]

    operator fun set(key: String, value: Int) {
        timeMap[key] = value
    }

    operator fun iterator(): Iterator<Map.Entry<String, Int>> {
        return timeMap.iterator()
    }

    fun clear() {
        timeMap.clear()
    }

    fun increase(key: String) {
        timeMap.computeIfPresent(key) { _, v -> v + 1 }
    }

    fun addIfAbsent(taskName: String) {
        if (!timeMap.containsKey(taskName)) {
            timeMap[taskName] = 0
        }
    }

    override fun getState(): TimeStorage {
        return this
    }

    override fun loadState(state: TimeStorage) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): TimeStorage {
            return ServiceManager.getService(project, TimeStorage::class.java)
        }
    }

}