/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.resources

import org.junit.Assert.*
import org.junit.Test
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.resource.BaseTableResources
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.testutils.mock.MockDataSet
import java.io.File

/**
 * @author Denis Shurygin
 */
class BaseTableResourcesTest {


    @Test
    fun testParseMetaData() {
        assertEquals(mapOf(Pair("format", "123")), BaseTableResources.parseMeta("format=123"))
        assertEquals(mapOf(Pair("data", "@$%")), BaseTableResources.parseMeta("data=@$%"))
        assertEquals(mapOf(Pair("meta_1", "some value"), Pair("meta_2", "another value")), BaseTableResources.parseMeta("meta_1=some value;meta_2=another value"))

        // Ignore wrong parts
        assertEquals(mapOf(Pair("CDATA", "true")), BaseTableResources.parseMeta("CDATA=true;meta_data_2"))
        assertEquals(mapOf(Pair("CDATA", "false")), BaseTableResources.parseMeta("wrong;CDATA=false;data"))
        assertNull(BaseTableResources.parseMeta("CDATA~false"))
        assertNull(BaseTableResources.parseMeta("CDATA;"))
        assertNull(BaseTableResources.parseMeta(""))
    }

    @Test
    @Throws(Exception::class)
    fun testReadNoQuantity() {
        val dataSet = MockDataSet(arrayOf("en", "ru"))
        dataSet.add("key1", null, null, arrayOf("value1_1", "value1_2"), null)
        dataSet.add("key2", null, "Some comment", arrayOf("value2_1", "value2_2"), null)

        val sourceConfig = BaseTableResourcesConfigImpl(dataSet, keyColumn = "key")

        val source = sourceConfig.resources
        val result = source.read(setOf("en", "ru"), null)
        assertNotNull(result)

        assertEquals(3, result!!.size) // 2 locales + base locale

        // =============
        // Locale EN
        val resLocaleEn = result["en"]!!
        assertNotNull(resLocaleEn)

        val resItem1_1 = resLocaleEn["key1"]
        assertNotNull(resItem1_1)
        val resValue1_1 = resItem1_1?.valueForQuantity(Quantity.OTHER)
        assertEquals("value1_1", resValue1_1!!.value)
        assertNull(resValue1_1.comment)

        val resItem2_1 = resLocaleEn["key2"]
        assertNotNull(resItem2_1)
        val resValue2_1 = resItem2_1?.valueForQuantity(Quantity.OTHER)
        assertEquals("value2_1", resValue2_1!!.value)
        assertEquals("Some comment", resValue2_1.comment)

        // =============
        // Locale RU
        val resLocaleRu = result["ru"]!!
        assertNotNull(resLocaleRu)

        val resItem1_2 = resLocaleRu["key1"]
        assertNotNull(resItem1_2)
        val resValue1_2 = resItem1_2?.valueForQuantity(Quantity.OTHER)
        assertEquals("value1_2", resValue1_2!!.value)
        assertNull(resValue1_2.comment)

        val resItem2_2 = resLocaleRu["key2"]
        assertNotNull(resItem2_2)
        val resValue2_2 = resItem2_2?.valueForQuantity(Quantity.OTHER)
        assertEquals("value2_2", resValue2_2!!.value)
        assertEquals("Some comment", resValue2_2.comment)
    }

    @Test
    @Throws(Exception::class)
    fun testReadQuantity() {
        val dataSet = MockDataSet(arrayOf("ru"))
        dataSet.add("key1", null, null, arrayOf("value1"), null)
        dataSet.add("key1", Quantity.ONE.toString(), "Some comment", arrayOf("value2"), null)
        dataSet.add("key2", Quantity.FEW.toString(), "Some comment", arrayOf("value"), null)

        val sourceConfig = BaseTableResourcesConfigImpl(dataSet, keyColumn = "key")

        val source = sourceConfig.resources
        val result = source.read(setOf(Resources.BASE_LOCALE, "ru"), null)
        assertNotNull(result)

        assertEquals(2, result?.size) // 1 locale + base locale

        val resLocaleEn = result?.get("ru")
        assertNotNull(resLocaleEn)

        val resItem1 = resLocaleEn?.get("key1")
        assertNotNull(resItem1)
        assertEquals(2, resItem1?.values?.size)
        val resValue1_1 = resItem1?.valueForQuantity(Quantity.OTHER)
        assertEquals("value1", resValue1_1?.value)
        assertNull(resValue1_1?.comment)

        val resValue1_2 = resItem1?.valueForQuantity(Quantity.ONE)
        assertEquals("value2", resValue1_2?.value)
        assertEquals("Some comment", resValue1_2?.comment)
    }

