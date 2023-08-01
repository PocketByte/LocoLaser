package ru.pocketbyte.locolaser.config.resources

import org.junit.Test
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.testutils.mock.MockResourcesConfig

import java.io.File
import java.util.LinkedHashSet

import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig

class ResourcesSetConfigTest {

    @Test
    @Throws(InvalidConfigException::class)
    fun testTempFolder() {
        val testFolder1 = "./temp/test_folder1/"
        val testFolder2 = "./temp/test_folder2/"

        val config1 = object : MockResourcesConfig() {
            override val defaultTempDirPath: String
                get() = testFolder1
        }

        val config2 = object : MockResourcesConfig() {
            override val defaultTempDirPath: String
                get() = testFolder2
        }

        var configSet = prepareConfig(config1, config2)

        assertSame(testFolder1, configSet.defaultTempDirPath)
        assertNotSame(testFolder2, configSet.defaultTempDirPath)

        configSet = prepareConfig(config2, config1)

        assertNotSame(testFolder1, configSet.defaultTempDirPath)
        assertSame(testFolder2, configSet.defaultTempDirPath)
    }

    private fun prepareConfig(config1: ResourcesConfig, config2: ResourcesConfig): ResourcesSetConfig {
        val set = LinkedHashSet<ResourcesConfig>(2)
        set.add(config1)
        set.add(config2)
        return ResourcesSetConfig(set)
    }
}
