package ru.pocketbyte.locolaser.resource

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformResources
import ru.pocketbyte.locolaser.testutils.mock.MockResourceFile

import java.io.File
import java.io.IOException
import java.util.*

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

class PlatformSetResourcesTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private var resFolder: File? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        resFolder = tempFolder.newFolder()
    }

    @Test
    fun testRead() {
        val resources = prepareResources(
                MockResourceFile(prepareResMap1()),
                MockResourceFile(prepareResMap2())
        )

        val expected = prepareResMap1().merge(prepareResMap2())

        assertEquals(expected, resources.read(allLocales))
    }

    @Test
    fun testReadWrongDirection() {
        val resources = prepareResources(
                MockResourceFile(prepareResMap1()),
                MockResourceFile(prepareResMap2())
        )

        val expected = prepareResMap2().merge(prepareResMap1())

        assertNotEquals(expected, resources.read(allLocales))
    }

    @Test
    @Throws(IOException::class)
    fun testWrite() {
        val file1 = MockResourceFile(prepareResMap1())
        val file2 = MockResourceFile(prepareResMap1())

        val resources = prepareResources(file1, file2)

        val expected = prepareResMap2()

        resources.write(expected, null)

        assertEquals(expected, file1.map)
        assertEquals(expected, file2.map)
    }

    private fun prepareResources(res1: ResourceFile, res2: ResourceFile): PlatformSetResources {
        val set = LinkedHashSet<PlatformResources>(2)

        set.add(object : MockPlatformResources(resFolder!!, "resource1") {
            override fun getResourceFiles(locales: Set<String>): Array<ResourceFile>? {
                return arrayOf(res1)
            }
        })

        set.add(object : MockPlatformResources(resFolder!!, "resource2") {
            override fun getResourceFiles(locales: Set<String>): Array<ResourceFile>? {
                return arrayOf(res2)
            }
        })

        return PlatformSetResources(set)
    }

    private val allLocales: Set<String>
        get() = HashSet(Arrays.asList("locale1", "locale2", "locale3", "locale4"))

    private fun prepareResMap1(): ResMap {
        val resMap = ResMap()
        resMap.put("locale1", prepareResLocale1())
        resMap.put("locale2", prepareResLocale2())
        resMap.put("locale3", prepareResLocale3())
        return resMap
    }

    private fun prepareResMap2(): ResMap {
        val resMap = ResMap()
        resMap.put("locale1", prepareResLocale2())
        resMap.put("locale2", prepareResLocale3())
        resMap.put("locale4", prepareResLocale1())
        return resMap
    }

    private fun prepareResLocale1(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", null, Quantity.OTHER), ResValue("value2_1", null, Quantity.MANY), ResValue("value2_1", null, Quantity.FEW))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value1_1", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value1_1", null, Quantity.OTHER), ResValue("value2_1", null, Quantity.MANY), ResValue("value3_1", "Comment", Quantity.ZERO))))
        return resLocale
    }

    private fun prepareResLocale2(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER), ResValue("value2_2", null, Quantity.MANY), ResValue("value3_2", "Comment", Quantity.TWO))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key4", arrayOf(ResValue("value1_2", null, Quantity.OTHER), ResValue("value2_2", null, Quantity.MANY), ResValue("value3_2", "Comment", Quantity.ZERO))))
        return resLocale
    }

    private fun prepareResLocale3(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_3", null, Quantity.OTHER), ResValue("value2_3", null, Quantity.FEW), ResValue("value3_3", "Comment", Quantity.TWO), ResValue("value4_3", "Comment", Quantity.ZERO))))
        resLocale.put(prepareResItem("key4", arrayOf(ResValue("value1_3", null, Quantity.OTHER), ResValue("value3_3", "Comment", Quantity.ZERO))))
        return resLocale
    }

    private fun prepareResItem(key: String, values: Array<ResValue>): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }
}
