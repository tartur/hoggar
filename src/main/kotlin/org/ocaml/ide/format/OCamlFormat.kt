package org.ocaml.ide.format

import org.ocaml.merlin.OpamCommand

class OCamlFormat : OCamlExternalFormatter(OpamCommand("ocamlformat")) {
    companion object {
        val instance = OCamlFormat()
    }
}