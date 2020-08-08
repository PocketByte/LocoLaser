package ru.pocketbyte.locolaser.resource.entity

import org.junit.Assert.assertEquals
import org.junit.Test

class FormattingArgumentTest {

    @Test
    fun testMerge() {
        val arg1 = FormattingArgument("Arg1", 1, mapOf(Pair("P1", "false"), Pair("P2", "Hello")))
        val arg2 = FormattingArgument("Arg2", 2, mapOf(Pair("P1", "true")))

        assertEquals(arg1, arg2.merge(arg1))
        assertEquals(arg1, null.merge(arg1))
        assertEquals(arg2, arg2.merge(null))

        assertEquals(
            FormattingArgument("Arg2", 2, mapOf(Pair("P1", "true"), Pair("P2", "Hello"))),
            arg1.merge(arg2)
        )

        assertEquals(
            FormattingArgument("Arg1", 3, mapOf(Pair("P1", "false"), Pair("P2", "Hello"))),
            arg1.merge(
                FormattingArgument(null, 3, mapOf())
            )
        )

        assertEquals(
            FormattingArgument("Arg3", 1, mapOf(Pair("P1", "false"), Pair("P2", "Hello"), Pair("P3", "val"))),
            arg1.merge(
                FormattingArgument("Arg3", null, mapOf(Pair("P3", "val")))
            )
        )
    }
}