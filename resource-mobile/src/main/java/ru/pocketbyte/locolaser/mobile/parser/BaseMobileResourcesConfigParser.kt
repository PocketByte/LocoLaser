package ru.pocketbyte.locolaser.mobile.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

abstract class BaseMobileResourcesConfigParser : ResourcesConfigParser<BaseResourcesConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
        const val FILTER = "filter"
    }

    /**
     * Creates new platform object from it's type.
     * @param type Platform type.
     * @param throwIfWrongType Define that parser should trow exception if object type is not supported.
     * @return Platform object depends on type or null if type is not supported and throwIfWrongType equal false.
     * @throws InvalidConfigException if platform is unknown.
     */
    @Throws(InvalidConfigException::class)
    protected abstract fun platformByType(type: String?, throwIfWrongType: Boolean): BaseResourcesConfig?

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): BaseResourcesConfig? {

        if (resourceObject is String) {
            return parseString(resourceObject, throwIfWrongType)
        } else if (resourceObject is JSONObject) {
            return parseJSONObject(resourceObject, throwIfWrongType)
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    protected fun parseString(type: String, throwIfWrongType: Boolean): BaseResourcesConfig? {
        return platformByType(type, throwIfWrongType)
    }

    @Throws(InvalidConfigException::class)
    protected open fun parseJSONObject(platformJSON: JSONObject, throwIfWrongType: Boolean): BaseResourcesConfig? {
        val type = JsonParseUtils.getString(platformJSON, ResourcesConfigParser.RESOURCE_TYPE, ConfigParser.PLATFORM, true)
        val platform = platformByType(type, throwIfWrongType) ?: return null

        platform.resourceName = JsonParseUtils.getString(
                platformJSON, RESOURCE_NAME, ConfigParser.PLATFORM, false)

        platform.resourcesDir = JsonParseUtils.getFile(
                platformJSON, RESOURCES_DIR, ConfigParser.PLATFORM, false)

        platform.filter = BaseResourcesConfig.regExFilter(
                JsonParseUtils.getString(platformJSON, FILTER, ConfigParser.PLATFORM, false))

        return platform
    }

}
