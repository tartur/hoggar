package org.hoggar.ide.format

import com.intellij.openapi.diagnostic.Logger
import org.hoggar.ide.settings.ExternalFormatter
import org.hoggar.ide.settings.OCamlPluginConfig

class FormatterUtil {
    companion object {
        private val LOGGER = Logger.getInstance(FormatterUtil::class.java)
        private val config = OCamlPluginConfig.instance

        fun getConfiguredFormatter(): OCamlExternalFormatter? {
            return try {
                when (config.externalFormatter) {
                    ExternalFormatter.OCP_INDENT -> OcpIndent.instance
                    ExternalFormatter.OCAMLFORMAT -> OCamlFormat.instance
                }
            } catch (e: Throwable) {
                LOGGER.warn("Unable to used configured formatter ${config.externalFormatter}", e)
                null
            }
        }

        fun isFormattingEnabled(): Boolean {
            return config.externalFormatterEnabled
        }
    }
}