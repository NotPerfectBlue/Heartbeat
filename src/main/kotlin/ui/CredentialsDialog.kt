package ui

import com.intellij.credentialStore.Credentials
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import components.JiraSettings
import singletones.JiraClient
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPasswordField
import javax.swing.JTextField

class CredentialsDialog(project: Project?) : DialogWrapper(project, true) {

    init {
        init()
    }

    lateinit var hostname: JTextField
    lateinit var username: JTextField
    lateinit var password: JPasswordField

    override fun createCenterPanel(): JComponent? {

        title = "Jira credentials"
        setOKButtonText("Save")

        return panel {
            row {
                JLabel("Jira hostname")()
                hostname = JTextField("https://")
                hostname()
            }
            row {
                JLabel("Username:")()
                username = JTextField()
                username()
            }
            row {
                JLabel("Password:")()
                password = JPasswordField()
                password()
            }
        }
    }

    private fun validateData(): Boolean {
        return try {
            JiraClient.init(
                hostname.text,
                username.text,
                password.text
            )
            true
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }

    public override fun doOKAction() {

        if (validateData()) {

            setErrorText("")

            val jiraSettings = ServiceManager.getService(JiraSettings::class.java)

            jiraSettings.apply {
                writeHostname(hostname.text)
                loadState(
                    Credentials(
                        username.text,
                        password.text
                    )
                )
            }
            super.doOKAction()
        } else {
            setErrorText("Incorrect credentials!")
        }
    }

}
