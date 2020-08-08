package ru.pocketbyte.locolaser.resource.formatting

import org.junit.Assert.*
import org.junit.Test
import ru.pocketbyte.locolaser.resource.entity.ResValue

class NoFormattingTypeTest {

    @Test
    fun testArgumentsFromValue() {
        assertNull(NoFormattingType.argumentsFromValue(""))
        assertNull(NoFormattingType.argumentsFromValue("%s"))
        assertNull(NoFormattingType.argumentsFromValue("%d"))
        assertNull(NoFormattingType.argumentsFromValue("{{count}}"))
    }

    @Test
    fun testConvert() {
        ResValue("AnyVaue", null, formattingType = NoFormattingType).let {
            assertSame(it, NoFormattingType.convert(it))
        }
        ResValue("AnyVaue", null, formattingType = JavaFormattingType).let {
            assertSame(it, NoFormattingType.convert(it))
        }

        ResValue("Str %s", null,
            formattingType = JavaFormattingType,
            formattingArguments = JavaFormattingType.argumentsFromValue("Str %s")
        ).let {
            assertSame(it, NoFormattingType.convert(it))
        }
    }

    @Test
    fun testConvertToJava() {
        ResValue("AnyVaue", null, formattingType = NoFormattingType).let {
            assertSame(it, NoFormattingType.convertToJava(it))
        }
        ResValue("AnyVaue", null, formattingType = JavaFormattingType).let {
            assertSame(it, NoFormattingType.convertToJava(it))
        }
        ResValue("AnyVaue", null, formattingType = WebFormattingType).let {
            val convertedValue = NoFormattingType.convertToJava(it)
            assertNotEquals(it, convertedValue)
            assertEquals(WebFormattingType.convertToJava(it), convertedValue)
        }
    }
}