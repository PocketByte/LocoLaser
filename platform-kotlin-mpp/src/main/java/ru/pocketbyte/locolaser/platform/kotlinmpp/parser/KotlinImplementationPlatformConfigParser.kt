package ru.pocketbyte.locolaser.platform.kotlinmpp.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.platform.kotlinmpp.*
import ru.pocketbyte.locolaser.platform.mobile.parser.BaseMobilePlatformConfigParser
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

class KotlinImplementationPlatformConfigParser : BaseMobilePlatformConfigParser() {

    companion object {
        const val INTERFACE = "implements"
        const val FORMATTING_TYPE = "formatting_type"
    }

    @Throws(InvalidConfigException::class)
    override fun parseJSONObject(platformJSON: JSONObject, throwIfWrongType: Boolean): BasePlatformConfig? {
        val config = super.parseJSONObject(platformJSON, throwIfWrongType) as? KotlinBaseImplPlatformConfig

        if (config != null) {
            config.interfaceName = JsonParseUtils
                    .getString(platformJSON, INTERFACE, ConfigParser.PLATFORM, false)

            if (config is KotlinAbsKeyValuePlatformConfig) {
                config.formattingType = JsonParseUtils.getFormattingType(
                        platformJSON, FORMATTING_TYPE, ConfigParser.PLATFORM, false
                ) ?: NoFormattingType
            }
        }

        return config
    }

    @Throws(InvalidConfigException::class)
    override fun platformByType(type: String?, throwIfWrongType: Boolean): BasePlatformConfig? {
        if (KotlinAndroidPlatformConfig.TYPE == type)
            return KotlinAndroidPlatformConfig()
        if (KotlinIosPlatformConfig.TYPE == type)
            return KotlinIosPlatformConfig()
        if (KotlinJsPlatformConfig.TYPE == type)
            return KotlinJsPlatformConfig()
        if (KotlinAbsKeyValuePlatformConfig.TYPE == type)
            return KotlinAbsKeyValuePlatformConfig()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
