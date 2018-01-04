package org.ocaml.ide.format

class OCamlFormat : OCamlExternalFormatter("ocamlformat") {
    companion object {
        val instance = OCamlFormat()
    }
}