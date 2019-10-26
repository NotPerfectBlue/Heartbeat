package components

import com.intellij.AppTopics
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import listeners.CustomDocumentListener
import listeners.CustomEditorMouseListener
import listeners.CustomSaveListener
import listeners.CustomVisibleAreaListener
import singletones.GitClient
import ui.CredentialsDialog

class Heartbeat : StartupActivity {

    override fun runActivity(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            initJira(project)
            initListeners()


            initGit(project, {

            })
        }
    }

    private fun initJira(project: Project) {
        val jiraSettings = ServiceManager.getService(project, JiraSettings::class.java)

        if (jiraSettings.key == null) {
            val apiKey = CredentialsDialog(project)
            apiKey.show()
        }
    }

    private fun initGit(project: Project, onCheckout: () -> Unit) {
        GitClient.init(project, onCheckout)
    }

    private fun initListeners() {
        ApplicationManager
            .getApplication()
            .messageBus
            .connect()
            .subscribe(
                AppTopics.FILE_DOCUMENT_SYNC,
                CustomSaveListener()
            )

        EditorFactory
            .getInstance()
            .eventMulticaster.apply {
                addDocumentListener(CustomDocumentListener())
                addEditorMouseListener(CustomEditorMouseListener())
                addVisibleAreaListener(CustomVisibleAreaListener())
            }
    }

}
