package components

import actions.MenuDialog
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class TimeTracker : StartupActivity {

    override fun runActivity(project: Project) {
        ApplicationManager.getApplication().invokeLater {

            val apiKey = MenuDialog(project)
            if (MenuDialog.apiKey.isNullOrEmpty()) {
                apiKey.show()
            }
        }
    }
}
