package ru.pocketbyte.locolaser.config.resources

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.testutils.mock.MockResourceFileProvider

class BaseResourcesConfigTest {

    @Test
    fun testDefaultProperties() {
        val config = BaseResourcesConfigImpl()

        assertEquals("default_res_name", config.resourceName)
        assertNull(config.resourcesDirPath)
    }

    @Test
    fun testCustomProperties() {
        val config = BaseResourcesConfigImpl(
            "custom_res_name",
            "custom_res_dir"
        )

        assertEquals("custom_res_name", config.resourceName)
        assertEquals("custom_res_dir", config.resourcesDirPath)
    }

    private class BaseResourcesConfigImpl(
        resourceName: String? = null,
        resourcesDirPath: String? = null,
    ): BaseResourcesConfig(null, resourceName, resourcesDirPath, MockResourceFileProvider, null) {

        override val type: String = "mock"

        override val defaultResourcesPath: String = "default_res_path"
        override val defaultResourceName: String= "default_res_name"

        override val resources: Resources
            get() {
                throw UnsupportedOperationException()
            }
    }
}