    @Test
    fun testReadDefaultLocale1() {
        val dataSet = MockDataSet(arrayOf("en", "ru"))
        dataSet.add("key1", null, null, arrayOf("value1_1", "value1_2"), null)
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", arrayOf("value2", "value1_2"), null)
        dataSet.add("key2", null, "Some comment", arrayOf("value2_1", "value2_2"), null)

        val sourceConfig = BaseTableResourcesConfigImpl(dataSet, keyColumn = "key")

        val source = sourceConfig.resources
        val result = source.read(setOf(Resources.BASE_LOCALE, "ru", "en"), null)

        val expectLocale = result?.get("en")
        val baseLocale = result?.get(Resources.BASE_LOCALE)
        assertEquals(expectLocale, baseLocale)
        assertNotSame(expectLocale, baseLocale)
    }

    @Test
    fun testReadDefaultLocale2() {
        val dataSet = MockDataSet(arrayOf("true_base", "ru"))
        dataSet.add("key1", null, null, arrayOf("value1_1", "value1_2"), null)
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", arrayOf("value2", "value1_2"), null)
        dataSet.add("key2", null, "Some comment", arrayOf("value2_1", "value2_2"), null)

        val sourceConfig = BaseTableResourcesConfigImpl(dataSet, keyColumn = "key")

        val source = sourceConfig.resources
        val result = source.read(setOf(Resources.BASE_LOCALE, "en", "true_base", "ru"), null)

        val expectLocale = result?.get("true_base")
        val baseLocale = result?.get(Resources.BASE_LOCALE)
        assertEquals(expectLocale, baseLocale)
        assertNotSame(expectLocale, baseLocale)
    }

    @Test
    fun testReadMetaData() {
        val locales = arrayOf(Resources.BASE_LOCALE, "ru")
        val dataSet = MockDataSet(locales)
        dataSet.add("key1", null, null, arrayOf("value1_1", "value1_2"), "meta_1=data")
        dataSet.add("key1", Quantity.ONE.toString(), "Some comment", arrayOf("value2", "value1_2"), "invalid meta")
        dataSet.add("key2", null, "Some comment", arrayOf("value2_1", "value2_2"), "format=1;case=UPPER")

        val sourceConfig = BaseTableResourcesConfigImpl(dataSet, keyColumn = "key")

        val source = sourceConfig.resources
        val result = source.read(setOf(Resources.BASE_LOCALE, "ru"), null)

        val expectedMap = ResMap().apply {
            val meta1 = mapOf(Pair("meta_1", "data"))
            val meta2 = mapOf(Pair("format", "1"), Pair("case", "UPPER"))

            put("base", ResLocale().apply {
                put(ResItem("key1").apply {
                    addValue(ResValue("value1_1", null, Quantity.OTHER, meta = meta1))
                    addValue(ResValue("value2", "Some comment", Quantity.ONE, meta = null))
                })
                put(ResItem("key2").apply {
                    addValue(ResValue("value2_1", "Some comment", Quantity.OTHER, meta = meta2))
                })
            })
            put("ru", ResLocale().apply {
                put(ResItem("key1").apply {
                    addValue(ResValue("value1_2", null, Quantity.OTHER, meta = meta1))
                    addValue(ResValue("value1_2", "Some comment", Quantity.ONE, meta = null))
                })
                put(ResItem("key2").apply {
                    addValue(ResValue("value2_2", "Some comment", Quantity.OTHER, meta = meta2))
                })
            })
        }

        assertEquals(expectedMap, result)
    }

    private class TableResourcesImpl(
            private val dataSet: MockDataSet
    ) : BaseTableResources() {

        override val formattingType: FormattingType = NoFormattingType

        override fun allFiles(locales: Set<String>): List<File> {
            throw RuntimeException("This method shouldn't be called")
        }

        override val firstRow: Int
            get() = 0

        override val rowsCount: Int
            get() = dataSet.size()

        override fun getValue(col: Int, row: Int): String? {
            return dataSet[col, row]
        }

        override val columnIndexes: ColumnIndexes
            get() = dataSet.columnIndexes

        override fun write(resMap: ResMap, extraParams: ExtraParams?) {
            // Do nothing
        }
    }

    private class BaseTableResourcesConfigImpl(
        private val dataSet: MockDataSet,
        keyColumn: String,
        quantityColumn: String? = null,
        commentColumn: String? = null,
        metadataColumn: String? = null
    ) : BaseTableResourcesConfig(
        keyColumn, quantityColumn, commentColumn, metadataColumn
    ) {

        override val type: String
            get() = "mock"

        override val resources: Resources
            get() = TableResourcesImpl(dataSet)
    }
}
