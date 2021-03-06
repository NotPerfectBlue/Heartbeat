package listeners

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.vfs.VirtualFile
import singletones.TickerStorage

class CustomSaveListener : FileDocumentManagerListener {

    override fun beforeDocumentSaving(document: Document) {
        TickerStorage.needIncrease = true
    }

    override fun beforeAllDocumentsSaving() {}

    override fun beforeFileContentReload(file: VirtualFile, document: Document) {}

    override fun fileWithNoDocumentChanged(file: VirtualFile) {}

    override fun fileContentReloaded(file: VirtualFile, document: Document) {}

    override fun fileContentLoaded(file: VirtualFile, document: Document) {}

    override fun unsavedDocumentsDropped() {}
}