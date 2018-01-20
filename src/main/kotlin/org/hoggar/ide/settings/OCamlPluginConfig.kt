package org.hoggar.ide.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "OCamlPluginConfig", storages = arrayOf(Storage("OCamlPluginConfig.xml")))
class OCamlPluginConfig : PersistentStateComponent<OCamlPluginConfig> {
    var externalFormatter = ExternalFormatter.OCAMLFORMAT
    var externalFormatterEnabled = true

    override fun getState(): OCamlPluginConfig? {
        return this
    }

    override fun loadState(state: OCamlPluginConfig) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {

        val instance: OCamlPluginConfig by lazy(LazyThreadSafetyMode.PUBLICATION) { ServiceManager.getService(OCamlPluginConfig::class.java) }

    }
}
