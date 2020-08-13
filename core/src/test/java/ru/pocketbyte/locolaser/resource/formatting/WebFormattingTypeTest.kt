package ru.pocketbyte.locolaser.resource.formatting

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue
import ru.pocketbyte.locolaser.resource.entity.merge

class WebFormattingTypeTest {

    @Test
    fun testArgumentsFromValue() {
        assertEquals(
            listOf(FormattingArgument("name", null, parameters(""))),
            WebFormattingType.argumentsFromValue("{{name}}")
        )

        // Read type format
        assertEquals(
            listOf(FormattingArgument("date", null, parameters("dd/mm/yyyy"))),
            WebFormattingType.argumentsFromValue("{{date,dd/mm/yyyy}}")
        )

        // Read complex string
        assertEquals(
            listOf(
                FormattingArgument("name", null, parameters("")),
                FormattingArgument("date", null, parameters("dd/mm/yyyy"))
            ),
            WebFormattingType.argumentsFromValue("Hello, {{name}}. Your date is {{date,dd/mm/yyyy}}")
        )
    }

    @Test
    fun testConvertFromSameType() {
        val value1 = ResValue("value1", null, formattingType = WebFormattingType)
        assertSame(value1, WebFormattingType.convert(value1))
    }

    @Test
    fun testConvertFromNoFormatting() {
        val value = ResValue("value1", null, formattingType = NoFormattingType)
        assertSame(value, WebFormattingType.convert(value))
    }

    @Test
    fun testConvertFromJava_NameCreation() {
        val string = "The string is '%s'"
        val value = ResValue(
            string, null,
            formattingType = JavaFormattingType,
            formattingArguments = JavaFormattingType.argumentsFromValue(string)
        )
        val expectedValue = ResValue(
            "The string is '{{s1}}'", null,
            formattingType = WebFormattingType,
            formattingArguments = JavaFormattingType.argumentsFromValue(string)?.map {
                FormattingArgument("s1", it.index, it.parameters)
            }
        )
        assertEquals(expectedValue, WebFormattingType.convert(value))
    }

    @Test
    fun testConvertFromJava_NameUtilize() {
        val string = "The string is '%s'"
        val value = ResValue(
            string, null,
            formattingType = JavaFormattingType,
            formattingArguments = JavaFormattingType.argumentsFromValue(string)?.map {
                FormattingArgument("count", it.index, it.parameters)
            }
        )
        val expectedValue = ResValue(
            "The string is '{{count}}'", null,
            formattingType = WebFormattingType,
            formattingArguments = value.formattingArguments
        )
        assertEquals(expectedValue, WebFormattingType.convert(value))
    }

    @Test
    fun testConvertToJava_JavaParamsUtilize() {
        val javaString = "String %1\$s, count %d, price %2\$.2f"
        val javaArguments = JavaFormattingType.argumentsFromValue(javaString)

        val webString = "String {{some_string}}, count {{count}}, price {{price}}"
        val webValue = ResValue(
            webString, null,
            formattingType = WebFormattingType,
            formattingArguments =
            WebFormattingType.argumentsFromValue(webString)?.mapIndexed { index, it ->
                it.merge(javaArguments?.getOrNull(index))
            }?.filterNotNull() ?: emptyList()
        )
        val expectedValue = ResValue(
            javaString, null,
            formattingType = JavaFormattingType,
            formattingArguments = webValue.formattingArguments
        )
        assertEquals(expectedValue, WebFormattingType.convertToJava(webValue))
    }

    @Test
    fun testConvertToJava_NoJavaParams() {
        val webString = "String {{some_string}}, count {{count}}, price {{price}}"
        val webValue = ResValue(
            webString, null,
            formattingType = WebFormattingType,
            formattingArguments = WebFormattingType.argumentsFromValue(webString)
        )
        val expectedValue = ResValue(
            "String %s, count %s, price %s", null,
            formattingType = JavaFormattingType,
            formattingArguments = webValue.formattingArguments?.map {
                FormattingArgument(
                    it.name, it.index,
                    it.parameters?.toMutableMap()?.apply {
                        put(JavaFormattingType.PARAM_TYPE_NAME, "s")
                    }
                )
            }
        )
        assertEquals(expectedValue, WebFormattingType.convertToJava(webValue))
    }

    @Test
    fun testConvertToJava_NoFormattedType() {
        val string = "String without formatting"
        val value = ResValue(
            string, null,
            formattingType = NoFormattingType,
            formattingArguments = null
        )
        assertSame(value, WebFormattingType.convertToJava(value))
    }

    @Test
    fun testConvertToJava_NoFormattedValue() {
        val webString = "String without formatting"
        val webValue = ResValue(
            webString, null,
            formattingType = WebFormattingType,
            formattingArguments = WebFormattingType.argumentsFromValue(webString)
        )
        val expectedValue = ResValue(
            webValue.value, webValue.comment,
            formattingType = JavaFormattingType,
            formattingArguments = webValue.formattingArguments
        )
        assertEquals(expectedValue, WebFormattingType.convertToJava(webValue))
    }

    private fun parameters(typeFormat: String): Map<String, String> {
        return mapOf(
            Pair(WebFormattingType.PARAM_TYPE_FORMAT, typeFormat)
        )
    }
}