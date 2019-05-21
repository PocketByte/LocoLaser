package ru.pocketbyte.locolaser.config.parser

import org.junit.Assert.assertSame
import org.junit.Test
import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformConfig
import java.util.*

class PlatformSetConfigParserTest {

    @Test
    @Throws(InvalidConfigException::class)
    fun testParseCorrectData() {
        val config1 = MockPlatformConfig()
        val config2 = MockPlatformConfig()

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
            config1: PlatformConfig = MockPlatformConfig(),
            config2: PlatformConfig = MockPlatformConfig()
    ): PlatformSetConfigParser {
        val parsersSet = LinkedHashSet<PlatformConfigParser<*>>(2)
        parsersSet.add(TestParser("type_1", config1))
        parsersSet.add(TestParser("type_2", config2))

        return PlatformSetConfigParser(parsersSet)
    }

    private inner class TestParser internal constructor(
            private val mType: String, private val mConfig: PlatformConfig
    ) : PlatformConfigParser<PlatformConfig> {

        @Throws(InvalidConfigException::class)
        override fun parse(platformObject: Any?, throwIfWrongType: Boolean): PlatformConfig? {
            if (mType == platformObject)
                return mConfig

            if (throwIfWrongType)
                throw InvalidConfigException("Invalid!")

            return null
        }
    }
}
