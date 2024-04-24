package ru.pocketbyte.locolaser.provider

import kotlin.test.Test
import kotlin.test.assertEquals

class StringWithFormatTest {

    @Test
    fun test() {
        assertEquals(
            "Say: \"Hello\"",
            "Say: \"%@\"".stringWithFormat("Hello")
        )

        assertEquals(
            "Say: \"Hello World\"",
            "Say: \"%@ %@\"".stringWithFormat("Hello", "World")
        )

        var array = arrayOf("Hello", "World")
        assertEquals(
            "Say: \"Hello World\"",
            "Say: \"%@ %@\"".stringWithFormat(*array)
        )

        array = arrayOf("Hello", "Again")

        assertEquals(
            "Say: \"Hello\"",
            getString("Say: \"%@\"", "Hello")
        )
        assertEquals(
            "Say: \"Hello Again\"",
            getString("Say: \"%@ %@\"", *array)
        )
    }

    private fun getString(key: String, vararg args: Any): String {
        return key.stringWithFormat(*args)
    }
}