package ru.pocketbyte.locolaser.platform.kotlinmobile.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinAndroidPlatformConfig
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinBaseImplPlatformConfig
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinIosPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.parser.BaseMobilePlatformConfigParser
import ru.pocketbyte.locolaser.utils.JsonParseUtils

class KotlinImplementationPlatformConfigParser : BaseMobilePlatformConfigParser() {

    companion object {
        const val INTERFACE = "implements"
    }

    @Throws(InvalidConfigException::class)
    override fun parseJSONObject(platformJSON: JSONObject, throwIfWrongType: Boolean): BasePlatformConfig? {
        val config = super.parseJSONObject(platformJSON, throwIfWrongType) as? KotlinBaseImplPlatformConfig

        if (config != null) {
            config.interfaceName = JsonParseUtils
                    .getString(platformJSON, INTERFACE, ConfigParser.PLATFORM, false)
        }

        return config
    }

    @Throws(InvalidConfigException::class)
    override fun platformByType(type: String?, throwIfWrongType: Boolean): BasePlatformConfig? {
        if (KotlinAndroidPlatformConfig.TYPE == type)
            return KotlinAndroidPlatformConfig()
        if (KotlinIosPlatformConfig.TYPE == type)
            return KotlinIosPlatformConfig()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
