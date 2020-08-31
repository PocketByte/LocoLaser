package ru.pocketbyte.locolaser.platform.ini.parser

import org.json.simple.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.platform.ini.IniPlatformConfig
import java.io.File
import java.io.IOException
import java.util.*

class IniPlatformConfigParserTest {

    @JvmField @Rule
    var tempFolder = TemporaryFolder()

    private var parser: IniPlatformConfigParser? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = IniPlatformConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromString() {
        val config = parser!!.parse(IniPlatformConfig.TYPE, true)

        assertNotNull(config)
        assertEquals(IniPlatformConfig::class.java, config!!.javaClass)
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
        assertEquals(IniPlatformConfig::class.java, config!!.javaClass)

        assertEquals("test_res", config.resourceName)
        assertEquals(File("test_res_dir").canonicalPath,
                config.resourcesDir!!.canonicalPath)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJsonNoType() {
        val json = prepareTestPlatformJson()
        json.remove(ResourcesConfigParser.RESOURCE_TYPE)

        parser!!.parse(json, true)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromJsonOnlyType() {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = IniPlatformConfig.TYPE
        val config = parser!!.parse(json, true)

        assertNotNull(config)
        assertEquals(IniPlatformConfig::class.java, config!!.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromInvalidClass() {
        parser!!.parse(ArrayList<String>(), true)
    }

    private fun prepareTestPlatformJson(): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = IniPlatformConfig.TYPE
        json[IniPlatformConfigParser.RESOURCE_NAME] = "test_res"
        json[IniPlatformConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}
