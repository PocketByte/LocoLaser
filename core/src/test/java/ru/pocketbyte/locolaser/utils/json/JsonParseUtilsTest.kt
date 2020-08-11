package ru.pocketbyte.locolaser.utils.json

import org.json.simple.JSONObject
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test
import ru.pocketbyte.locolaser.resource.formatting.*

class JsonParseUtilsTest {

    @Test
    fun testGetFormattingTypeByName() {
        var formattingType = JsonParseUtils.getFormattingType(
                JSONObject(), "formattingType"
        )
        assertNull(formattingType)

        formattingType = JsonParseUtils.getFormattingType(
                JSONObject().apply {
                    this["formattingType"] = "java"
                }, "formattingType"
        )
        assertSame(JavaFormattingType, formattingType)

        formattingType = JsonParseUtils.getFormattingType(
                JSONObject().apply {
                    this["formattingType"] = "web"
                }, "formattingType"
        )
        assertSame(WebFormattingType, formattingType)

        formattingType = JsonParseUtils.getFormattingType(
                JSONObject().apply {
                    this["formattingType"] = "no"
                }, "formattingType"
        )
        assertSame(NoFormattingType, formattingType)
    }

    @Test
    fun testGetFormattingTypeByClassName() {
        var formattingType = JsonParseUtils.getFormattingType(
                JSONObject().apply {
                    this["formattingType"] = JavaFormattingType::class.qualifiedName
                }, "formattingType"
        )
        assertSame(JavaFormattingType, formattingType)

        formattingType = JsonParseUtils.getFormattingType(
                JSONObject().apply {
                    this["formattingType"] = MixedFormattingType::class.qualifiedName
                }, "formattingType"
        )
        assertSame(MixedFormattingType, formattingType)

        formattingType = JsonParseUtils.getFormattingType(
                JSONObject().apply {
                    this["formattingType"] = CustomFormattingType::class.qualifiedName
                }, "formattingType"
        )
        assertSame(CustomFormattingType::class.java, formattingType?.javaClass)
    }

}