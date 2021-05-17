package ru.pocketbyte.locolaser.kotlinmpp.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.kotlinmpp.*
import ru.pocketbyte.locolaser.mobile.parser.BaseMobileResourcesConfigParser
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

class KotlinImplementationResourcesConfigParser : BaseMobileResourcesConfigParser() {

    companion object {
        const val INTERFACE = "implements"
        const val FORMATTING_TYPE = "formatting_type"
    }

    @Throws(InvalidConfigException::class)
    override fun parseJSONObject(platformJSON: JSONObject, throwIfWrongType: Boolean): BaseResourcesConfig? {
        val config = super.parseJSONObject(platformJSON, throwIfWrongType) as? KotlinBaseImplResourcesConfig

        if (config != null) {
            config.implements = JsonParseUtils
                    .getString(platformJSON, INTERFACE, ConfigParser.PLATFORM, false)

            if (config is KotlinAbsKeyValueResourcesConfig) {
                config.formattingType = JsonParseUtils.getFormattingType(
                        platformJSON, FORMATTING_TYPE, ConfigParser.PLATFORM, false
                ) ?: NoFormattingType
            }
        }

        return config
    }

    @Throws(InvalidConfigException::class)
    override fun platformByType(type: String?, throwIfWrongType: Boolean): BaseResourcesConfig? {
        if (KotlinAndroidResourcesConfig.TYPE == type)
            return KotlinAndroidResourcesConfig()
        if (KotlinIosResourcesConfig.TYPE == type)
            return KotlinIosResourcesConfig()
        if (KotlinJsResourcesConfig.TYPE == type)
            return KotlinJsResourcesConfig()
        if (KotlinAbsKeyValueResourcesConfig.TYPE == type)
            return KotlinAbsKeyValueResourcesConfig()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
