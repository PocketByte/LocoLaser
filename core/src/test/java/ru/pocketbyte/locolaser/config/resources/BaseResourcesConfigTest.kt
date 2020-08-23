package ru.pocketbyte.locolaser.config.resources

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.pocketbyte.locolaser.resource.Resources

import java.io.File

class BaseResourcesConfigTest {

    @Test
    fun testDefaultProperties() {
        val config = BaseResourcesConfigImpl()

        assertEquals("default_res_name", config.resourceName)
        assertEquals(File("default_res_path").absolutePath, config.resourcesDir?.absolutePath)
    }

    @Test
    fun testCustomProperties() {
        val config = BaseResourcesConfigImpl()

        config.resourceName = "custom_res_name"
        config.resourcesDir = File("custom_res_dir")

        assertEquals("custom_res_name", config.resourceName)
        assertEquals(File("custom_res_dir").absolutePath, config.resourcesDir?.absolutePath)
    }

    private class BaseResourcesConfigImpl: BaseResourcesConfig() {

        override val type: String
            get() = "mock"

        override val defaultTempDirPath: String
            get() = "default_temp"

        override val defaultResourcesPath: String
            get() = "default_res_path"

        override val defaultResourceName: String
            get() = "default_res_name"

        override val resources: Resources
            get() {
                throw UnsupportedOperationException()
            }
    }

}