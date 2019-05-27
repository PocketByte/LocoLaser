/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import org.junit.Assert.*
import org.junit.Test
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.testutils.mock.MockDataSet

import java.util.Arrays
import java.util.HashSet

import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.resource.PlatformResources

/**
 * @author Denis Shurygin
 */
class BaseTableSourceTest {

    @Test
    @Throws(Exception::class)
    fun testReadNoQuantity() {
        val dataSet = MockDataSet(arrayOf("en", "ru"))
        dataSet.add("key1", null, null, arrayOf("value1_1", "value1_2"))
        dataSet.add("key2", null, "Some comment", arrayOf("value2_1", "value2_2"))

        val sourceConfig = BaseTableSourceConfigImpl(dataSet)
        sourceConfig.keyColumn = "key"
        sourceConfig.localeColumns = HashSet(Arrays.asList("en", "ru"))

        val source = sourceConfig.open()
        val result = source!!.read()
        assertNotNull(result)

        assertEquals(2, result.items!!.size.toLong()) // 2 locales
        assertEquals(0, result.missedValues!!.size.toLong()) // no missed values

        // =============
        // Locale EN
        val resLocaleEn = result.items!!["en"]!!
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
        val resLocaleRu = result.items!!["ru"]!!
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
        val dataSet = MockDataSet(arrayOf("en"))
        dataSet.add("key1", null, null, arrayOf("value1"))
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", arrayOf("value2"))
        dataSet.add("key2", Quantity.FEW.toString(), "Some comment", arrayOf("value"))

        val sourceConfig = BaseTableSourceConfigImpl(dataSet)
        sourceConfig.keyColumn = "key"
        sourceConfig.localeColumns = HashSet(Arrays.asList("en"))

        val source = sourceConfig.open()
        val result = source!!.read()
        assertNotNull(result)

        assertEquals(1, result.items!!.size.toLong()) // 1 locale
        assertEquals(0, result.missedValues!!.size.toLong()) // no missed values

        val resLocaleEn = result.items!!["en"]!!
        assertNotNull(resLocaleEn)

        val resItem1 = resLocaleEn["key1"]
        assertNotNull(resItem1)
        assertEquals(2, resItem1!!.values.size.toLong())
        val resValue1_1 = resItem1.valueForQuantity(Quantity.OTHER)
        assertEquals("value1", resValue1_1!!.value)
        assertNull(resValue1_1.comment)

        val resValue1_2 = resItem1.valueForQuantity(Quantity.ZERO)
        assertEquals("value2", resValue1_2!!.value)
        assertEquals("Some comment", resValue1_2.comment)
    }

