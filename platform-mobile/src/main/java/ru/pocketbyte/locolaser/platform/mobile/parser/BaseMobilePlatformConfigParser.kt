package ru.pocketbyte.locolaser.platform.mobile.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.JsonParseUtils

abstract class BaseMobilePlatformConfigParser : PlatformConfigParser<BasePlatformConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
    }

    /**
     * Creates new platform object from it's type.
     * @param type Platform type.
     * @param throwIfWrongType Define that parser should trow exception if object type is not supported.
     * @return Platform object depends on type or null if type is not supported and throwIfWrongType equal false.
     * @throws InvalidConfigException if platform is unknown.
     */
    @Throws(InvalidConfigException::class)
    protected abstract fun platformByType(type: String?, throwIfWrongType: Boolean): BasePlatformConfig?

    @Throws(InvalidConfigException::class)
    override fun parse(platformObject: Any, throwIfWrongType: Boolean): BasePlatformConfig? {

        if (platformObject is String) {
            return parseString(platformObject, throwIfWrongType)
        } else if (platformObject is JSONObject) {
            return parseJSONObject(platformObject, throwIfWrongType)
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    protected fun parseString(type: String, throwIfWrongType: Boolean): BasePlatformConfig? {
        return platformByType(type, throwIfWrongType)
    }

    @Throws(InvalidConfigException::class)
    protected open fun parseJSONObject(platformJSON: JSONObject, throwIfWrongType: Boolean): BasePlatformConfig? {
        val type = JsonParseUtils.getString(platformJSON, PlatformConfigParser.PLATFORM_TYPE, ConfigParser.PLATFORM, true)
        val platform = platformByType(type, throwIfWrongType) ?: return null

        platform.resourceName = JsonParseUtils.getString(
                platformJSON, RESOURCE_NAME, ConfigParser.PLATFORM, false)

        platform.resourcesDir = JsonParseUtils.getFile(
                platformJSON, RESOURCES_DIR, ConfigParser.PLATFORM, false)

        return platform
    }

}
