package listeners

import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import singletones.TickerStorage

class CustomEditorMouseListener : EditorMouseListener {
    override fun mousePressed(editorMouseEvent: EditorMouseEvent) {
        TickerStorage.needIncrease = true
    }

    override fun mouseClicked(editorMouseEvent: EditorMouseEvent) {}

    override fun mouseReleased(editorMouseEvent: EditorMouseEvent) {}

    override fun mouseEntered(editorMouseEvent: EditorMouseEvent) {}

    override fun mouseExited(editorMouseEvent: EditorMouseEvent) {}
}
