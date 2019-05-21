package ru.pocketbyte.locolaser.config.platform

import org.junit.Test
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformConfig

import java.io.File
import java.util.LinkedHashSet

import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame

class PlatformSetConfigTest {

    @Test
    @Throws(InvalidConfigException::class)
    fun testTempFolder() {
        val testFolder1 = File("./temp/test_folder1/")
        val testFolder2 = File("./temp/test_folder2/")

        val config1 = object : MockPlatformConfig() {
            override val defaultTempDir: File
                get() = testFolder1
        }

        val config2 = object : MockPlatformConfig() {
            override val defaultTempDir: File
                get() = testFolder2
        }

        var configSet = prepareConfig(config1, config2)

        assertSame(testFolder1, configSet.defaultTempDir)
        assertNotSame(testFolder2, configSet.defaultTempDir)

        configSet = prepareConfig(config2, config1)

        assertNotSame(testFolder1, configSet.defaultTempDir)
        assertSame(testFolder2, configSet.defaultTempDir)
    }

    private fun prepareConfig(config1: PlatformConfig, config2: PlatformConfig): PlatformSetConfig {
        val set = LinkedHashSet<PlatformConfig>(2)
        set.add(config1)
        set.add(config2)
        return PlatformSetConfig(set)
    }
}
