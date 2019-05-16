package ru.pocketbyte.locolaser.platform.kotlinmobile.parser

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinCommonPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.parser.BaseMobilePlatformConfigParser

class KotlinCommonPlatformConfigParser : BaseMobilePlatformConfigParser() {

    @Throws(InvalidConfigException::class)
    override fun platformByType(type: String?, throwIfWrongType: Boolean): BasePlatformConfig? {
        if (KotlinCommonPlatformConfig.TYPE == type)
            return KotlinCommonPlatformConfig()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
