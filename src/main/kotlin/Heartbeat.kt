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
import ui.CredentialsDialog

class Heartbeat : StartupActivity {

    override fun runActivity(project: Project) {

        invokeLater {

            initJira(project)

            TimeStorage.getInstance(project)
            GitService.getInstance(project)

            initListeners()

            initShutdownListener {
                //                JiraClient.logTime()
            }
        }
    }

    private fun initJira(project: Project) {

        val jiraSettings = JiraSettings.getInstance(project)

        if (jiraSettings.key == null) {
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
