package ru.pocketbyte.locolaser.resource.formatting

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.pocketbyte.locolaser.resource.entity.FormattingArgument

class FormattingArgumentFormattingTest {

    @Test
    fun testAnyName() {
        assertEquals(
            "hello",
            FormattingArgument("hello").anyName(1)
        )
        assertEquals(
            "world",
            FormattingArgument("world", 2, mapOf(
                Pair(JavaFormattingType.PARAM_TYPE_NAME, "s")
            )).anyName(45))

        assertEquals(
            "arg1",
            FormattingArgument(null).anyName(0)
        )
        assertEquals(
            "arg2",
            FormattingArgument(null).anyName(1)
        )
        assertEquals(
            "arg1",
            FormattingArgument(null, 1).anyName(2)
        )
        assertEquals(
            "s1",
            FormattingArgument(null, 1, mapOf(
                Pair(JavaFormattingType.PARAM_TYPE_NAME, "s")
            )).anyName(2)
        )
        assertEquals(
            "d12",
            FormattingArgument(parameters =  mapOf(
                Pair(JavaFormattingType.PARAM_TYPE_NAME, "d")
            )).anyName(11)
        )
    }

    @Test
    fun testParameterClass() {
        assertEquals(
            Int::class,
            FormattingArgument(parameters = mapOf(
                Pair(FormattingType.PARAM_CLASS, Int::class)
            )).parameterClass()
        )
        assertEquals(
            Float::class,
            FormattingArgument(parameters = mapOf(
                Pair(FormattingType.PARAM_CLASS, Float::class),
                Pair(JavaFormattingType.PARAM_TYPE_NAME, "s")
            )).parameterClass()
        )
        assertEquals(
            Thread::class,
            FormattingArgument(parameters = mapOf(
                Pair(FormattingType.PARAM_CLASS, Thread::class)
            )).parameterClass()
        )
        assertEquals(
            String::class,
            FormattingArgument("count", parameters = mapOf(
                Pair(JavaFormattingType.PARAM_TYPE_NAME, "s")
            )).parameterClass()
        )
        assertEquals(
            Long::class,
            FormattingArgument("name", parameters = mapOf(
                Pair(JavaFormattingType.PARAM_TYPE_NAME, "d")
            )).parameterClass()
        )
        assertEquals(
            Long::class,
            FormattingArgument("count").parameterClass()
        )
        assertEquals(
            String::class,
            FormattingArgument("name").parameterClass()
        )
        assertEquals(
            String::class,
            FormattingArgument("money").parameterClass()
        )
    }
}