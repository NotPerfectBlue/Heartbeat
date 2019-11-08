import com.intellij.AppTopics
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import components.GitService
import components.JiraSettings
import components.TimeStorage
import ext.*
import listeners.CustomDocumentListener
import listeners.CustomEditorMouseListener
import listeners.CustomSaveListener
import listeners.CustomVisibleAreaListener
import org.joda.time.DateTime
import singletones.JiraClient
import singletones.TickerStorage
import ui.CredentialsDialog

class Heartbeat : StartupActivity {

    override fun runActivity(project: Project) {

        invokeLater {

            initJira(project)

            initListeners()

            TimeStorage.getInstance(project)
            GitService.getInstance(project)

            TickerStorage.increaseTime = { taskName ->
                val timeStorage = TimeStorage.getInstance(project)
                timeStorage.increase(taskName)
            }

            initShutdownListener {

                val timeStorage = TimeStorage.getInstance(project)

                for (entry in timeStorage) {
//                    JiraClient.logTime(entry.key, DateTime.now(), entry.value)
                    println("${entry.key}, ${entry.value}")
                }

                timeStorage.clear()
            }
        }
    }

    private fun initJira(project: Project) {

        val jiraSettings = JiraSettings.getInstance(project)

        if (jiraSettings.key.isNullOrEmpty()) {
            val apiKey = CredentialsDialog(project)
            apiKey.show()
        }
    }

    private fun initShutdownListener(onShutdown: () -> Unit) {
        subscribeToAppTopic(AppLifecycleListener.TOPIC) {
            object: AppLifecycleListener {
                override fun appWillBeClosed(isRestart: Boolean) {
                    if (!isRestart) {
                        onShutdown()
                    }
                }
            }
        }
    }

    private fun initListeners() {
        subscribeToAppTopic(AppTopics.FILE_DOCUMENT_SYNC) {
            CustomSaveListener()
        }

        EditorFactory
            .getInstance()
            .eventMulticaster
            .apply {
                addDocumentListener(CustomDocumentListener())
                addEditorMouseListener(CustomEditorMouseListener())
                addVisibleAreaListener(CustomVisibleAreaListener())
            }
    }

}
