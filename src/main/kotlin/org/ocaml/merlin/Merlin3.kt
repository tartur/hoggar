package org.ocaml.merlin

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.EmptyAction
import com.intellij.ui.CloseNotificationAction
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import java.util.concurrent.TimeUnit

class Merlin3(private val objectMapper: ObjectMapper) {
    companion object {
        fun newInstance(): Merlin3 {
            val om = ObjectMapper()
            om.registerModule(KotlinModule())
            return Merlin3(om)
        }
    }


    private val merlinBin = "/home/beajeanm/.opam/4.05.0/bin/ocamlmerlin"
    private val opamCommand = OpamCommand()

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
        args.add(merlinBin)
        args.add("single")
        args.add(cmd)
        if (dotMerlin != null) {
            args.add("-dot-merlin")
            args.add(dotMerlin.absolutePath)
        }
        args.addAll(command)

        val processBuilder = opamCommand.processBuilder(*(args.toTypedArray()))
        processBuilder.redirectErrorStream(true)
        val merlinProcess = processBuilder.start()

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