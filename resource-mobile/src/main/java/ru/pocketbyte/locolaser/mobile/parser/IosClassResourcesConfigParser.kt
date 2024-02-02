package ru.pocketbyte.locolaser.mobile.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.mobile.IosClassResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.IosObjectiveCResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosObjectiveCResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.IosSwiftResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosSwiftResourcesConfigBuilder
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class IosClassResourcesConfigParser
    : BaseMobileResourcesConfigParser<IosClassResourcesConfigBuilder<*>>() {

    companion object {
        const val TABLE_NAME = "table_name"
    }

    @Throws(InvalidConfigException::class)
    override fun parseJSONObject(
        platformJSON: JSONObject, throwIfWrongType: Boolean
    ): IosClassResourcesConfigBuilder<*>? {
        val config = super.parseJSONObject(platformJSON, throwIfWrongType)

        if (config != null) {
            config.tableName = JsonParseUtils.getString(
                    platformJSON, TABLE_NAME, ConfigParser.PLATFORM, false)
        }

        return config
    }

    @Throws(InvalidConfigException::class)
    override fun builderByType(
        type: String?, throwIfWrongType: Boolean
    ): IosClassResourcesConfigBuilder<*>? {
        if (IosSwiftResourcesConfig.TYPE == type)
            return IosSwiftResourcesConfigBuilder()
        if (IosObjectiveCResourcesConfig.TYPE == type)
            return IosObjectiveCResourcesConfigBuilder()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
