package org.hoggar.ide.components

import com.intellij.openapi.components.ApplicationComponent
import com.intellij.psi.PsiFile
import org.hoggar.merlin.CompletionEntry
import org.hoggar.merlin.LocateResponse
import org.hoggar.merlin.MerlinError
import org.hoggar.merlin.Position

/**
 * Created by sidharthkuruvila on 26/05/16.
 */
interface MerlinServiceComponent : ApplicationComponent {
    fun errors(file: PsiFile): List<MerlinError>
    fun completions(file: PsiFile, prefix: String, position: Position): List<CompletionEntry>
    fun locate(file: PsiFile, position: Position): LocateResponse
}