package ru.pocketbyte.locolaser.kotlinmpp.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.kotlinmpp.*
import ru.pocketbyte.locolaser.mobile.parser.BaseMobileResourcesConfigParser
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class KotlinImplementationResourcesConfigParser
    : BaseMobileResourcesConfigParser<KotlinBaseResourcesConfigBuilder<*>>() {

    companion object {
        const val INTERFACE = "implements"
        const val FORMATTING_TYPE = "formatting_type"
    }

    @Throws(InvalidConfigException::class)
    override fun parseJSONObject(
        platformJSON: JSONObject, throwIfWrongType: Boolean
    ): KotlinBaseResourcesConfigBuilder<*>? {
        val config = super.parseJSONObject(platformJSON, throwIfWrongType)

        if (config != null) {
            JsonParseUtils.getString(platformJSON, INTERFACE, ConfigParser.PLATFORM, false)?.let {
                config.implements = it
            }

            if (config is KotlinBaseCustomFormattingResourceConfigBuilder) {
                config.formattingType = JsonParseUtils.getFormattingType(
                        platformJSON, FORMATTING_TYPE, ConfigParser.PLATFORM, false
                ) ?: NoFormattingType
            }
        }

        return config
    }

    @Throws(InvalidConfigException::class)
    override fun builderByType(
        type: String?, throwIfWrongType: Boolean
    ): KotlinBaseResourcesConfigBuilder<*>? {
        if (KotlinAndroidResourcesConfig.TYPE == type)
            return KotlinAndroidResourcesConfigBuilder()
        if (KotlinIosResourcesConfig.TYPE == type)
            return KotlinIosResourcesConfigBuilder()
        if (KotlinJsResourcesConfig.TYPE == type)
            return KotlinJsResourcesConfigBuilder()
        if (KotlinAbsKeyValueResourcesConfig.TYPE == type)
            return KotlinAbsKeyValueResourcesConfigBuilder()
        if (KotlinAbsStaticResourcesConfig.TYPE == type)
            return KotlinAbsStaticResourcesConfigBuilder()
        if (KotlinAbsProxyResourcesConfig.TYPE == type)
            return KotlinAbsProxyResourcesConfigBuilder()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
