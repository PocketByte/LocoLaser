package ru.pocketbyte.locolaser.config.platform

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.pocketbyte.locolaser.resource.PlatformResources

import java.io.File

class BasePlatformConfigTest {

    @Test
    fun testDefaultProperties() {
        val config = BasePlatformConfigImpl()

        assertEquals("default_res_name", config.resourceName)
        assertEquals(File("default_res_path").absolutePath, config.resourcesDir?.absolutePath)
    }

    @Test
    fun testCustomProperties() {
        val config = BasePlatformConfigImpl()

        config.resourceName = "custom_res_name"
        config.resourcesDir = File("custom_res_dir")

        assertEquals("custom_res_name", config.resourceName)
        assertEquals(File("custom_res_dir").absolutePath, config.resourcesDir?.absolutePath)
    }

    private class BasePlatformConfigImpl: BasePlatformConfig() {

        override val type: String
            get() = "mock"

        override val defaultTempDirPath: String
            get() = "default_temp"

        override val defaultResourcesPath: String
            get() = "default_res_path"

        override val defaultResourceName: String
            get() = "default_res_name"

        override val resources: PlatformResources
            get() {
                throw UnsupportedOperationException()
            }
    }

}