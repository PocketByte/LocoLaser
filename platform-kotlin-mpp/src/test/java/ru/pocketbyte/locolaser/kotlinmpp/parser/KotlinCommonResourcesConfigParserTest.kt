package ru.pocketbyte.locolaser.kotlinmpp.parser

import org.json.simple.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfig
import ru.pocketbyte.locolaser.mobile.parser.BaseMobileResourcesConfigParser
import ru.pocketbyte.locolaser.resource.AbsResources
import java.io.File
import java.io.IOException

class KotlinCommonResourcesConfigParserTest {

    @Rule
    @JvmField
    var tempFolder = TemporaryFolder()

    private var parser: KotlinCommonResourcesConfigParser? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = KotlinCommonResourcesConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    fun testConfigResources() {
        val json = prepareTestPlatformJson()
        val resources = (parser?.parse(json, true) as ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfig).resources as AbsResources

        Assert.assertEquals("com.test_res", resources.name)
        Assert.assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    private fun prepareTestPlatformJson(): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfig.TYPE
        json[BaseMobileResourcesConfigParser.RESOURCE_NAME] = "com.test_res"
        json[BaseMobileResourcesConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}