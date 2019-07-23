package ru.pocketbyte.locolaser.platform.ini.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.platform.ini.IniPlatformConfig
import ru.pocketbyte.locolaser.utils.JsonParseUtils

class IniPlatformConfigParser : PlatformConfigParser<BasePlatformConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
    }

    @Throws(InvalidConfigException::class)
    override fun parse(platformObject: Any?, throwIfWrongType: Boolean): BasePlatformConfig? {

        if (platformObject is String) {
            if (checkType(platformObject as String?, throwIfWrongType))
                return IniPlatformConfig()
        } else if (platformObject is JSONObject) {
            val platformJSON = platformObject as JSONObject?

            if (checkType(JsonParseUtils.getString(platformJSON!!, PlatformConfigParser.PLATFORM_TYPE, ConfigParser.PLATFORM, true), throwIfWrongType)) {
                val platform = IniPlatformConfig()

                platform.resourceName = JsonParseUtils.getString(
                        platformJSON, RESOURCE_NAME, ConfigParser.PLATFORM, false)

                platform.resourcesDir = JsonParseUtils.getFile(
                        platformJSON, RESOURCES_DIR, ConfigParser.PLATFORM, false)

                return platform
            }
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    private fun checkType(type: String?, throwIfWrongType: Boolean): Boolean {
        if (IniPlatformConfig.TYPE != type) {
            if (throwIfWrongType)
                throw InvalidConfigException("Source type is \"" + type + "\", but expected \"" + IniPlatformConfig.TYPE + "\".")

            return false
        }

        return true
    }

}
