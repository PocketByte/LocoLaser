package ru.pocketbyte.locolaser.properties.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.PLATFORM
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser.Companion.RESOURCE_TYPE
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.properties.PropertiesResourcesConfig
import ru.pocketbyte.locolaser.properties.PropertiesResourcesConfigBuilder
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils
import java.io.File

@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class PropertiesResourcesConfigParser : ResourcesConfigParser<BaseResourcesConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
    }

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, workDir: File?, throwIfWrongType: Boolean): BaseResourcesConfig? {

        if (resourceObject is String) {
            if (checkType(resourceObject as String?, throwIfWrongType))
                return PropertiesResourcesConfigBuilder().build(workDir)
        } else if (resourceObject is JSONObject) {
            val platformJSON = resourceObject as JSONObject?

            if (checkType(JsonParseUtils.getString(platformJSON!!, RESOURCE_TYPE, PLATFORM, true), throwIfWrongType)) {
                val platform = PropertiesResourcesConfigBuilder()

                JsonParseUtils.getString(platformJSON, RESOURCE_NAME, PLATFORM, false)?.let {
                    platform.resourceName = it
                }

                JsonParseUtils.getString(platformJSON, RESOURCES_DIR, PLATFORM, false)?.let {
                    platform.resourcesDir = it
                }

                return platform.build(workDir)
            }
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"$PLATFORM\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    private fun checkType(type: String?, throwIfWrongType: Boolean): Boolean {
        if (PropertiesResourcesConfig.TYPE != type) {
            if (throwIfWrongType)
                throw InvalidConfigException("Source type is \"$type\", but expected \"${PropertiesResourcesConfig.TYPE}\".")

            return false
        }

        return true
    }

}
