/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.summary

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.resource.PlatformResources

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformResources

/**
 * @author Denis Shurygin
 */
class SummaryTest {

    companion object {
        private val JSON_PARSER = JSONParser()
    }

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Before
    @Throws(IOException::class)
    fun init() {
        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testConstructorFromJson() {
        val jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + FileSummary(123, "test").toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":23132123," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + FileSummary(1233, "test1").toJson() + "," +
                "\"ru\":" + FileSummary(1263, "test2").toJson() + "}" +
                "}"

        val file = tempFolder.newFile()
        val json = JSON_PARSER.parse(jsonString) as JSONObject
        val summary = Summary(file, json)

        assertEquals(file.canonicalPath, summary.file!!.canonicalPath)
        assertEquals(FileSummary(123, "test"), summary.configSummary)
        assertEquals(23132123, summary.sourceModifiedDate)
        assertEquals(FileSummary(1233, "test1"), summary.getResourceSummary("en"))
        assertEquals(FileSummary(1263, "test2"), summary.getResourceSummary("ru"))
        assertNull(summary.getResourceSummary("none"))
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testConstructorFromBadJson() {
        val summary = Summary(tempFolder.newFile(), JSONObject())
        assertNull(summary.configSummary)

        var jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":[1,2,3]," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":1," +
                "\"" + Summary.RESOURCE_FILES + "\":1" +
                "}"
        var json = JSON_PARSER.parse(jsonString) as JSONObject
        Summary(tempFolder.newFile(), json)

        jsonString = "{" +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":[[0], {\"key\": 431231}, [1, \"str\"]]" +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\": 1," +
                "\"ru\": [\"Str\", 1]}" +
                "}"

        json = JSON_PARSER.parse(jsonString) as JSONObject
        Summary(tempFolder.newFile(), json)
    }


    @Test(expected = IllegalArgumentException::class)
    @Throws(ParseException::class, IOException::class)
    fun testConstructorNullFile() {
        Summary(null, JSONObject())
    }

    @Test
    @Throws(IOException::class)
    fun testSetGetSourceModifiedDate() {
        val summary = Summary(tempFolder.newFile(), JSONObject())
        assertEquals(0, summary.sourceModifiedDate)

        summary.sourceModifiedDate = 1234567
        assertEquals(1234567, summary.sourceModifiedDate)
    }

    @Test
    @Throws(IOException::class)
    fun testSetGetResourceSummary() {
        val summary = Summary(tempFolder.newFile(), JSONObject())
        assertNull(summary.getResourceSummary("en"))
        assertNull(summary.getResourceSummary("ru"))

        val enSummary = FileSummary(1123, "adqwed")
        val ruSummary = FileSummary(1523, "tnetsr")

        summary.setResourceSummary("en", enSummary)
        assertEquals(enSummary, summary.getResourceSummary("en"))
        assertNull(summary.getResourceSummary("ru"))

        summary.setResourceSummary("ru", ruSummary)
        assertEquals(enSummary, summary.getResourceSummary("en"))
        assertEquals(ruSummary, summary.getResourceSummary("ru"))
    }

    @Test
    @Throws(IOException::class)
    fun testSetGetConfigSummary() {
        val summary = Summary(tempFolder.newFile(), JSONObject())
        assertNull(summary.configSummary)

        val config = Config()
        config.file = prepareTestFile("{}")

        summary.setConfigSummary(config)

        assertEquals(FileSummary(config.file), summary.configSummary)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testSave() {

        val jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + FileSummary(4123, "test").toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":23132123," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + FileSummary(1233, "test1").toJson() + "," +
                "\"ru\":" + FileSummary(1263, "test2").toJson() + "}" +
                "}"

        val file = tempFolder.newFile()
        val json = JSON_PARSER.parse(jsonString) as JSONObject
        val summary = Summary(file, json)

        summary.save()

        assertEquals(json.toJSONString(), String(Files.readAllBytes(Paths.get(file.toURI()))))
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testLoad() {
        val config = Config()
        config.file = prepareTestFile("{}")
        config.platform = MockPlatformResources(
                File(System.getProperty("user.dir"), "temp/"),
                "mock") as? PlatformConfig

        val jsonString = (JSON_PARSER.parse("{" +
                "\"" + Summary.CONFIG_FILE + "\":" + FileSummary(config.file).toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":23132123," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + FileSummary(1233, "test1").toJson() + "," +
                "\"ru\":" + FileSummary(1263, "test2").toJson() + "}" +
                "}") as JSONObject).toJSONString()

        prepareTestFile(File(config.tempDir, Summary.SUMMARY_FILE_NAME), jsonString)

        val summary = Summary.loadSummary(config)!!
        assertEquals(jsonString, summary.toJson().toJSONString())

    }

    @Throws(IOException::class)
    private fun prepareTestFile(text: String): File {
        return prepareTestFile(tempFolder.newFile(), text)
    }

    @Throws(IOException::class)
    private fun prepareTestFile(file: File, text: String): File {
        file.parentFile?.mkdirs()
        val writer = PrintWriter(file)
        writer.write(text)
        writer.flush()
        writer.close()
        return file
    }
}
