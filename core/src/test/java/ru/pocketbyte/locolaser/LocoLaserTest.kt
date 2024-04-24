/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser

import org.json.simple.JSONObject
import org.json.simple.parser.ParseException
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.summary.FileSummary
import ru.pocketbyte.locolaser.summary.Summary
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

import java.io.File
import java.io.IOException
import java.io.PrintWriter

import org.junit.Assert.*
import ru.pocketbyte.locolaser.config.trimUnsupportedQuantities
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.plus
import kotlin.collections.HashMap

/**
 * @author Denis Shurygin
 */
class LocoLaserTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private lateinit var config: Config
    private lateinit var platformConfig: MockResourcesConfig
    private lateinit var platformResources: MockResources

    private lateinit var source: MockSource
    private lateinit var sourceConfig: MockSourceConfig

    private lateinit var mResMap: ResMap

    @Before
    @Throws(IOException::class)
    fun init() {
        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)

        Summary.setFactory(object : Summary.Factory {
            override fun loadSummary(config: Config?): Summary? {
                return null
            }
        })

        val localeEn = ResLocale()
        localeEn.put(buildItem("key1", "valueEn1"))
        localeEn.put(buildItem("key2", "valueEn2"))

        val localeRu = ResLocale()
        localeRu.put(buildItem("key1", "valueRu1"))
        localeRu.put(buildItem("key2", "valueRu2"))

        mResMap = ResMap()
        mResMap["en"] = localeEn
        mResMap["ru"] = localeRu

        platformResources = MockResources(null, null)
        platformConfig = MockResourcesConfig("mockPlatform", platformResources)

        sourceConfig = MockSourceConfig("mockSource", mResMap)
        source = sourceConfig.resources

        config = Config(
            workDir = workDir,
            file = File(workDir, "config.json"),
            platform = platformConfig,
            source = sourceConfig,
            locales = setOf("en", "ru")
        )

        // Write config file to make it not empty
        config.file?.let {
            PrintWriter(it).run {
                write("{}")
                flush()
                close()
            }
        }
    }

    @After
    fun deInit() {
        Summary.setFactory(null)
    }

    // ====================================================
    // Test invalid config

    @Test(expected = IllegalArgumentException::class)
    fun testNullPlatform() {
        LocoLaser.localize(config.copy(
            platform = null
        ))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNullSources() {
        LocoLaser.localize(config.copy(
            source = null
        ))
    }

    // ====================================================
    // Test conflict strategy

    @Test
    fun testConflictStrategyRemovePlatform() {
        val locale = "en"
        val newKey = "newKey"
        // Check if original map doesn't contain new key
        assertEquals(false, mResMap[locale]?.containsKey(newKey))

        val resLocale = ResLocale()
        resLocale.put(buildItem(newKey, "value"))

        platformResources.mMap = ResMap().apply {
            put(locale, resLocale)
        }

        assertTrue(LocoLaser.localize(config.copy(
            conflictStrategy = Config.ConflictStrategy.REMOVE_PLATFORM
        )))
        assertEquals(platformResources.mMap, source.mockMap)
    }

    @Test
    fun testConflictStrategyKeepNewPlatform() {
        val locale = "en"
        val newKey = "newKey"
        // Check if original map doesn't contain new key
        assertFalse(mResMap[locale]!!.containsKey(newKey))

        val overriddenKey = "key1"
        val overriddenValue = "valueEn1Overridden"
        // Check if original map contain overridden key with different value
        assertTrue(mResMap[locale]?.containsKey(overriddenKey) ?: false)
        assertNotEquals(overriddenValue, mResMap[locale]?.get(overriddenKey)?.valueForQuantity(
            Quantity.OTHER))

        val resLocale = ResLocale()
        resLocale.put(buildItem(newKey, "value"))
        resLocale.put(buildItem(overriddenKey, overriddenValue))

        platformResources.mMap = ResMap().apply {
            put(locale, resLocale)
        }

        assertTrue(LocoLaser.localize(config.copy(
            conflictStrategy = Config.ConflictStrategy.KEEP_NEW_PLATFORM
        )))
        assertNotEquals(platformResources.mMap, source.mockMap)

        platformResources.mMap?.get(locale)?.remove(newKey)
        assertEquals(platformResources.mMap, source.mockMap)
    }

    @Test
    fun testConflictStrategyKeepPlatform() {
        val locale = "en"
        val newKey = "newKey"
        // Check if original map doesn't contain new value
        assertFalse(mResMap[locale]?.containsKey(newKey) ?: false)

        val overriddenKey = "key1"
        val overriddenValue = "valueEn1Overridden"
        // Check if original map contain overridden key with different value
        assertTrue(mResMap[locale]?.containsKey(overriddenKey) ?: false)
        assertNotEquals(overriddenValue, mResMap[locale]?.get(overriddenKey)?.valueForQuantity(
            Quantity.OTHER))

        val resLocale = ResLocale()
        resLocale.put(buildItem(newKey, "value"))
        resLocale.put(buildItem(overriddenKey, overriddenValue))

        platformResources.mMap = ResMap().apply {
            put(locale, resLocale)
        }

        assertTrue(LocoLaser.localize(config.copy(
            conflictStrategy = Config.ConflictStrategy.KEEP_PLATFORM
        )))
        assertNotEquals(platformResources.mMap, source.mockMap)

        platformResources.mMap?.get(locale)?.remove(newKey)
        assertNotEquals(platformResources.mMap, source.mockMap)

        platformResources.mMap?.get(locale)?.get(overriddenKey)?.let {
            // Check if overridden value is not changed
            assertEquals(overriddenValue, it.valueForQuantity(Quantity.OTHER)?.value)

            // Check that it's only one difference
            source.mockMap?.get(locale)?.put(it)
            assertEquals(platformResources.mMap, source.mockMap)
        }
    }

    @Test
    fun testConflictStrategyExportNewLocal() {
        val locale = "en"
        val newKey = "newKey"
        // Check if original map doesn't contain new value
        assertFalse(mResMap[locale]!!.containsKey(newKey))

        val overriddenKey = "key1"
        val overriddenValue = "valueEn1Overridden"
        // Check if original map contain overridden key with different value
        assertTrue(mResMap[locale]?.containsKey(overriddenKey) ?: false)
        assertNotEquals(overriddenValue, mResMap[locale]?.get(overriddenKey)?.valueForQuantity(
            Quantity.OTHER))

        val resLocale = ResLocale()
        resLocale.put(buildItem(newKey, "value"))
        resLocale.put(buildItem(overriddenKey, overriddenValue))

        platformResources.mMap = ResMap().apply {
            put(locale, resLocale)
        }

        assertTrue(LocoLaser.localize(config.copy(
            conflictStrategy = Config.ConflictStrategy.EXPORT_NEW_PLATFORM
        )))
        assertEquals(platformResources.mMap, source.mockMap)

        source.mockMap?.get(locale)?.run {
            assertTrue(containsKey(newKey))
            assertTrue(containsKey(overriddenKey))
        }

        platformResources.mMap?.get(locale)?.get(overriddenKey)?.let {
            // Check if overridden value is replaced
            assertNotEquals(overriddenValue, it.valueForQuantity(Quantity.OTHER)?.value)
        }
    }

    @Test
    fun testConflictStrategyExportLocal() {
        val locale = "en"
        val newKey = "newKey"
        // Check if original map doesn't contain new value
        assertFalse(mResMap[locale]!!.containsKey(newKey))

        val overriddenKey = "key1"
        val overriddenValue = "valueEn1Overridden"
        // Check if original map contain overridden key with different value
        assertTrue(mResMap[locale]?.containsKey(overriddenKey) ?: false)
        assertNotEquals(overriddenValue, mResMap[locale]?.get(overriddenKey)?.valueForQuantity(
            Quantity.OTHER))

        val resLocale = ResLocale()
        resLocale.put(buildItem(newKey, "value"))
        resLocale.put(buildItem(overriddenKey, overriddenValue))

        platformResources.mMap = ResMap().apply {
            put(locale, resLocale)
        }

        assertTrue(LocoLaser.localize(config.copy(
            conflictStrategy = Config.ConflictStrategy.EXPORT_PLATFORM
        )))
        assertEquals(platformResources.mMap, source.mockMap)

        source.mockMap?.get(locale)?.run {
            assertTrue(containsKey(newKey))
            assertTrue(containsKey(overriddenKey))
        }

        platformResources.mMap?.get(locale)?.get(overriddenKey)?.let {
            // Check if overridden value is not changed
            assertEquals(overriddenValue, it.valueForQuantity(Quantity.OTHER)?.value)
        }
    }


    @Test
    fun testConflictStrategyExportMissedValues() {
        val missedKey = "key1"
        val locale = "ru"

        platformResources.mMap = ResMap(mResMap)
        assertNotNull(source.mockMap?.get(locale)?.remove(missedKey))

        assertTrue(LocoLaser.localize(config.copy(
            conflictStrategy = Config.ConflictStrategy.EXPORT_NEW_PLATFORM
        )))
        assertEquals(platformResources.mMap, source.mockMap)

        val missedItem = source.mockMap?.get(locale)?.get(missedKey)
        assertNotNull(missedItem)
    }

    // ====================================================
    // Other

    @Test
    fun testSimpleReadAndWrite() {
        assertNull(platformResources.mMap)
        assertNotNull(source.mockMap)

        assertTrue(LocoLaser.localize(config))

        assertEquals(platformResources.mMap, source.mockMap)
    }

    @Test
    fun testWritingConfigUsage() {
        assertNull(platformResources.mExtraParams)
        assertNotNull(config.extraParams)

        assertTrue(LocoLaser.localize(config))

        assertTrue(platformResources.mExtraParams == config.extraParams)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testLocalizationNotNeeded() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        assertTrue(LocoLaser.localize(config))
        assertNull(platformResources.mMap)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testForceImport() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        assertTrue(LocoLaser.localize(config.copy(
            forceImport = true
        )))
        assertNotNull(platformResources.mMap)
        assertEquals(platformResources.mMap, source.mockMap)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testConfigChanged() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        // Override config file
        val writer = PrintWriter(config.file!!)
        writer.write("{\"key\":\"wrong\"}")
        writer.flush()
        writer.close()

        assertTrue(LocoLaser.localize(config))
        assertNotNull(platformResources.mMap)
        assertEquals(platformResources.mMap, source.mockMap)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testSourceChanged() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        source.mSummaryMap = HashMap<String, FileSummary>(2).apply {
            put("en", FileSummary(45, "changed"))
            put("ru", FileSummary(928, "changed"))
        }

        assertTrue(LocoLaser.localize(config))
        assertNotNull(platformResources.mMap)
        assertEquals(platformResources.mMap, source.mockMap)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testLocalResourceChanged() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        val changedLocale = "en"
        val localeSummary = platformResources.mSummaryMap!![changedLocale]!!

        platformResources.mSummaryMap?.put(changedLocale, FileSummary(localeSummary.bytes + 1, "hash_en"))

        assertTrue(LocoLaser.localize(config))
        assertNotNull(platformResources.mMap)
        assertNotEquals(platformResources.mMap, source.mockMap)
        assertEquals(1, platformResources.mMap?.size)
        assertEquals(platformResources.mMap!![changedLocale], source.mockMap!![changedLocale])
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testSaveSummary() {
        platformResources.mSummaryMap = HashMap<String, FileSummary>(2).apply {
            put("en", FileSummary(2300000, "p_hash_en"))
            put("ru", FileSummary(2123000, "p_hash_ru"))
        }

        source.mSummaryMap = HashMap<String, FileSummary>(2).apply {
            put("en", FileSummary(5, "s_hash_en"))
            put("ru", FileSummary(19, "s_hash_ru"))
        }

        val jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + FileSummary(12312, "old_config").toJson() + "," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + FileSummary(23213, "old_en").toJson() + "," +
                "\"ru\":" + FileSummary(24563, "old_ru").toJson() + "}" +
                "}"

        val tempDir = config.tempDir
        if (!tempDir!!.exists())
            assertTrue(tempDir.mkdirs())
        val summaryFile = File(tempDir, Summary.SUMMARY_FILE_NAME)

        val writer = PrintWriter(summaryFile)
        writer.write(jsonString)
        writer.flush()
        writer.close()

        Summary.setFactory(null)

        //Check if summary saved
        var summary = Summary.loadSummary(config)!!
        assertEquals(FileSummary(12312, "old_config"), summary.configSummary)
        assertEquals(FileSummary(23213, "old_en"), summary.getResourceSummary("en"))
        assertEquals(FileSummary(24563, "old_ru"), summary.getResourceSummary("ru"))

        assertTrue(LocoLaser.localize(config))

        //Check if summary changed
        summary = Summary.loadSummary(config)!!
        assertEquals(FileSummary(config.file), summary.configSummary)
        assertEquals(FileSummary(2300005, "p_hash_ens_hash_en"), summary.getResourceSummary("en"))
        assertEquals(FileSummary(2123019, "p_hash_rus_hash_ru"), summary.getResourceSummary("ru"))
    }

    @Test
    fun testNoTrimUnsupportedQuantities() {
        val pluralItem = ResItem("value3").apply {
            addValue(ResValue("value ZERO", null, quantity = Quantity.ZERO))
            addValue(ResValue("value ONE", null, quantity = Quantity.ONE))
            addValue(ResValue("value TWO", null, quantity = Quantity.TWO))
            addValue(ResValue("value FEW", null, quantity = Quantity.FEW))
            addValue(ResValue("value MANY", null, quantity = Quantity.MANY))
            addValue(ResValue("value OTHER", null, quantity = Quantity.OTHER))
        }
        source.mockMap?.get("ru")?.put(pluralItem)
        source.mockMap?.get("en")?.put(pluralItem)

        assertTrue(LocoLaser.localize(config.copy(
            extraParams = ExtraParams().apply {
                putAll(config.extraParams)
                trimUnsupportedQuantities = false
            }
        )))

        assertEquals(
            ResItem("value3").apply {
                addValue(ResValue("value ZERO", null, quantity = Quantity.ZERO))
                addValue(ResValue("value ONE", null, quantity = Quantity.ONE))
                addValue(ResValue("value TWO", null, quantity = Quantity.TWO))
                addValue(ResValue("value FEW", null, quantity = Quantity.FEW))
                addValue(ResValue("value MANY", null, quantity = Quantity.MANY))
                addValue(ResValue("value OTHER", null, quantity = Quantity.OTHER))
            },
            platformResources.mMap?.get("ru")?.get("value3")
        )

        assertEquals(
            ResItem("value3").apply {
                addValue(ResValue("value ZERO", null, quantity = Quantity.ZERO))
                addValue(ResValue("value ONE", null, quantity = Quantity.ONE))
                addValue(ResValue("value TWO", null, quantity = Quantity.TWO))
                addValue(ResValue("value FEW", null, quantity = Quantity.FEW))
                addValue(ResValue("value MANY", null, quantity = Quantity.MANY))
                addValue(ResValue("value OTHER", null, quantity = Quantity.OTHER))
            },
            platformResources.mMap?.get("en")?.get("value3")
        )
    }

    @Test
    fun testTrimUnsupportedQuantities() {
        val pluralItem = ResItem("value3").apply {
            addValue(ResValue("value ZERO", null, quantity = Quantity.ZERO))
            addValue(ResValue("value ONE", null, quantity = Quantity.ONE))
            addValue(ResValue("value TWO", null, quantity = Quantity.TWO))
            addValue(ResValue("value FEW", null, quantity = Quantity.FEW))
            addValue(ResValue("value MANY", null, quantity = Quantity.MANY))
            addValue(ResValue("value OTHER", null, quantity = Quantity.OTHER))
        }
        source.mockMap?.get("ru")?.put(pluralItem)
        source.mockMap?.get("en")?.put(pluralItem)

        assertTrue(LocoLaser.localize(config.copy(
            extraParams = ExtraParams().apply {
                putAll(config.extraParams)
                trimUnsupportedQuantities = true
            }
        )))

        assertEquals(
            ResItem("value3").apply {
                addValue(ResValue("value ONE", null, quantity = Quantity.ONE))
                addValue(ResValue("value FEW", null, quantity = Quantity.FEW))
                addValue(ResValue("value MANY", null, quantity = Quantity.MANY))
                addValue(ResValue("value OTHER", null, quantity = Quantity.OTHER))
            },
            platformResources.mMap?.get("ru")?.get("value3")
        )

        assertEquals(
            ResItem("value3").apply {
                addValue(ResValue("value ONE", null, quantity = Quantity.ONE))
                addValue(ResValue("value OTHER", null, quantity = Quantity.OTHER))
            },
            platformResources.mMap?.get("en")?.get("value3")
        )
    }

    // ====================================================
    // Private classes and methods

    private fun buildItem(key: String, value: String): ResItem {
        val item = ResItem(key)
        item.addValue(ResValue(value, null))
        return item
    }

    @Throws(IOException::class, ParseException::class)
    private fun prepareStateWhenLocalizationNotNeededBecauseNoChanges(): Summary {
        platformResources.mSummaryMap = HashMap<String, FileSummary>(2).apply {
            put("en", FileSummary(23, "p_hash_en"))
            put("ru", FileSummary(2123123, "p_hash_ru"))
        }

        source.mSummaryMap = HashMap<String, FileSummary>(2).apply {
            put("en", FileSummary(45, "s_hash_en"))
            put("ru", FileSummary(928, "s_hash_ru"))
        }

        val jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + FileSummary(config.file).toJson() + "," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + (
                    platformResources.mSummaryMap?.get("en") +
                    source.mSummaryMap?.get("en")
                )?.toJson() + "," +
                "\"ru\":" + (
                    platformResources.mSummaryMap?.get("ru") +
                    source.mSummaryMap?.get("ru")
                )?.toJson() + "}" +
                "}"

        val summary = Summary(tempFolder.newFile(),
                JsonParseUtils.JSON_PARSER.parse(jsonString) as JSONObject)

        Summary.setFactory(object : Summary.Factory {
            override fun loadSummary(config: Config?): Summary {
                return summary
            }
        })

        return summary
    }

    private inner class MockResourcesConfig constructor(
            override val type: String,
            override val resources: Resources
    ) : ResourcesConfig {

        override val defaultTempDirPath: String = "./temp/"

    }

    private inner class MockResources constructor(
            var mMap: ResMap?,
            var mSummaryMap: MutableMap<String, FileSummary>?
    ) : Resources {

        override val formattingType: FormattingType = NoFormattingType

        var mExtraParams: ExtraParams? = null

        override fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap {
            val resMap = ResMap()
            mMap?.let { map ->
                locales?.forEach { locale ->
                    resMap[locale] = ResLocale(map[locale])
                }
            }
            return resMap
        }

        @Throws(IOException::class)
        override fun write(resMap: ResMap, extraParams: ExtraParams?) {
            mMap = ResMap(resMap)
            mExtraParams = extraParams
        }

        override fun summaryForLocale(locale: String): FileSummary? {
            return mSummaryMap?.get(locale)
        }

        override fun allFiles(locales: Set<String>): List<File> {
            throw RuntimeException("This method shouldn't be called")
        }
    }

    private class MockSourceConfig constructor(
            override val type: String,
            resMap: ResMap
    ) : ResourcesConfig {
        override val defaultTempDirPath: String
            get() = throw NotImplementedError()

        override val resources: MockSource = MockSource(ResMap(resMap), null)
    }

    private class MockSource(
        var mockMap: ResMap?,
        var mSummaryMap: MutableMap<String, FileSummary>?
    ) : Resources {

        override val formattingType: FormattingType = NoFormattingType

        override fun summaryForLocale(locale: String): FileSummary {
            return mSummaryMap?.get(locale) ?: FileSummary(0, null)
        }

        override fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap? {
            return ResMap(mockMap)
        }

        override fun write(resMap: ResMap, extraParams: ExtraParams?) {
            if (mockMap == null)
                mockMap = ResMap(resMap)
            else
                mockMap.merge(resMap)
        }

        override fun allFiles(locales: Set<String>): List<File> {
            throw RuntimeException("This method shouldn't be called")
        }
    }
}
