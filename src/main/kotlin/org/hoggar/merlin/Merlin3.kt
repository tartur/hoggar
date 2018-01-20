package org.hoggar.merlin

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class Merlin3(private val objectMapper: ObjectMapper) {
    companion object {
        fun newInstance(): Merlin3 {
            val om = ObjectMapper()
            om.registerModule(KotlinModule())
            return Merlin3(om)
        }
    }

    private val ocamlMerlin by lazy { OpamCommand.ocamlMerlin() }

    fun document(text: String, position: Position): Doc {
        val output = runCommand("document", text, null, listOf("-position", "${position.line}:${position.col}"))
        val response = extractResponse(objectMapper.readTree(output))
        return Doc(objectMapper.convertValue(response, String::class.java))
    }

    fun version(): String {
        return runCommand("-version", null, null, Collections.emptyList())!!
    }

    fun complete(text: String, prefix: String, position: Position, dotMerlin: File?): Completions {
        val output = runCommand("complete-prefix", text, dotMerlin, listOf("-position", "${position.line}:${position.col}", "-types", "true", "-prefix", prefix))
        val response = extractResponse(objectMapper.readTree(output))
        return objectMapper.convertValue(response, Completions::class.java)
    }

    private fun runCommand(cmd: String, text: String?, dotMerlin: File?, command: List<String>): String? {
        val args = ArrayList<String>()
        args.add("single")
        args.add(cmd)
        if (dotMerlin != null) {
            args.add("-dot-merlin")
            args.add(dotMerlin.absolutePath)
        }
        args.addAll(command)

        val merlinProcess = ocamlMerlin.start(*(args.toTypedArray()))
        val writer = OutputStreamWriter(merlinProcess.outputStream)
        val reader = BufferedReader(InputStreamReader(merlinProcess.inputStream))

        if (text != null) {
            writer.write(text)
        }
        writer.close()

        return reader.readLine()
    }

    private fun extractResponse(t: JsonNode): JsonNode {
        val responseType = t.get("class").textValue()
        val value = t.get("value")
        when (responseType) {
            "return" -> return value
            else -> {
                throw RuntimeException(value.textValue())
            }
        }
    }
}

data class Doc(val value: String)