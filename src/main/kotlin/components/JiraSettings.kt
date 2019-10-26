package components

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import java.io.FileReader
import java.io.FileWriter

@State(
    name = "JiraSettings",
    storages = [
        Storage(StoragePathMacros.WORKSPACE_FILE)
    ])
class JiraSettings : PersistentStateComponent<Credentials> {

    private var credentials: Credentials? = null

    var key: String? = null
        get() {
            val reader = FileReader("hostname.txt")
            val hostname = reader.readText()
            return field ?: hostname
        }

    fun writeHostname(hostname: String) {

        key = hostname

        val file = FileWriter("hostname.txt")
        file.apply {
            flush()
            write(hostname)
            close()
        }
    }

    override fun getState(): Credentials? {
        if (credentials == null)  {
            val credentialAttributes = createCredentialAttributes()
            credentials = PasswordSafe.instance.get(credentialAttributes)
        }
        return credentials
    }

    override fun loadState(state: Credentials) {
        credentials = state
        val credentialAttributes = createCredentialAttributes()
        PasswordSafe.instance.set(credentialAttributes, credentials)
    }

    private fun createCredentialAttributes(): CredentialAttributes {
        return CredentialAttributes(generateServiceName("TimeTracker", key!!))
    }

}