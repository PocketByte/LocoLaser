package ru.pocketbyte.locolaser.ini.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.ini.IniResourcesConfig
import ru.pocketbyte.locolaser.ini.IniResourcesConfigBuilder
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils
import java.io.File

@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class IniResourcesConfigParser : ResourcesConfigParser<BaseResourcesConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
    }

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, workDir: File?, throwIfWrongType: Boolean): BaseResourcesConfig? {

        if (resourceObject is String) {
            if (checkType(resourceObject as String?, throwIfWrongType))
                return IniResourcesConfigBuilder().build(workDir)
        } else if (resourceObject is JSONObject) {
            val platformJSON = resourceObject as JSONObject?

            if (checkType(JsonParseUtils.getString(platformJSON!!, ResourcesConfigParser.RESOURCE_TYPE, ConfigParser.PLATFORM, true), throwIfWrongType)) {
                val platform = IniResourcesConfigBuilder()

                JsonParseUtils.getString(platformJSON, RESOURCE_NAME, ConfigParser.PLATFORM, false)?.let {
                    platform.resourceName = it
                }

                JsonParseUtils.getString(platformJSON, RESOURCES_DIR, ConfigParser.PLATFORM, false)?.let {
                    platform.resourcesDir = it
                }

                return platform.build(workDir)
            }
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    private fun checkType(type: String?, throwIfWrongType: Boolean): Boolean {
        if (IniResourcesConfig.TYPE != type) {
            if (throwIfWrongType)
                throw InvalidConfigException("Source type is \"" + type + "\", but expected \"" + IniResourcesConfig.TYPE + "\".")

            return false
        }

        return true
    }

}
