package listeners

import com.intellij.openapi.editor.event.VisibleAreaEvent
import com.intellij.openapi.editor.event.VisibleAreaListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.joda.time.DateTime

class CustomVisibleAreaListener : VisibleAreaListener {
    override fun visibleAreaChanged(visibleAreaEvent: VisibleAreaEvent) {
        val instance = FileDocumentManager.getInstance()
        val file = instance.getFile(visibleAreaEvent.editor.document)
        val project = visibleAreaEvent.editor.project
        println("onVisibleAreaChanged ${visibleAreaEvent.newRectangle} ${DateTime()}")
    }
}
