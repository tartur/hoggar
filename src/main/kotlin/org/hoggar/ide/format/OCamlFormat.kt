package org.hoggar.ide.format

import org.hoggar.merlin.OpamCommand

class OCamlFormat : OCamlExternalFormatter(OpamCommand("ocamlformat")) {
    companion object {
        val instance = OCamlFormat()
    }
}