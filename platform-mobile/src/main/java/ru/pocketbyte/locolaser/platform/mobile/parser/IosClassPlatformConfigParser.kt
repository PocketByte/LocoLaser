package ru.pocketbyte.locolaser.platform.mobile.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.platform.mobile.IosBaseClassPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.IosObjectiveCPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.IosSwiftPlatformConfig
import ru.pocketbyte.locolaser.utils.JsonParseUtils

class IosClassPlatformConfigParser : BaseMobilePlatformConfigParser() {

    companion object {
        const val TABLE_NAME = "table_name"
    }

    @Throws(InvalidConfigException::class)
    override fun parseJSONObject(platformJSON: JSONObject, throwIfWrongType: Boolean): BasePlatformConfig? {
        val config = super.parseJSONObject(platformJSON, throwIfWrongType) as IosBaseClassPlatformConfig?

        if (config != null) {

            config.tableName = JsonParseUtils.getString(
                    platformJSON, TABLE_NAME, ConfigParser.PLATFORM, false)
        }

        return config
    }

    @Throws(InvalidConfigException::class)
    override fun platformByType(type: String?, throwIfWrongType: Boolean): BasePlatformConfig? {
        if (IosSwiftPlatformConfig.TYPE == type)
            return IosSwiftPlatformConfig()
        if (IosObjectiveCPlatformConfig.TYPE == type)
            return IosObjectiveCPlatformConfig()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
