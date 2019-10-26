package components

import actions.CredentialsDialog
import com.intellij.AppTopics
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import listeners.CustomDocumentListener
import listeners.CustomEditorMouseListener
import listeners.CustomSaveListener
import listeners.CustomVisibleAreaListener

class TimeTracker : StartupActivity {

    override fun runActivity(project: Project) {
        ApplicationManager.getApplication().invokeLater {

            val apiKey = CredentialsDialog(project)
            apiKey.show()
        }
//        ApplicationManager.getApplication().invokeLater {
//            // save file
//            val bus = ApplicationManager.getApplication().messageBus
//            val connection = bus.connect()
//            connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC, CustomSaveListener())
//
//            // edit document
//            EditorFactory.getInstance().eventMulticaster.addDocumentListener(CustomDocumentListener())
//
//            // mouse press
//            EditorFactory.getInstance().eventMulticaster.addEditorMouseListener(CustomEditorMouseListener())
//
//            // scroll document
//            EditorFactory.getInstance().eventMulticaster.addVisibleAreaListener(CustomVisibleAreaListener())
//        }
    }
}
