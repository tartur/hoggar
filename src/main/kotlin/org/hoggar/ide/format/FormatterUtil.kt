package org.hoggar.ide.format

import org.hoggar.ide.settings.ExternalFormatter
import org.hoggar.ide.settings.OCamlPluginConfig

class FormatterUtil {
    companion object {
        private val config = OCamlPluginConfig.instance

        fun getConfiguredFormatter(): OCamlExternalFormatter {
            return when (config.externalFormatter) {
                ExternalFormatter.OCP_INDENT -> OcpIndent.instance
                ExternalFormatter.OCAMLFORMAT -> OCamlFormat.instance
            }
        }

        fun isFormattingEnabled(): Boolean {
            return config.externalFormatterEnabled
        }
    }
}