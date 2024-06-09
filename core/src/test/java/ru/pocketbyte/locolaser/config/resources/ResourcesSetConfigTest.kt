package ru.pocketbyte.locolaser.config.resources

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.resource.ResourcesSet
import ru.pocketbyte.locolaser.testutils.mock.MockResources
import ru.pocketbyte.locolaser.testutils.mock.MockResourcesConfig
import java.io.File

class ResourcesSetConfigTest {

    @Test
    @Throws(InvalidConfigException::class)
    fun testResources() {
        val resources1 = MockResources(File("./"), "mock1", null)
        val resources2 = MockResources(File("./"), "mock2", null)
        val config1 = object : MockResourcesConfig() {
            override val resources = resources1
        }
        val config2 = object : MockResourcesConfig() {
            override val resources = resources2
        }

        assertEquals(
            ResourcesSet(setOf(resources1, resources2), null),
            prepareConfig(config1, config2).resources
        )

        assertEquals(
            ResourcesSet(setOf(resources1, resources2), null),
            prepareConfig(config2, config1).resources
        )
    }

    private fun prepareConfig(config1: ResourcesConfig, config2: ResourcesConfig): ResourcesSetConfig {
        val set = LinkedHashSet<ResourcesConfig>(2)
        set.add(config1)
        set.add(config2)
        return ResourcesSetConfig(set)
    }
}
