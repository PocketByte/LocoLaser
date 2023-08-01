package ru.pocketbyte.locolaser.properties.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.PLATFORM
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser.Companion.RESOURCE_TYPE
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.properties.PropertiesResourceConfig
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class PropertiesResourceConfigParser : ResourcesConfigParser<BaseResourcesConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
    }

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): BaseResourcesConfig? {

        if (resourceObject is String) {
            if (checkType(resourceObject as String?, throwIfWrongType))
                return PropertiesResourceConfig()
        } else if (resourceObject is JSONObject) {
            val platformJSON = resourceObject as JSONObject?

            if (checkType(JsonParseUtils.getString(platformJSON!!, RESOURCE_TYPE, PLATFORM, true), throwIfWrongType)) {
                val platform = PropertiesResourceConfig()

                JsonParseUtils.getString(platformJSON, RESOURCE_NAME, PLATFORM, false)?.let {
                    platform.resourceName = it
                }

                JsonParseUtils.getString(platformJSON, RESOURCES_DIR, PLATFORM, false)?.let {
                    platform.resourcesDirPath = it
                }

                return platform
            }
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"$PLATFORM\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    private fun checkType(type: String?, throwIfWrongType: Boolean): Boolean {
        if (PropertiesResourceConfig.TYPE != type) {
            if (throwIfWrongType)
                throw InvalidConfigException("Source type is \"$type\", but expected \"${PropertiesResourceConfig.TYPE}\".")

            return false
        }

        return true
    }

}
