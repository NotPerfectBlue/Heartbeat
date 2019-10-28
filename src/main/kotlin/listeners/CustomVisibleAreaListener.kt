package listeners

import com.intellij.openapi.editor.event.VisibleAreaEvent
import com.intellij.openapi.editor.event.VisibleAreaListener
import singletones.TickerStorage

class CustomVisibleAreaListener : VisibleAreaListener {
    override fun visibleAreaChanged(visibleAreaEvent: VisibleAreaEvent) {
        TickerStorage.needInrease = true
    }
}
