package components

import com.intellij.AppTopics
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import ext.extractJiraTaskName
import listeners.CustomDocumentListener
import listeners.CustomEditorMouseListener
import listeners.CustomSaveListener
import listeners.CustomVisibleAreaListener
import singletones.GitClient
import singletones.JiraClient
import ui.CredentialsDialog

class Heartbeat : StartupActivity {

    override fun runActivity(project: Project) {
        ApplicationManager.getApplication().invokeLater {

            println("adults/MOB-7059".extractJiraTaskName())
            println("MOB-7059".extractJiraTaskName())

            initJira(project)
            initListeners()

            initGit(project) {
                //TODO
            }

            initShutdownListener {
//                JiraClient.logTime()
            }
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

    private fun initShutdownListener(onShutdown: () -> Unit) {
        ApplicationManager
            .getApplication()
            .messageBus
            .connect()
            .subscribe(
                AppLifecycleListener.TOPIC,
                object: AppLifecycleListener {
                    override fun appWillBeClosed(isRestart: Boolean) {
                        if (!isRestart) {
                            onShutdown()
                        }
                    }
                }
            )
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
