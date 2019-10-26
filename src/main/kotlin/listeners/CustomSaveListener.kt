package listeners

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.vfs.VirtualFile
import org.joda.time.DateTime

class CustomSaveListener : FileDocumentManagerListener {

    override fun beforeDocumentSaving(document: Document) {
        val instance = FileDocumentManager.getInstance()
        val file = instance.getFile(document)
        println("onDocumentSaved ${file?.name} ${DateTime()}")
    }

    override fun beforeAllDocumentsSaving() {}

    override fun beforeFileContentReload(file: VirtualFile, document: Document) {}

    override fun fileWithNoDocumentChanged(file: VirtualFile) {}

    override fun fileContentReloaded(file: VirtualFile, document: Document) {}

    override fun fileContentLoaded(file: VirtualFile, document: Document) {}

    override fun unsavedDocumentsDropped() {}
}