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
import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.config.source.Source
import ru.pocketbyte.locolaser.config.source.SourceConfig
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.summary.FileSummary
import ru.pocketbyte.locolaser.summary.Summary
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.*

import org.junit.Assert.*

/**
 * @author Denis Shurygin
 */
class LocoLaserTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private var config: Config? = null
    private var platformConfig: MockPlatformConfig? = null
    private var platformResources: MockPlatformResources? = null

    private var source: MockSource? = null
    private var sourceConfig: MockSourceConfig? = null

    private var mResMap: ResMap? = null

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
        mResMap!!.put("en", localeEn)
        mResMap!!.put("ru", localeRu)

        platformResources = MockPlatformResources(null, null)
        platformConfig = MockPlatformConfig("mockPlatform", platformResources!!)

        sourceConfig = MockSourceConfig("mockSource", mResMap!!, HashSet(Arrays.asList("en", "ru")))
        source = sourceConfig!!.mSource

        config = Config()
        config!!.file = File(workDir, "config.json")
        config!!.platform = platformConfig
        config!!.sourceConfig = sourceConfig

        // Write config file to make it not empty
        val writer = PrintWriter(config!!.file!!)
        writer.write("{}")
        writer.flush()
        writer.close()
    }

    @After
    fun deInit() {
        Summary.setFactory(null)
    }

    // ====================================================
    // Test invalid config

    @Test(expected = IllegalArgumentException::class)
    fun testNullPlatform() {
        config!!.platform = null
        LocoLaser.localize(config!!)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNullSources() {
        config!!.sourceConfig = null
        LocoLaser.localize(config!!)
    }

    // ====================================================
    // Test conflict strategy

    @Test
    fun testConflictStrategyRemoveLocal() {
        config!!.conflictStrategy = Config.ConflictStrategy.REMOVE_PLATFORM

        val locale = "en"
        val newKey = "newKey"
        // Check if original map doesn't contain new value
        assertFalse(mResMap!![locale]!!.containsKey(newKey))

        val resLocale = ResLocale()
        resLocale.put(buildItem(newKey, "value"))

        platformResources!!.mMap = ResMap()
        platformResources!!.mMap!!.put(locale, resLocale)

        assertTrue(LocoLaser.localize(config!!))
        assertEquals(platformResources!!.mMap, source!!.mMap)
    }

    @Test
    fun testConflictStrategyKeepNewLocal() {
        config!!.conflictStrategy = Config.ConflictStrategy.KEEP_NEW_PLATFORM

        val locale = "en"
        val newKey = "newKey"
        // Check if original map doesn't contain new value
        assertFalse(mResMap!![locale]!!.containsKey(newKey))

        val resLocale = ResLocale()
        resLocale.put(buildItem(newKey, "value"))

        platformResources!!.mMap = ResMap()
        platformResources!!.mMap!!.put(locale, resLocale)

        assertTrue(LocoLaser.localize(config!!))
        assertNotEquals(platformResources!!.mMap, source!!.mMap)

        platformResources!!.mMap!![locale]!!.remove(newKey)
        assertEquals(platformResources!!.mMap, source!!.mMap)
    }

    @Test
    fun testConflictStrategyExportNewLocal() {
        config!!.conflictStrategy = Config.ConflictStrategy.EXPORT_NEW_PLATFORM

        val locale = "en"
        val newKey = "newKey"
        // Check if original map doesn't contain new value
        assertFalse(mResMap!![locale]!!.containsKey(newKey))

        val resLocale = ResLocale()
        resLocale.put(buildItem(newKey, "value"))

        platformResources!!.mMap = ResMap()
        platformResources!!.mMap!!.put(locale, resLocale)

        assertTrue(LocoLaser.localize(config!!))
        assertEquals(platformResources!!.mMap, source!!.mMap)

        assertTrue(source!!.mMap!![locale]!!.containsKey(newKey))
    }


    @Test
    fun testConflictStrategyExportMissedValues() {
        config!!.conflictStrategy = Config.ConflictStrategy.EXPORT_NEW_PLATFORM

        val missedKey = "key1"
        val locale = "ru"
        val location = object : Source.ValueLocation(source!!) {

        }

        platformResources!!.mMap = ResMap(mResMap)
        assertNotNull(source!!.mMap!![locale]!!.remove(missedKey))

        source!!.mMissedValues = listOf(Source.MissedValue(
                missedKey, locale,
                Quantity.OTHER, location))

        assertTrue(LocoLaser.localize(config!!))
        assertEquals(platformResources!!.mMap, source!!.mMap)

        val missedItem = source!!.mMap!![locale]!![missedKey]!!
        assertNotNull(missedItem)
        assertTrue(missedItem.valueForQuantity(Quantity.OTHER)!!.location === location)
    }

    // ====================================================
    // Other

    @Test
    fun testSimpleReadAndWrite() {
        assertNull(platformResources!!.mMap)
        assertNotNull(source!!.mMap)

        assertTrue(LocoLaser.localize(config!!))

        assertEquals(platformResources!!.mMap, source!!.mMap)
    }

    @Test
    fun testWritingConfigUsage() {
        assertNull(platformResources!!.mExtraParams)
        assertNotNull(config!!.extraParams)

        assertTrue(LocoLaser.localize(config!!))

        assertTrue(platformResources!!.mExtraParams == config!!.extraParams)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testLocalizationNotNeeded() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        assertTrue(LocoLaser.localize(config!!))
        assertNull(platformResources!!.mMap)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testForceImport() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        config!!.isForceImport = true
        assertTrue(LocoLaser.localize(config!!))
        assertNotNull(platformResources!!.mMap)
        assertEquals(platformResources!!.mMap, source!!.mMap)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testConfigChanged() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        // Override config file
        val writer = PrintWriter(config!!.file!!)
        writer.write("{\"key\":\"wrong\"}")
        writer.flush()
        writer.close()

        assertTrue(LocoLaser.localize(config!!))
        assertNotNull(platformResources!!.mMap)
        assertEquals(platformResources!!.mMap, source!!.mMap)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testSourceChanged() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        source!!.modifiedDate++

        assertTrue(LocoLaser.localize(config!!))
        assertNotNull(platformResources!!.mMap)
        assertEquals(platformResources!!.mMap, source!!.mMap)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testLocalResourceChanged() {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges()

        val changedLocale = "en"
        val localeSummary = platformResources!!.mSummaryMap!![changedLocale]!!

        platformResources!!.mSummaryMap!!.put(changedLocale, FileSummary(localeSummary.bytes + 1, "hash_en"))

        assertTrue(LocoLaser.localize(config!!))
        assertNotNull(platformResources!!.mMap)
        assertNotEquals(platformResources!!.mMap, source!!.mMap)
        assertEquals(1, platformResources!!.mMap!!.size.toLong())
        assertEquals(platformResources!!.mMap!![changedLocale], source!!.mMap!![changedLocale])
    }

    @Test
    fun testSourceClose() {
        assertTrue(LocoLaser.localize(config!!))
        assertTrue(source!!.isClosed)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testSaveSummary() {
        platformResources!!.mSummaryMap = HashMap(2)
        platformResources!!.mSummaryMap!!.put("en", FileSummary(2300000, "hash_en"))
        platformResources!!.mSummaryMap!!.put("ru", FileSummary(2123123, "hash_ru"))

        val jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + FileSummary(12312, "old_config").toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":" + (source!!.modifiedDate + 1) + "," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + FileSummary(23213, "old_en").toJson() + "," +
                "\"ru\":" + FileSummary(24563, "old_ru").toJson() + "}" +
                "}"

        val tempDir = config!!.tempDir
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
        assertEquals(source!!.modifiedDate + 1, summary.sourceModifiedDate)

        assertTrue(LocoLaser.localize(config!!))

        //Check if summary changed
        summary = Summary.loadSummary(config)!!
        assertEquals(FileSummary(config!!.file), summary.configSummary)
        assertEquals(FileSummary(2300000, "hash_en"), summary.getResourceSummary("en"))
        assertEquals(FileSummary(2123123, "hash_ru"), summary.getResourceSummary("ru"))
        assertEquals(source!!.modifiedDate, summary.sourceModifiedDate)
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
        platformResources!!.mSummaryMap = HashMap(2)
        platformResources!!.mSummaryMap!!.put("en", FileSummary(23, "hash_en"))
        platformResources!!.mSummaryMap!!.put("ru", FileSummary(2123123, "hash_ru"))

        val jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + FileSummary(config!!.file).toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":" + source!!.modifiedDate + "," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + platformResources!!.mSummaryMap!!["en"]!!.toJson() + "," +
                "\"ru\":" + platformResources!!.mSummaryMap!!["ru"]!!.toJson() + "}" +
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

    private inner class MockPlatformConfig internal constructor(
            override val type: String,
            override val resources: PlatformResources
    ) : PlatformConfig {

        override val defaultTempDir: File
            get() = File(System.getProperty("user.dir"), "./temp/")

    }

    private inner class MockPlatformResources internal constructor(
            var mMap: ResMap?,
            var mSummaryMap: MutableMap<String, FileSummary>?
    ) : PlatformResources {

        var mExtraParams: ExtraParams? = null

        override fun read(locales: Set<String>, extraParams: ExtraParams): ResMap {
            val resMap = ResMap()
            if (mMap != null) {
                for (locale in locales) {
                    resMap[locale] = ResLocale(mMap!![locale])
                }
            }
            return resMap
        }

        @Throws(IOException::class)
        override fun write(map: ResMap, extraParams: ExtraParams?) {
            mMap = ResMap(map)
            mExtraParams = extraParams
        }

        override fun summaryForLocale(locale: String): FileSummary {
            return mSummaryMap?.get(locale) ?: throw IllegalArgumentException()
        }
    }

    private class MockSourceConfig internal constructor(
            override val type: String,
            resMap: ResMap,
            private val mLocales: Set<String>
    ) : SourceConfig {

        val mSource: MockSource = MockSource(this, ResMap(resMap), System.currentTimeMillis())

        override fun open(): Source? {
            return mSource
        }

        override val locales: Set<String>
            get() = mLocales
    }

    private class MockSource(
            config: SourceConfig,
            var mMap: ResMap?,
            override var modifiedDate: Long) : Source(config) {

        var isClosed = false
        var mMissedValues: List<Source.MissedValue>? = null

        override fun read(): Source.ReadResult {
            return Source.ReadResult(ResMap(mMap), mMissedValues)
        }

        override fun write(resMap: ResMap) {
            if (mMap == null)
                mMap = ResMap(resMap)
            else
                mMap!!.merge(resMap)
        }

        override fun close() {
            if (isClosed)
                throw IllegalStateException("Source should be closed only once")
            isClosed = true
        }
    }
}
