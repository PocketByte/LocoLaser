package ru.pocketbyte.locolaser.config.parser

import org.junit.Assert.assertSame
import org.junit.Test
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.testutils.mock.MockResourcesConfig
import java.io.File
import java.util.*

class ResourcesSetConfigParserTest {

    @Test
    @Throws(InvalidConfigException::class)
    fun testParseCorrectData() {
        val config1 = MockResourcesConfig()
        val config2 = MockResourcesConfig()

        val parser = prepareParser(config1, config2)

        assertSame(config1, parser.parse("type_1", null, true))
        assertSame(config2, parser.parse("type_2", null, true))
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testNullForWrongType() {
        assertSame(null, prepareParser().parse("wrong", null, false))
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testThrowForWrongType() {
        assertSame(null, prepareParser().parse("wrong", null, true))
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
        override fun parse(
            resourceObject: Any?,
            workDir: File?,
            throwIfWrongType: Boolean
        ): ResourcesConfig? {
            if (mType == resourceObject)
                return mConfig

            if (throwIfWrongType)
                throw InvalidConfigException("Invalid!")

            return null
        }
    }
}
