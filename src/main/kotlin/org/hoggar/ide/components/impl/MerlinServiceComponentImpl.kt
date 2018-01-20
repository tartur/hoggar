package org.hoggar.ide.components.impl

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.EditorFactory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import org.hoggar.ide.components.MerlinServiceComponent
import org.hoggar.ide.components.MerlinServiceDocumentListener
import org.hoggar.merlin.*
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by sidharthkuruvila on 24/05/16.
 */

class MerlinServiceComponentImpl : MerlinServiceComponent {
    private val LOGGER = Logger.getInstance(MerlinServiceComponentImpl::class.java)

    private var merlin: Merlin? = null
    private var merlin3: Merlin3? = null
    private var msdl: MerlinServiceDocumentListener? = null

    init {
        try {
            merlin = Merlin.newInstance()
            merlin3 = Merlin3.newInstance()
            msdl = MerlinServiceDocumentListener(merlin!!)
        } catch (e: Exception) {
            LOGGER.error(e)
        }
    }

    override fun getComponentName(): String {
        return "ocaml.merlinservice"
    }

    override fun disposeComponent() {
        if (msdl != null) {
            EditorFactory.getInstance().eventMulticaster.removeDocumentListener(msdl!!)
        }
    }

    override fun initComponent() {
        if (msdl != null) {
            EditorFactory.getInstance().eventMulticaster.addDocumentListener(msdl!!)
        }
    }

    override fun errors(file: PsiFile): List<MerlinError> {
        return if (merlin != null) {
            reloadFileIfModified(file)
            merlin!!.errors(file.virtualFile.canonicalPath!!)
        } else {
            emptyList()
        }
    }

    override fun completions(file: PsiFile, prefix: String, position: Position): List<CompletionEntry> {
        return if (merlin3 != null) {
            val dotMerlin = getDotMerlin(file)
            val completions = merlin3!!.complete(file.text, prefix, position, dotMerlin)
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
            allEntries
        } else {
            emptyList()
        }
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
        return if (merlin != null) {
            reloadFileIfModified(file)
            merlin!!.locate(file.virtualFile.canonicalPath!!, position)
        } else {
            LocateFailed("Merlin not found")
        }
    }

    private fun reloadFileIfModified(file: PsiFile) {
        val doc = PsiDocumentManager.getInstance(file.project).getCachedDocument(file)
        val filename = file.virtualFile.canonicalPath!!
        if (merlin != null && (doc == null || doc.getUserData(MerlinServiceDocumentListener.DOCUMENT_CHANGED) != false)) {
            merlin!!.tellSource(filename, file.text)
            doc?.putUserData(MerlinServiceDocumentListener.DOCUMENT_CHANGED, false)
        }
    }
}