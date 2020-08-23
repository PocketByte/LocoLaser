package ru.pocketbyte.locolaser.config.parser

import org.junit.Assert.assertSame
import org.junit.Test
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.testutils.mock.MockResourcesConfig
import java.util.*

class ResourcesSetConfigParserTest {

    @Test
    @Throws(InvalidConfigException::class)
    fun testParseCorrectData() {
        val config1 = MockResourcesConfig()
        val config2 = MockResourcesConfig()

        val parser = prepareParser(config1, config2)

        assertSame(config1, parser.parse("type_1", true))
        assertSame(config2, parser.parse("type_2", true))
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testNullForWrongType() {
        assertSame(null, prepareParser().parse("wrong", false))
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testThrowForWrongType() {
        assertSame(null, prepareParser().parse("wrong", true))
    }

    private fun prepareParser(
            config1: ResourcesConfig = MockResourcesConfig(),
            config2: ResourcesConfig = MockResourcesConfig()
    ): ResourcesSetConfigParser {
        val parsersSet = LinkedHashSet<ResourcesConfigParser<*>>(2)
        parsersSet.add(TestParser("type_1", config1))
        parsersSet.add(TestParser("type_2", config2))

        return ResourcesSetConfigParser(parsersSet)
    }

    private inner class TestParser internal constructor(
            private val mType: String, private val mConfig: ResourcesConfig
    ) : ResourcesConfigParser<ResourcesConfig> {

        @Throws(InvalidConfigException::class)
        override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): ResourcesConfig? {
            if (mType == resourceObject)
                return mConfig

            if (throwIfWrongType)
                throw InvalidConfigException("Invalid!")

            return null
        }
    }
}
