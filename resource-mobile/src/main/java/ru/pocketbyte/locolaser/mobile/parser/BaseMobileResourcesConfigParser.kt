package ru.pocketbyte.locolaser.mobile.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.PLATFORM
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser.Companion.RESOURCE_TYPE
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils
import java.io.File

@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
abstract class BaseMobileResourcesConfigParser<ConfigBuilder : BaseResourcesConfigBuilder<*>>
    : ResourcesConfigParser<BaseResourcesConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
        const val FILTER = "filter"
    }

    /**
     * Creates new platform object from it's type.
     * @param type Platform type.
     * @param throwIfWrongType Defines that parser should trow exception if object type is not supported.
     * @return Platform object depends on type or null if type is not supported and throwIfWrongType equal false.
     * @throws InvalidConfigException if platform is unknown.
     */
    @Throws(InvalidConfigException::class)
    protected abstract fun builderByType(type: String?, throwIfWrongType: Boolean): ConfigBuilder?

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, workDir: File?, throwIfWrongType: Boolean): BaseResourcesConfig? {

        if (resourceObject is String) {
            return parseString(resourceObject, workDir, throwIfWrongType)
        } else if (resourceObject is JSONObject) {
            return parseJSONObject(resourceObject, throwIfWrongType)?.build(workDir)
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"$PLATFORM\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    protected fun parseString(type: String, workDir: File?, throwIfWrongType: Boolean): BaseResourcesConfig? {
        return builderByType(type, throwIfWrongType)?.build(workDir)
    }

    @Throws(InvalidConfigException::class)
    protected open fun parseJSONObject(platformJSON: JSONObject, throwIfWrongType: Boolean): ConfigBuilder? {
        val type = JsonParseUtils.getString(platformJSON, RESOURCE_TYPE, PLATFORM, true)
        val platform = builderByType(type, throwIfWrongType) ?: return null

        JsonParseUtils.getString(platformJSON, RESOURCE_NAME, PLATFORM, false)?.let {
            platform.resourceName = it
        }

        JsonParseUtils.getString(platformJSON, RESOURCES_DIR, PLATFORM, false)?.let {
            platform.resourcesDir = it
        }

        platform.filter = BaseResourcesConfig.regExFilter(
                JsonParseUtils.getString(platformJSON, FILTER, PLATFORM, false))

        return platform
    }

}
