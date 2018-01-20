package org.hoggar.lang.parser

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import org.hoggar.lang.MlFileType
import org.hoggar.lang.OcamlLanguage

/**
 * Created by sidharthkuruvila on 07/05/16.
 */
class OcamlPsiFileRoot(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, OcamlLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return MlFileType.INSTANCE
    }
}