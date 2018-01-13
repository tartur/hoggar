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

                        val ln = LineNumbering(parameters.originalFile.text)
                        val searchPrefix = findSuitablePrefix(parameters)
                        val matchPrefix = matchingPrefix(searchPrefix)
                        val rs = resultSet.withPrefixMatcher(PlainPrefixMatcher(matchPrefix))
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
        val file = parameters.originalFile
        val text = file.text
        val offset = Math.min(text.length, parameters.offset)
        return findEmacsOCamlAtom(text, offset - 1)
    }

    private fun matchingPrefix(searchPrefix: String): String {
        return if (searchPrefix.contains(".")) {
            searchPrefix.substring(searchPrefix.lastIndexOfAny(listOf(".")) + 1)
        } else {
            searchPrefix
        }
    }

    private fun findEmacsOCamlAtom(text: String, offset: Int): String {
        val firstResult = re.find(ReversedSubstringCharSequence(text, offset, 0))
        val resultToUse = if (text.length - 1 == offset) {
            firstResult
        } else {
            firstResult?.next()
        }

        val endIndex = resultToUse?.range?.last

        return if (endIndex != null) {
            text.substring(offset - endIndex, offset + 1)
        } else {
            ""
        }
    }
}