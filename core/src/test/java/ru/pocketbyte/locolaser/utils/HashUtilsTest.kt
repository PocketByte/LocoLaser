/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import java.io.File
import java.io.IOException
import java.io.PrintWriter

import org.junit.Assert.*

/**
 * @author Denis Shurygin
 */
class HashUtilsTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testMD5ChecksumForSameFiles() {
        val fileContent = "text1 text2 text3 tex4 text5 te6xt te7xt"

        val file1 = prepareTestFile(fileContent)
        assertEquals(file1.getMD5Checksum(), file1.getMD5Checksum())

        val file2 = prepareTestFile(fileContent)
        assertEquals(file1.getMD5Checksum(), file2.getMD5Checksum())
    }

    @Test
    @Throws(IOException::class)
    fun testMD5ChecksumForNotSameFiles() {
        val fileContent1 = "text1 text2 text3 tex4 text5 te6xt te7xt"
        val fileContent2 = "text1 text2 text3 tex4 text5 te6xt te7xt another"

        assertNotEquals(fileContent1, fileContent2)

        val file1 = prepareTestFile(fileContent1)
        val file2 = prepareTestFile(fileContent2)

        assertNotEquals(file1.getMD5Checksum(), file2.getMD5Checksum())
    }

    @Test
    @Throws(IOException::class)
    fun testMD5ChecksumForNotExistsFile() {
        val file = tempFolder.newFile()
        assertTrue(file.delete())

        assertNull(file.getMD5Checksum())
    }

    @Test
    @Throws(IOException::class)
    fun testMD5ChecksumForNullFile() {
        assertNull((null as File?).getMD5Checksum())
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
