package ru.pocketbyte.locolaser.platform.gettext.parser

import org.json.simple.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException

import java.io.File
import java.io.IOException
import java.util.ArrayList

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.gettext.GetTextPlatformConfig
import ru.pocketbyte.locolaser.resource.AbsPlatformResources

class GetTextPlatformConfigParserTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private var parser: GetTextPlatformConfigParser? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = GetTextPlatformConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromString() {
        val config = parser!!.parse(GetTextPlatformConfig.TYPE, true)

        assertNotNull(config)
        assertEquals(GetTextPlatformConfig::class.java, config!!.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testUnknownFromString() {
        parser!!.parse("invalid_platform", true)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJson() {
        val config = parser!!.parse(prepareTestPlatformJson(), true)

        assertNotNull(config)
        assertEquals(GetTextPlatformConfig::class.java, config!!.javaClass)

        assertNull(config.filter)
        assertEquals("test_res", config.resourceName)
        assertEquals(File("test_res_dir").canonicalPath,
                config.resourcesDir!!.canonicalPath)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJsonNoType() {
        val json = prepareTestPlatformJson()
        json.remove(PlatformConfigParser.PLATFORM_TYPE)

        parser!!.parse(json, true)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromJsonOnlyType() {
        val json = JSONObject()
        json[PlatformConfigParser.PLATFORM_TYPE] = GetTextPlatformConfig.TYPE
        val config = parser!!.parse(json, true)

        assertNotNull(config)
        assertEquals(GetTextPlatformConfig::class.java, config!!.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromInvalidClass() {
        parser!!.parse(ArrayList<String>(), true)
    }

    @Test
    fun testConfigResources() {
        val json = prepareTestPlatformJson()
        val resources = (parser?.parse(json, true) as GetTextPlatformConfig).resources as AbsPlatformResources

        assertEquals("test_res", resources.name)
        assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFilter() {
        val json = prepareTestPlatformJson().apply {
            this[GetTextPlatformConfigParser.FILTER] = "test_filter"
        }

        assertNotNull(parser!!.parse(json, true)?.filter)
    }

    private fun prepareTestPlatformJson(): JSONObject {
        val json = JSONObject()
        json[PlatformConfigParser.PLATFORM_TYPE] = GetTextPlatformConfig.TYPE
        json[GetTextPlatformConfigParser.RESOURCE_NAME] = "test_res"
        json[GetTextPlatformConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}
