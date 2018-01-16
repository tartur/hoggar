package org.ocaml.merlin

import com.google.common.collect.Lists
import com.google.common.io.ByteStreams

class OpamCommand(private val executable: String = "<NONE>") {
    companion object {
        val OCaml = OpamCommand("ocaml")
        val OCamlMerlin = OpamCommand("ocamlmerlin")
    }

    private var binFolder: String = binFolder()

    private fun binFolder(): String {
        val pb = ProcessBuilder("opam", "config", "list").start()
        pb.waitFor()
        val output = String(ByteStreams.toByteArray(pb.inputStream))
        return output.split("\n")
                .filter { it.startsWith("bin") }
                .map { it.split(Regex("\\s+"))[1] }
                .first()
    }

    fun start(vararg parameters: String): Process {
        val command = Lists.asList("$binFolder/$executable", parameters)
        return ProcessBuilder(command).redirectErrorStream(true).start()
    }

    private fun makeCommandString(vararg parameters: String): String {
        val userHome = System.getProperty("user.home")
        val cmd = """
            . $userHome/.opam/opam-init/init.zsh > /dev/null 2> /dev/null || true
            cd ~/Code/ocaml-test; ${parameters.joinToString(" ")}
            """
        return cmd
    }

    fun processBuilder(vararg  parameters: String): ProcessBuilder {
        val cmd = makeCommandString(*parameters)
        return ProcessBuilder("bash", "-c", cmd)
    }
}