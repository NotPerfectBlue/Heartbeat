
package listeners

import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import singletones.TickerStorage

class CustomDocumentListener : DocumentListener {
    override fun beforeDocumentChange(documentEvent: DocumentEvent) {}

    override fun documentChanged(documentEvent: DocumentEvent) {
        TickerStorage.needInrease = true
    }
}