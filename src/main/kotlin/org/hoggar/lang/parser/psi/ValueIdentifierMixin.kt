package org.hoggar.lang.parser.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import org.hoggar.ide.reference.ValLongIdentReference

abstract class ValueIdentifierMixin(node: ASTNode) : ASTWrapperPsiElement(node), ValLongident {

    override fun getReference(): PsiReference? {
        return ValLongIdentReference(this)
    }
}