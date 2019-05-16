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
import ru.pocketbyte.locolaser.utils.HashUtils

import java.io.File
import java.io.IOException
import java.io.PrintWriter

import org.junit.Assert.*

/**
 * @author Denis Shurygin
 */
class FileSummaryTest {

    companion object {
        private val JSON_PARSER = JSONParser()
    }

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private var mBytes: Long = 0
    private var mHash: String? = null

    @Before
    fun init() {
        mBytes = 100
        mHash = "testtesttest"
    }

    @Test
    @Throws(ParseException::class)
    fun testMockJson() {
        val jsonObject = JSON_PARSER.parse(buildMockJson(mBytes, mHash))
        assertTrue(jsonObject is JSONObject)
    }


    @Test
    @Throws(ParseException::class)
    fun testConstructorFromParams() {
        val fileSummary = FileSummary(mBytes, mHash!!)
        assertEquals(mBytes, fileSummary.bytes)
        assertEquals(mHash, fileSummary.hash)
    }

    @Test
    @Throws(ParseException::class)
    fun testConstructorFromJson() {
        val fileSummary = FileSummary(JSON_PARSER.parse(buildMockJson(mBytes, mHash)) as JSONObject)
        assertEquals(mBytes, fileSummary.bytes)
        assertEquals(mHash, fileSummary.hash)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testConstructorFromBadJson() {
        val json = JSONObject()
        var fileSummary = FileSummary(json)
        assertEquals(0, fileSummary.bytes)
        assertNull(fileSummary.hash)

        json.put(FileSummary.BYTES, "string")
        fileSummary = FileSummary(json)
        assertEquals(0, fileSummary.bytes)

        json.put(FileSummary.HASH, JSONObject())
        fileSummary = FileSummary(json)
        assertNull(fileSummary.hash)

        fileSummary = FileSummary(json)
        assertEquals(0, fileSummary.bytes)
        assertNull(fileSummary.hash)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testConstructorFromFile() {
        val file = prepareTestFile("test text")
        val fileSummary = FileSummary(file)
        assertEquals(file.length(), fileSummary.bytes)
        assertEquals(HashUtils.getMD5Checksum(file), fileSummary.hash)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testConstructorFromNotExistsFile() {
        val file = tempFolder.newFile()
        assertTrue(file.delete())

        val fileSummary = FileSummary(file)
        assertEquals(0, fileSummary.bytes)
        assertNull(fileSummary.hash)
    }

    @Test
    @Throws(ParseException::class, IOException::class)
    fun testConstructorFromFilesArray() {
        val file1 = prepareTestFile("test text")
        val file2 = prepareTestFile("test text2")

        var fileSummary = FileSummary(arrayOf<File?>(file1, file2))
        assertEquals(file1.length() + file2.length(), fileSummary.bytes)
        assertEquals(HashUtils.getMD5Checksum(file1) + HashUtils.getMD5Checksum(file2), fileSummary.hash)

        fileSummary = FileSummary(arrayOf(file1, null, file2))
        assertEquals(file1.length() + file2.length(), fileSummary.bytes)
        assertEquals(HashUtils.getMD5Checksum(file1) + HashUtils.getMD5Checksum(file2), fileSummary.hash)

        assertTrue(file2.delete())
        fileSummary = FileSummary(arrayOf(file1, null, file2))
        assertEquals(file1.length(), fileSummary.bytes)
        assertEquals(HashUtils.getMD5Checksum(file1) + "null", fileSummary.hash)
    }

    @Test
    @Throws(IOException::class)
    fun testEqualsToFile() {
        val file = prepareTestFile("Test text")
        val fileSummary = FileSummary(file)
        assertTrue(fileSummary.equalsToFile(file))
    }

    @Test
    @Throws(IOException::class)
    fun testEquals() {
        var fileSummary1 = FileSummary(1, "hash")
        var fileSummary2 = FileSummary(1, "hash")

        assertEquals(fileSummary1, fileSummary2)
        assertEquals(fileSummary2, fileSummary1)

        fileSummary2 = FileSummary(2, "hash")
        assertNotEquals(fileSummary1, fileSummary2)
        assertNotEquals(fileSummary2, fileSummary1)

        fileSummary2 = FileSummary(1, "wrong")
        assertNotEquals(fileSummary1, fileSummary2)
        assertNotEquals(fileSummary2, fileSummary1)

        // Summary with 0 bytes not equals
        fileSummary1 = FileSummary(0, "hash")
        fileSummary2 = FileSummary(0, "hash")
        assertNotEquals(fileSummary1, fileSummary2)
        assertNotEquals(fileSummary2, fileSummary1)

        // Summary with null hash not equals
        fileSummary1 = FileSummary(1, null)
        fileSummary2 = FileSummary(1, null)
        assertNotEquals(fileSummary1, fileSummary2)
        assertNotEquals(fileSummary2, fileSummary1)
    }

    @Test
    fun testToJson() {
        val summary = FileSummary(23, "test_hash")
        val json = summary.toJson()

        assertEquals(summary.bytes, json[FileSummary.BYTES])
        assertEquals(summary.hash, json[FileSummary.HASH])
    }

    private fun buildMockJson(bytes: Long, hash: String?): String {
        val json = "{\"" + FileSummary.BYTES + "\":" + bytes + ",\"" + FileSummary.HASH + "\":\"" + hash + "\"}"
        print(json)
        return json
    }

    @Throws(IOException::class)
    private fun prepareTestFile(text: String): File {
        val file = tempFolder.newFile()
        val writer = PrintWriter(file)
        writer.write(text)
        writer.flush()
        writer.close()
        return file
    }
}
