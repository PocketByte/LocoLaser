package ru.pocketbyte.locolaser.resource.formatting

import org.junit.Assert.*
import org.junit.Test
import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue
import java.lang.UnsupportedOperationException

class JavaFormattingTypeTest {

    @Test
    fun testArgumentsFromValue() {
        assertEquals(
            listOf(FormattingArgument(null, null, parameters("s", ""))),
            JavaFormattingType.argumentsFromValue("%s")
        )

        // Read index
        assertEquals(
            listOf(FormattingArgument(null, 21, parameters("s", ""))),
            JavaFormattingType.argumentsFromValue("%21\$s")
        )

        // Read type parameters
        assertEquals(
            listOf(FormattingArgument(null, null, parameters("d", "+20"))),
            JavaFormattingType.argumentsFromValue("%+20d")
        )

        // Read complex string
        assertEquals(
            listOf(
                FormattingArgument(null, 2, parameters("s", "")),
                FormattingArgument(null, 1, parameters("d", "("))
            ),
            JavaFormattingType.argumentsFromValue("Hello, %2\$s. Your number is %1\$(d")
        )
    }

    @Test
    fun testConvert() {
        // Convert from java to java
        val value1 = ResValue("value1", null, formattingType = JavaFormattingType)
        assertSame(value1, JavaFormattingType.convert(value1))

        // Convert from another type
        val value2 = ResValue("value2", null, formattingType = JavaFormattingType)
        val anotherFormattingType = FormattingTypeMock(value2)

        assertEquals(0, anotherFormattingType.convertToJavaCalls)
        assertSame(value2, JavaFormattingType.convert(
            ResValue("value2 another type", null, formattingType = anotherFormattingType)
        ))
        assertEquals(1, anotherFormattingType.convertToJavaCalls)
    }

    @Test
    fun testConvertFromNoFormatting() {
        val value = ResValue("value1", null, formattingType = NoFormattingType)
        assertSame(value, JavaFormattingType.convert(value))
    }

    @Test
    fun testArgumentToString() {
        assertNull(JavaFormattingType.argumentToString(FormattingArgument()))
        assertNull(JavaFormattingType.argumentToString(FormattingArgument("some_name")))
        assertNull(JavaFormattingType.argumentToString(FormattingArgument("some_name", 1)))
        assertNull(JavaFormattingType.argumentToString(FormattingArgument("some_name", 1, emptyMap())))
        assertNull(JavaFormattingType.argumentToString(FormattingArgument("some_name", 1,
                parameters("", "2"))))

        assertEquals("%s", JavaFormattingType.argumentToString(FormattingArgument(
                parameters = parameters("s", "")
        )))

        assertEquals("%1\$d", JavaFormattingType.argumentToString(FormattingArgument(
                index = 1,
                parameters = parameters("d", "")
        )))

        assertEquals("%0.2f", JavaFormattingType.argumentToString(FormattingArgument(
                parameters = parameters("f", "0.2")
        )))

        assertEquals("%5\$-.4f", JavaFormattingType.argumentToString(FormattingArgument(
                index = 5,
                parameters = parameters("f", "-.4")
        )))
    }

    private fun parameters(typeName: String, typeParameters: String): Map<String, String> {
        return mapOf(
            Pair(JavaFormattingType.PARAM_TYPE_NAME, typeName),
            Pair(JavaFormattingType.PARAM_TYPE_PARAMETERS, typeParameters)
        )
    }

    private class FormattingTypeMock(
            val javaValue: ResValue
    ): FormattingType {
        var convertToJavaCalls = 0
            private set

        override fun argumentsFromValue(value: String): List<FormattingArgument> {
            throw UnsupportedOperationException()
        }

        override fun convert(value: ResValue): ResValue {
            throw UnsupportedOperationException()
        }

        override fun convertToJava(value: ResValue): ResValue {
            convertToJavaCalls += 1
            return javaValue
        }

    }

}