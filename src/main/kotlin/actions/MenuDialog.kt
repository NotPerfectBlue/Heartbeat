package actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.jbTextField
import com.intellij.ui.layout.panel
import config.ConfigFile

import javax.swing.*
import java.util.UUID

class MenuDialog(project: Project?) : DialogWrapper(project, true) {

    init {
        init()
    }

    private lateinit var input: JTextField

    override fun createCenterPanel(): JComponent? {

        input = JTextField(36).apply {
            text = MenuDialog.apiKey
        }

        title = "Credentials"
        setOKButtonText("Save")

        return panel(LCFlags.flowY) {
            titledRow("WakaTime API key") {
                cell(true) {
                    input()
                }
            }
            titledRow("Jira credentials") {
                cell(true) {
                    JTextField()()
                    JTextField()()
                }
            }
        }
    }

    override fun doValidate(): ValidationInfo? {
        val apiKey = input.text
        try {
            UUID.fromString(apiKey)
        } catch (e: Exception) {
            return ValidationInfo("Invalid api key.")
        }

        return null
    }

    public override fun doOKAction() {
        MenuDialog.apiKey = input.text
        super.doOKAction()
    }

    companion object {
        private var _api_key = ""

        var apiKey: String
            get() {
                if (_api_key != "") {
                    return _api_key
                }

                var apiKey = ConfigFile.get("settings", "api_key")
                if (apiKey == null) apiKey = ""

                _api_key = apiKey
                return apiKey
            }
            set(apiKey) {
                ConfigFile.set("settings", "api_key", apiKey)
                _api_key = apiKey
            }
    }

}
