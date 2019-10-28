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

    val timeMap: HashMap<String, Int> = hashMapOf()

    operator fun get(key: String): Int? = timeMap[key]

    operator fun set(key: String, value: Int) {
        timeMap[key] = value
    }

    override fun getState(): TimeStorage {
        return this
    }

    override fun loadState(state: TimeStorage) {
        XmlSerializerUtil.copyBean(state, this);
    }

    companion object {
        fun getInstance(project: Project): TimeStorage {
            return ServiceManager.getService(project, TimeStorage::class.java)
        }
    }

}