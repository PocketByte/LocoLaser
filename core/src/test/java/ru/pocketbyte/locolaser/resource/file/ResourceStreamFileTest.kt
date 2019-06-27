/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.file

import org.junit.*
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.resource.entity.ResMap

import java.io.*
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

/**
 * @author Denis Shurygin
 */
class ResourceStreamFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private var streamFile: ResourceStreamFile? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        streamFile = object : ResourceStreamFile(tempFolder.newFile()) {
            override fun read(): ResMap? {
                return null
            }

            @Throws(IOException::class)
            override fun write(resMap: ResMap, writingConfig: WritingConfig?) {

            }
        }
    }

    @After
    @Throws(IOException::class)
    fun deInit() {
        streamFile!!.close()
    }

    @Test
    @Throws(IOException::class)
    fun testOpen() {
        assertFalse(streamFile!!.isOpen)

        streamFile!!.open()
        assertTrue(streamFile!!.isOpen)
    }

    @Test
    @Throws(IOException::class)
    fun testOpenNoFile() {
        assertTrue(streamFile!!.file.delete())

        streamFile!!.open()
        assertTrue(streamFile!!.isOpen)
    }

    @Test(expected = IllegalStateException::class)
    @Throws(IOException::class)
    fun testWriteStringWithoutOpen() {
        streamFile!!.writeString("str")
    }

    @Test(expected = IllegalStateException::class)
    @Throws(IOException::class)
    fun testWriteStringLnWithoutOpen() {
        streamFile!!.writeStringLn("str")
    }

    @Test(expected = IllegalStateException::class)
    @Throws(IOException::class)
    fun testWritelnWithoutOpen() {
        streamFile!!.writeln()
    }

    @Test
    @Throws(IOException::class)
    fun testWriteString() {
        val testString = "test string"
        streamFile!!.open()
        streamFile!!.writeString(testString)

        val result = String(Files.readAllBytes(Paths.get(streamFile!!.file.toURI())))
        assertEquals(testString, result)
    }

    @Test
    @Throws(IOException::class)
    fun testWriteStringLn() {
        streamFile!!.open()
        streamFile!!.writeStringLn("1")
        streamFile!!.writeString("2")

        val result = Files.readAllLines(Paths.get(streamFile!!.file.toURI()), Charset.defaultCharset())
        assertEquals(2, result.size.toLong())
        assertEquals("1", result[0])
        assertEquals("2", result[1])
    }

    @Test
    @Throws(IOException::class)
    fun testWriteln() {
        streamFile!!.open()
        streamFile!!.writeString("1")
        streamFile!!.writeln()
        streamFile!!.writeString("2")

        val result = Files.readAllLines(Paths.get(streamFile!!.file.toURI()), Charset.defaultCharset())
        assertEquals(2, result.size.toLong())
        assertEquals("1", result[0])
        assertEquals("2", result[1])
    }
}