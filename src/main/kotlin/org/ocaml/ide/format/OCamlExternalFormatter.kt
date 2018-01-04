package org.ocaml.ide.format

import com.intellij.openapi.editor.Document
import com.intellij.util.io.delete
import org.ocaml.merlin.OpamCommand
import java.nio.charset.Charset
import java.nio.file.Files

abstract class OCamlExternalFormatter (val cmd: String){
    private val opamCommand = OpamCommand()

    fun runExternalFormatter(inputText: String): String {
        val tempPath = Files.createTempFile("ijfmt", ".ml")
        Files.write(tempPath, inputText.toByteArray(Charset.forName("UTF-8")))
        val absolutePath = tempPath.toAbsolutePath().toFile().absolutePath
        val ocamlformatProcess = opamCommand.processBuilder(cmd, "--inplace", absolutePath).start()
        ocamlformatProcess.waitFor()
        val formattedText = Files.readAllLines(tempPath).joinToString(separator = "\n")
        tempPath.delete()
        return formattedText
    }



    fun updateDocumentFormatting(document: Document?) {
        if (document != null && document.isWritable) {
            val inputText = document.text
            val formattedText = runExternalFormatter(inputText)
            if (!inputText.isEmpty() && !formattedText.isEmpty()) {
                document.setText(formattedText)
            }
        }
    }
}