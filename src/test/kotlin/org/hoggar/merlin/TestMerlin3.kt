package org.hoggar.merlin

import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

class TestMerlin3 {
    private val text = """
                    let () f =
                    let xs = [1;3;4] in
                    List.iter (fun x -> print_endline (string_of_int x)) xs
                    """

    @Test
    fun version() {
        val merlin = Merlin3.newInstance()
        assertTrue(merlin.version().startsWith("The Merlin toolkit version"))
    }

    @Test
    fun testDocument() {
        val merlin = Merlin3.newInstance()
        val doc = merlin.document(text, Position(4, 27))
        assertTrue(doc.value.contains("List.iter"))
    }

    @Test
    fun testComplete() {
        val merlin = Merlin3.newInstance()
        val completions = merlin.complete(text, "List.it", Position(4, 27), null)
        Assert.assertEquals(3, completions.entries.size)
    }
}