package components

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
        Storage(StoragePathMacros.CACHE_FILE)
    ])
class JiraSettings : PersistentStateComponent<Credentials> {

    private var credentials: Credentials? = null

    var key: String? = null
        get() {
            val reader = FileReader(keyFileName)
            val hostname = reader.readText()
            return field ?: hostname
        }

    fun writeHostname(hostname: String) {

        key = hostname

        val file = FileWriter(keyFileName)
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
        return CredentialAttributes(generateServiceName(subsystemName, key!!))
    }

    companion object {
        private const val keyFileName = "hostname.txt"
        private const val subsystemName = "TimeTracker"
    }

}