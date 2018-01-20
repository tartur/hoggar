package org.hoggar.ide.format

import org.hoggar.merlin.OpamCommand

class OcpIndent : OCamlExternalFormatter(OpamCommand("ocp-indent")) {
    companion object {
        val instance = OcpIndent()
    }
}