package ru.pocketbyte.locolaser.config.parser

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser.Companion.RESOURCE_TYPE
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getString
import java.io.File

import java.util.LinkedHashSet

@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class ResourcesSetConfigParser(
    private val mParsers: Set<ResourcesConfigParser<*>>,
    private val hasMainConfig: Boolean = false
) : ResourcesConfigParser<ResourcesConfig> {

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, workDir: File?, throwIfWrongType: Boolean): ResourcesConfig? {
        if (resourceObject is JSONArray) {
            val configs = LinkedHashSet<ResourcesConfig>(resourceObject.size)

            resourceObject.mapNotNullTo(configs) {
                this.parse(it ?: return@mapNotNullTo null, workDir, true)
            }

            return ResourcesSetConfig(configs, if (hasMainConfig) configs.firstOrNull() else null)
        } else {
            mParsers.forEach { parser ->
                val config = parser.parse(resourceObject, workDir, false)
                if (config != null)
                    return config
            }
        }

        if (throwIfWrongType) {
            when (resourceObject) {
                is String -> throw InvalidConfigException("Unknown resource: $resourceObject")
                is JSONObject -> {
                    val type = getString(resourceObject, RESOURCE_TYPE, null, false)
                    throw InvalidConfigException("Unknown resource: " + type!!)
                }
                else -> throw InvalidConfigException(
                        "Resource must be a String, JSON object or Array of Strings and JSON objects.")
            }
        }

        return null
    }
}
