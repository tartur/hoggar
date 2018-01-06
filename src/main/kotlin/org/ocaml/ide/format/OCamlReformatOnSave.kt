package org.ocaml.ide.format

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter
import org.ocaml.lang.MlFileType

class OCamlReformatOnSave : FileDocumentManagerAdapter() {
    override fun beforeDocumentSaving(document: Document) {
        val formatter = FormatterUtil.getConfiguredFormatter()
        val fileDocumentManager = FileDocumentManager.getInstance()
        val file = fileDocumentManager.getFile(document)
        if (FormatterUtil.isFormattingEnabled() && file != null && file.fileType is MlFileType) {
            formatter.updateDocumentFormatting(document)
        }
    }
}