    @Test
    @Throws(Exception::class)
    fun testReadMissedValues() {
        val dataSet = MockDataSet(arrayOf("en", "ru"))
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", arrayOf("value2", null))
        dataSet.add("key1", null, null, arrayOf("value1", "value2"))
        dataSet.add("key2", Quantity.FEW.toString(), "Some comment", arrayOf(null, "value"))

        val sourceConfig = BaseTableSourceConfigImpl(dataSet)
        sourceConfig.keyColumn = "key"
        sourceConfig.localeColumns = HashSet(Arrays.asList("en", "ru"))

        val source = sourceConfig.open()
        val result = source!!.read()
        assertNotNull(result)

        assertEquals(2, result.missedValues!!.size.toLong()) // 2 missed values

        for (missedValue in result.missedValues!!) {
            when (missedValue.key) {
                "key1" -> {
                    assertEquals("ru", missedValue.locale)
                    assertEquals(Quantity.ZERO, missedValue.quantity)
                    assertNotNull(missedValue.location)
                    assert(missedValue.location is BaseTableSource.CellLocation)
                    val location = missedValue.location as BaseTableSource.CellLocation
                    assertEquals(dataSet.columnIndexes.locales.getValue("ru").toInt().toLong(), location.col.toLong())
                    assertEquals(0, location.row.toLong())
                }
                "key2" -> {
                    assertEquals("en", missedValue.locale)
                    assertEquals(Quantity.FEW, missedValue.quantity)
                    assertNotNull(missedValue.location)
                    assert(missedValue.location is BaseTableSource.CellLocation)
                    val location = missedValue.location as BaseTableSource.CellLocation
                    assertEquals(dataSet.columnIndexes.locales.getValue("en").toInt().toLong(), location.col.toLong())
                    assertEquals(2, location.row.toLong())
                }
                else -> throw Exception()
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun testReadCellLocations() {
        val dataSet = MockDataSet(arrayOf("en", "ru"))
        dataSet.add("key1", null, null, arrayOf("value1_1", "value1_2"))
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", arrayOf("value2", "value1_2"))
        dataSet.add("key2", null, "Some comment", arrayOf("value2_1", "value2_2"))

        val sourceConfig = BaseTableSourceConfigImpl(dataSet)
        sourceConfig.keyColumn = "key"
        sourceConfig.localeColumns = HashSet(Arrays.asList("en", "ru"))

        val source = sourceConfig.open()
        val result = source!!.read()

        // =============
        // Locale EN
        val resLocaleEn = result.items!!["en"]!!
        var expectedCol = dataSet.columnIndexes.locales.getValue("en")

        assertLocation(resLocaleEn["key1"]!!.valueForQuantity(Quantity.OTHER), expectedCol, 0)
        assertLocation(resLocaleEn["key1"]!!.valueForQuantity(Quantity.ZERO), expectedCol, 1)
        assertLocation(resLocaleEn["key2"]!!.valueForQuantity(Quantity.OTHER), expectedCol, 2)

        // =============
        // Locale RU
        val resLocaleRu = result.items!!["ru"]!!
        expectedCol = dataSet.columnIndexes.locales.getValue("ru")

        assertLocation(resLocaleRu["key1"]!!.valueForQuantity(Quantity.OTHER), expectedCol, 0)
        assertLocation(resLocaleRu["key1"]!!.valueForQuantity(Quantity.ZERO), expectedCol, 1)
        assertLocation(resLocaleRu["key2"]!!.valueForQuantity(Quantity.OTHER), expectedCol, 2)
    }

    @Test
    fun testReadDefaultLocale1() {
        val dataSet = MockDataSet(arrayOf("en", "ru"))
        dataSet.add("key1", null, null, arrayOf("value1_1", "value1_2"))
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", arrayOf("value2", "value1_2"))
        dataSet.add("key2", null, "Some comment", arrayOf("value2_1", "value2_2"))

        val sourceConfig = BaseTableSourceConfigImpl(dataSet)
        sourceConfig.keyColumn = "key"
        sourceConfig.localeColumns = HashSet(Arrays.asList("en", "ru"))

        val source = sourceConfig.open()
        val result = source!!.read()

        val expectLocale = result.items?.get("en")!!
        val baseLocale = result.items?.get(PlatformResources.BASE_LOCALE)!!
        assertEquals(expectLocale, baseLocale)
        assertNotSame(expectLocale, baseLocale)
    }

    @Test
    fun testReadDefaultLocale2() {
        val dataSet = MockDataSet(arrayOf("true_base", "ru"))
        dataSet.add("key1", null, null, arrayOf("value1_1", "value1_2"))
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", arrayOf("value2", "value1_2"))
        dataSet.add("key2", null, "Some comment", arrayOf("value2_1", "value2_2"))

        val sourceConfig = BaseTableSourceConfigImpl(dataSet)
        sourceConfig.keyColumn = "key"
        sourceConfig.localeColumns = HashSet(Arrays.asList("en", "true_base", "ru"))

        val source = sourceConfig.open()
        val result = source!!.read()

        val expectLocale = result.items?.get("true_base")!!
        val baseLocale = result.items?.get(PlatformResources.BASE_LOCALE)!!
        assertEquals(expectLocale, baseLocale)
        assertNotSame(expectLocale, baseLocale)
    }

    @Throws(Exception::class)
    private fun assertLocation(resValue: ResValue?, col: Int, row: Int) {
        assertNotNull(resValue!!.location)
        assert(resValue.location is BaseTableSource.CellLocation)
        assertEquals(col.toLong(), (resValue.location as BaseTableSource.CellLocation).col.toLong())
        assertEquals(row.toLong(), (resValue.location as BaseTableSource.CellLocation).row.toLong())
    }

    private class TableSourceImpl(
            sourceConfig: BaseTableSourceConfig,
            private val mDataSet: MockDataSet
    ) : BaseTableSource(sourceConfig) {

        override val firstRow: Int
            get() = 0

        override val rowsCount: Int
            get() = mDataSet.size()

        override fun getValue(col: Int, row: Int): String? {
            return mDataSet[col, row]
        }

        override val columnIndexes: ColumnIndexes
            get() = mDataSet.columnIndexes

        override val modifiedDate: Long
            get() = 0

        override fun write(resMap: ResMap) {
            // Do nothing
        }

        override fun close() {
            // Do nothing
        }
    }

    private class BaseTableSourceConfigImpl internal constructor(
            private val mDataSet: MockDataSet
    ) : BaseTableSourceConfig() {

        override val type: String
            get() = "mock"

        override fun open(): Source? {
            return TableSourceImpl(this, mDataSet)
        }
    }
}
