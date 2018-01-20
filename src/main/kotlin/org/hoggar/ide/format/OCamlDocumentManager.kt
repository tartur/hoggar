package org.hoggar.ide.format

import com.intellij.AppTopics
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ApplicationComponent


class OCamlDocumentManager : ApplicationComponent {
    override fun getComponentName(): String {
        return "OCamlDocumentManager"
    }

    override fun initComponent() {
        val bus = ApplicationManager.getApplication().messageBus

        val connection = bus.connect()
        val handler = OCamlReformatOnSave()
        connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC, handler)
    }
}