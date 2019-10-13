package listeners

import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class CustomEditorMouseListener : EditorMouseListener {
    override fun mousePressed(editorMouseEvent: EditorMouseEvent) {
        val instance = FileDocumentManager.getInstance()
        val file = instance.getFile(editorMouseEvent.editor.document)
        val project = editorMouseEvent.editor.project
    }

    override fun mouseClicked(editorMouseEvent: EditorMouseEvent) {}

    override fun mouseReleased(editorMouseEvent: EditorMouseEvent) {}

    override fun mouseEntered(editorMouseEvent: EditorMouseEvent) {}

    override fun mouseExited(editorMouseEvent: EditorMouseEvent) {}
}
