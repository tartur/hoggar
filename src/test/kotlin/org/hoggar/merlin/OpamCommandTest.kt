package org.hoggar.merlin

import com.google.common.io.ByteStreams
import org.fest.assertions.Assertions.assertThat
import org.junit.Test

class OpamCommandTest {

    @Test
    fun extractOpamEnvironmentVariables() {
        val process = OpamCommand.OCaml.start("-version")
        process.waitFor()

        val result = String(ByteStreams.toByteArray(process.inputStream))

        assertThat(result).contains("OCaml").contains("version")
    }
}