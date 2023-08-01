package ru.pocketbyte.locolaser.config.resources

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.testutils.mock.MockResourceFileProvider

import java.io.File

class BaseResourcesConfigTest {

    @Test
    fun testDefaultProperties() {
        val config = BaseResourcesConfigImpl()

        assertEquals("default_res_name", config.resourceName)
        assertNull(config.resourcesDirPath)
    }

    @Test
    fun testCustomProperties() {
        val config = BaseResourcesConfigImpl()

        config.resourceName = "custom_res_name"
        config.resourcesDirPath = "custom_res_dir"

        assertEquals("custom_res_name", config.resourceName)
        assertEquals("custom_res_dir", config.resourcesDirPath)
    }

    private class BaseResourcesConfigImpl: BaseResourcesConfig() {

        override val type: String = "mock"

        override val defaultTempDirPath: String = "default_temp"
        override val defaultResourcesPath: String = "default_res_path"
        override val defaultResourceName: String= "default_res_name"

        override var resourceFileProvider: ResourceFileProvider = MockResourceFileProvider()

        override val resources: Resources
            get() {
                throw UnsupportedOperationException()
            }
    }

}