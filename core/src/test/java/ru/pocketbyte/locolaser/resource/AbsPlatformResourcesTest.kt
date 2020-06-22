/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformResources
import ru.pocketbyte.locolaser.testutils.mock.MockResourceFile

import java.io.File
import java.io.IOException
import java.util.Arrays
import java.util.HashSet

/**
 * @author Denis Shurygin
 */
class AbsPlatformResourcesTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private var resFolder: File? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        resFolder = tempFolder.newFolder()
    }

    @Test
    fun testConstructor() {
        val resources = MockPlatformResources(resFolder!!, NAME, null)

        assertEquals(NAME, resources.name)
        assertEquals(resFolder, resources.directory)
    }

    @Test
    fun testRead() {
        val res1 = MockResourceFile(prepareResMap1())
        val res2 = MockResourceFile(prepareResMap2())

        val resources = object : MockPlatformResources(resFolder!!, NAME, null) {
            override fun getResourceFiles(locales: Set<String>): Array<ResourceFile> {
                return arrayOf(res1, res2)
            }
        }

        val result = resources.read(allLocales, ExtraParams())

        val expectedMap = prepareResMap1().merge(prepareResMap2())

        assertEquals(expectedMap, result)
    }

    @Test
    @Throws(IOException::class)
    fun testWrite() {
        val map = ResMap()
        val res1 = MockResourceFile(null)
        val res2 = MockResourceFile(null)

        val resources = object : MockPlatformResources(resFolder!!, NAME, null) {
            override fun getResourceFiles(locales: Set<String>): Array<ResourceFile> {
                return arrayOf(res1, res2)
            }
        }

        resources.write(map, null)
        assertTrue(map === res1.map)
        assertTrue(map === res2.map)
    }

    @Test
    @Throws(IOException::class)
    fun testWriteFiltered() {
        val map = ResMap()
        val res = MockResourceFile(null)

        val filter: ((key: String) -> Boolean)? = { it.contains("1") }
        val resources = object : MockPlatformResources(resFolder!!, NAME, filter) {
            override fun getResourceFiles(locales: Set<String>): Array<ResourceFile> {
                return arrayOf(res)
            }
        }

        resources.write(map, null)
        assertNotSame(map, res.map)
        assertEquals(map.filter(filter), res.map)
    }

    private val allLocales: Set<String>
        get() = HashSet(Arrays.asList("locale1", "locale2", "locale3", "locale4"))

    private fun prepareResMap1(): ResMap {
        val resMap = ResMap()
        resMap.put("locale1", prepareResLocale1())
        resMap.put("locale2", prepareResLocale2())
        resMap.put("locale3", prepareResLocale3())
        return resMap
    }

    private fun prepareResMap2(): ResMap {
        val resMap = ResMap()
        resMap.put("locale1", prepareResLocale2())
        resMap.put("locale2", prepareResLocale3())
        resMap.put("locale4", prepareResLocale1())
        return resMap
    }

    private fun prepareResLocale1(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", null, Quantity.OTHER), ResValue("value2_1", null, Quantity.MANY), ResValue("value2_1", null, Quantity.FEW))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value1_1", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value1_1", null, Quantity.OTHER), ResValue("value2_1", null, Quantity.MANY), ResValue("value3_1", "Comment", Quantity.ZERO))))
        return resLocale
    }

    private fun prepareResLocale2(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER), ResValue("value2_2", null, Quantity.MANY), ResValue("value3_2", "Comment", Quantity.TWO))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key4", arrayOf(ResValue("value1_2", null, Quantity.OTHER), ResValue("value2_2", null, Quantity.MANY), ResValue("value3_2", "Comment", Quantity.ZERO))))
        return resLocale
    }

    private fun prepareResLocale3(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_3", null, Quantity.OTHER), ResValue("value2_3", null, Quantity.FEW), ResValue("value3_3", "Comment", Quantity.TWO), ResValue("value4_3", "Comment", Quantity.ZERO))))
        resLocale.put(prepareResItem("key4", arrayOf(ResValue("value1_3", null, Quantity.OTHER), ResValue("value3_3", "Comment", Quantity.ZERO))))
        return resLocale
    }

    private fun prepareResItem(key: String, values: Array<ResValue>): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }

    companion object {
        private const val NAME = "name"
    }
}
