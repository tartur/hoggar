package org.ocaml.ide.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.ocaml.ide.components.MerlinServiceComponent
import org.ocaml.util.LineNumbering
import org.ocaml.util.ReversedSubstringCharSequence

class OcamlCompletionContributor : CompletionContributor() {

    private val re = Regex("[a-zA-Z0-9.'_]*[~?]?")
    private val merlinService = ApplicationManager.getApplication().getComponent(MerlinServiceComponent::class.java)

    override fun invokeAutoPopup(position: PsiElement, typeChar: Char): Boolean {
        return typeChar == '~'
    }

    init {

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(),
                object : CompletionProvider<CompletionParameters>() {
                    override fun addCompletions(parameters: CompletionParameters,
                                                context: ProcessingContext,
                                                resultSet: CompletionResultSet) {

                        resultSet.stopHere()
                        val ln = LineNumbering(parameters.originalFile.text)
                        val searchPrefix = findSuitablePrefix(parameters)
                        val matchPrefix = matchingPrefix(searchPrefix)
                        val rs = resultSet.withPrefixMatcher(matchPrefix)
                        merlinService.completions(parameters.originalFile, searchPrefix, ln.position(parameters.offset))
                                .map {
                                    LookupElementBuilder.create(it.name)
                                            .withTypeText(it.desc)
                                }
                                .forEach { rs.addElement(it) }
                    }
                })
    }


    private fun findSuitablePrefix(parameters: CompletionParameters): String {
        val end = getOriginalPosition(parameters)
        return findEmacsOCamlAtom(parameters.originalFile.text, end)
    }

    private fun matchingPrefix(searchPrefix: String): String {
        return if (searchPrefix.contains(".")) {
            searchPrefix.substring(searchPrefix.lastIndexOf(".") + 1)
        } else {
            searchPrefix
        }
    }

    private fun getOriginalPosition(parameters: CompletionParameters): Int {
        return if (parameters.originalPosition == null) {
            parameters.originalFile.textLength - 1
        } else {
            parameters.originalPosition!!.textOffset
        }
    }

    private fun findEmacsOCamlAtom(text: String, offset: Int): String {
        val endIndex = re.find(ReversedSubstringCharSequence(text, offset, 0))?.next()?.range?.last

        return if (endIndex != null) {
            text.substring(offset - endIndex, offset + 1)
        } else {
            ""
        }
    }
}