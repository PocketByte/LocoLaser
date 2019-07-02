package ru.pocketbyte.locolaser.platform.json.resource

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.resource.entity.*

import java.io.IOException

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import ru.pocketbyte.locolaser.platform.json.resource.JsonResources

class JsonResourcesTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testWriteAndRead() {
        val resMap1 = prepareResMap()

        val resources = JsonResources(tempFolder.newFolder(), "test")
        resources.write(resMap1, null)

        val resMap2 = resources.read(resMap1.keys)

        assertNotNull(resMap2)
        assertEquals(resMap1, resMap2)
    }

    //FIXME Map without plurals. Add plural items when platform start support it
    private fun prepareResMap(): ResMap {
        val resMap = ResMap()
        resMap["locale1"] = prepareResLocale1()
        resMap["locale2"] = prepareResLocale2()
        resMap["locale3"] = prepareResLocale3()
        return resMap
    }

    private fun prepareResLocale1(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value2_1", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value3_1", null, Quantity.OTHER))))
        return resLocale
    }

    private fun prepareResLocale2(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value3_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key4", arrayOf(ResValue("value4_2", null, Quantity.OTHER))))
        return resLocale
    }

    private fun prepareResLocale3(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_3", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key4", arrayOf(ResValue("value4_3", null, Quantity.OTHER))))
        return resLocale
    }

    private fun prepareResItem(key: String, values: Array<ResValue>): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }
}
