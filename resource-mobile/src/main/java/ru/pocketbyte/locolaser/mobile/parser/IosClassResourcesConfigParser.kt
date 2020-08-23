package ru.pocketbyte.locolaser.mobile.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.mobile.IosBaseClassResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosObjectiveCResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosSwiftResourcesConfig
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

class IosClassResourcesConfigParser : BaseMobileResourcesConfigParser() {

    companion object {
        const val TABLE_NAME = "table_name"
    }

    @Throws(InvalidConfigException::class)
    override fun parseJSONObject(platformJSON: JSONObject, throwIfWrongType: Boolean): BaseResourcesConfig? {
        val config = super.parseJSONObject(platformJSON, throwIfWrongType) as IosBaseClassResourcesConfig?

        if (config != null) {

            config.tableName = JsonParseUtils.getString(
                    platformJSON, TABLE_NAME, ConfigParser.PLATFORM, false)
        }

        return config
    }

    @Throws(InvalidConfigException::class)
    override fun platformByType(type: String?, throwIfWrongType: Boolean): BaseResourcesConfig? {
        if (IosSwiftResourcesConfig.TYPE == type)
            return IosSwiftResourcesConfig()
        if (IosObjectiveCResourcesConfig.TYPE == type)
            return IosObjectiveCResourcesConfig()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
