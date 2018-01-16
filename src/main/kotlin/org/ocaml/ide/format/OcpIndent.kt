package org.ocaml.ide.format

import org.ocaml.merlin.OpamCommand

class OcpIndent : OCamlExternalFormatter(OpamCommand("ocp-indent")) {
    companion object {
        val instance = OcpIndent()
    }
}