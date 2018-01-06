package org.ocaml.ide.format

import org.ocaml.ide.settings.ExternalFormatter
import org.ocaml.ide.settings.OCamlPluginConfig

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