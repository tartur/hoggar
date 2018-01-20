package org.hoggar.ide.components.impl

import com.intellij.openapi.editor.EditorFactory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import org.hoggar.ide.components.MerlinServiceComponent
import org.hoggar.ide.components.MerlinServiceDocumentListener
import org.hoggar.merlin.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by sidharthkuruvila on 24/05/16.
 */

class MerlinServiceComponentImpl : MerlinServiceComponent {

    val merlin = Merlin.newInstance()
    val merlin3 = Merlin3.newInstance()
    val msdl = MerlinServiceDocumentListener(merlin)

    override fun getComponentName(): String {
        return "ocaml.merlinservice"
    }

    override fun disposeComponent() {
        EditorFactory.getInstance().eventMulticaster.removeDocumentListener(msdl)
    }

    override fun initComponent() {
        EditorFactory.getInstance().eventMulticaster.addDocumentListener(msdl)
    }

    override fun errors(file: PsiFile): List<MerlinError> {
        reloadFileIfModified(file)
        return merlin.errors(file.virtualFile.canonicalPath!!)
    }

    override fun completions(file: PsiFile, prefix: String, position: Position): List<CompletionEntry> {
        val dotMerlin = getDotMerlin(file)
        val completions = merlin3.complete(file.text, prefix, position, dotMerlin)
        val applicationEntries = if (completions.context != null) {
            completions.context.applicationContext.labels.map {
                CompletionEntry(name = it.name, desc = it.type, info = "", kind = "")
            }
        } else {
            Collections.emptyList<CompletionEntry>()
        }
        val allEntries = ArrayList<CompletionEntry>()
        allEntries.addAll(applicationEntries)
        allEntries.addAll(completions.entries)
        return allEntries
    }

    private fun getDotMerlin(file: PsiFile): File? {
        val dotMerlin = File(file.containingDirectory.virtualFile.canonicalPath, ".merlin")
        return if (dotMerlin.exists()) {
            dotMerlin
        } else {
            null
        }
    }

    override fun locate(file: PsiFile, position: Position): LocateResponse {
        reloadFileIfModified(file)
        return merlin.locate(file.virtualFile.canonicalPath!!, position)
    }

    private fun reloadFileIfModified(file: PsiFile) {
        val doc = PsiDocumentManager.getInstance(file.project).getCachedDocument(file)
        val filename = file.virtualFile.canonicalPath!!
        if (doc == null || doc.getUserData(MerlinServiceDocumentListener.DOCUMENT_CHANGED) != false) {
            merlin.tellSource(filename, file.text)
            doc?.putUserData(MerlinServiceDocumentListener.DOCUMENT_CHANGED, false)
        }
    }
}