package ru.pocketbyte.locolaser.resource.formatting

import org.junit.Assert
import org.junit.Test
import ru.pocketbyte.locolaser.resource.entity.ResValue

class MixedFormattingTypeTest {

    @Test(expected = UnsupportedOperationException::class)
    fun testArgumentsFromValue() {
        MixedFormattingType.argumentsFromValue("Hello World")
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testConvert() {
        MixedFormattingType.convert(ResValue("AnyVaue", null, formattingType = MixedFormattingType))
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testConvertToJava() {
        MixedFormattingType.convert(ResValue("AnyVaue", null, formattingType = MixedFormattingType))
    }
}