package ru.pocketbyte.locolaser.config.parser

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.SOURCE
import ru.pocketbyte.locolaser.config.source.SourceConfig
import ru.pocketbyte.locolaser.config.source.SourceSetConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getString

import java.util.LinkedHashSet


class SourceSetConfigParser(
        private val mParsers: Set<SourceConfigParser<*>>
) : SourceConfigParser<SourceConfig> {

    @Throws(InvalidConfigException::class)
    override fun parse(sourceObject: Any?, throwIfWrongType: Boolean): SourceConfig? {
        if (sourceObject is JSONArray) {

            val configs = LinkedHashSet<SourceConfig>(sourceObject.size)

            sourceObject.mapNotNullTo(configs) {
                this.parse(it ?: return@mapNotNullTo null, true)
            }
            return SourceSetConfig(configs, configs.firstOrNull())
        } else {
            mParsers.forEach { parser ->
                val config = parser.parse(sourceObject, false)
                if (config != null)
                    return config
            }
        }

        if (throwIfWrongType) {
            when (sourceObject) {
                is String -> throw InvalidConfigException("Unknown source: $sourceObject")
                is JSONObject -> {
                    val type = getString(sourceObject, SourceConfigParser.SOURCE_TYPE, SOURCE, false)
                    throw InvalidConfigException("Unknown source: ${type!!}")
                }
                else -> throw InvalidConfigException(
                        "Property \"$SOURCE\" must be a String, JSON object or Array of Strings and JSON objects.")
            }
        }

        return null
    }
}
