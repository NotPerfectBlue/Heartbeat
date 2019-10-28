
package listeners

import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import org.joda.time.DateTime

class CustomDocumentListener : DocumentListener {
    override fun beforeDocumentChange(documentEvent: DocumentEvent) {}

    override fun documentChanged(documentEvent: DocumentEvent) {
        val document = documentEvent.document
        val instance = FileDocumentManager.getInstance()
        val file = instance.getFile(document)
        println("onDocumentChanged ${file?.name} ${DateTime()}")
    }
}