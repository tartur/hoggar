package org.ocaml.ide.format

class OcpIndent : OCamlExternalFormatter("ocp-indent") {
    companion object {
        val instance = OcpIndent()
    }
}