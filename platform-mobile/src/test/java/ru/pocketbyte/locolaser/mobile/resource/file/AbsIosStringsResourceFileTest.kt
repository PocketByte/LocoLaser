package ru.pocketbyte.locolaser.mobile.resource.file

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import java.io.File
import java.io.IOException

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import ru.pocketbyte.locolaser.config.ExtraParams

class AbsIosStringsResourceFileTest {

    companion object {
        const val TEST_STRING = "?'test';:<tag>\"value\nsecond line\" %1\$s %2\$s %s<tagg/>" + " Wrong Formats: %\$s $5s"
        const val PLATFORM_TEST_STRING = "?'test';:<tag>\\\"value\\nsecond line\\\" %1$@ %2$@ %@<tagg/>" + " Wrong Formats: %\$s $5s"
    }

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    fun testToPlatformValue() {
        assertEquals(PLATFORM_TEST_STRING, AbsIosStringsResourceFile.toPlatformValue(TEST_STRING))
    }

    @Test
    fun testFromPlatformValue() {
        assertEquals(TEST_STRING, AbsIosStringsResourceFile.fromPlatformValue(PLATFORM_TEST_STRING))
    }

    @Test
    @Throws(IOException::class)
    fun testReadNotExistsFile() {
        val testFile = tempFolder.newFile()
        if (testFile.exists())
            assertTrue(testFile.delete())

        val resourceFile = ResourceFileImpl(testFile, "en")

        assertNull(resourceFile.read(ExtraParams()))
    }

    private inner class ResourceFileImpl(file: File, locale: String) : AbsIosStringsResourceFile(file, locale) {

        override val keyValueLinePattern: String
            get() = ""

        @Throws(IOException::class)
        override fun writeKeyValueString(key: String, value: String) {

        }
    }
}
