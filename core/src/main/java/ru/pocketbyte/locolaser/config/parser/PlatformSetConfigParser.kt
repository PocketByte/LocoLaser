package ru.pocketbyte.locolaser.config.parser

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.PLATFORM
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser.Companion.PLATFORM_TYPE
import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.config.platform.PlatformSetConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.JsonParseUtils.getString

import java.util.LinkedHashSet

class PlatformSetConfigParser(
        private val mParsers: Set<PlatformConfigParser<*>>
) : PlatformConfigParser<PlatformConfig> {

    @Throws(InvalidConfigException::class)
    override fun parse(platformObject: Any, throwIfWrongType: Boolean): PlatformConfig? {
        if (platformObject is JSONArray) {
            val configs = LinkedHashSet<PlatformConfig>(platformObject.size)

            platformObject.mapNotNullTo(configs) {
                this.parse(it ?: return@mapNotNullTo null, true)
            }

            return PlatformSetConfig(configs)
        } else {
            for (parser in mParsers) {
                val config = parser.parse(platformObject, false)
                if (config != null)
                    return config
            }
        }

        if (throwIfWrongType) {
            when (platformObject) {
                is String -> throw InvalidConfigException("Unknown platform: " + platformObject)
                is JSONObject -> {
                    val type = getString(platformObject, PLATFORM_TYPE, PLATFORM, false)
                    throw InvalidConfigException("Unknown platform: " + type!!)
                }
                else -> throw InvalidConfigException(
                        "Property \"$PLATFORM\" must be a String, JSON object or Array of Strings and JSON objects.")
            }
        }

        return null
    }
}
