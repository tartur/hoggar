package org.hoggar.ide.format

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import org.hoggar.lang.MlFileType
import com.intellij.psi.PsiDocumentManager


class OCamlFormatAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent?) {
        val formatter = FormatterUtil.getConfiguredFormatter()
        if (formatter != null && e != null && FormatterUtil.isFormattingEnabled()) {
            val psiFile = e.getData(CommonDataKeys.PSI_FILE)
            val project = e.project
            if (project != null && psiFile != null && psiFile.fileType is MlFileType) {
                WriteCommandAction.runWriteCommandAction(project, {
                    val document = PsiDocumentManager.getInstance(project).getDocument(psiFile)
                    formatter.updateDocumentFormatting(document)
                })
            }
        }
    }

}