package ru.pocketbyte.locolaser.kotlinmpp.parser

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfig
import ru.pocketbyte.locolaser.mobile.parser.BaseMobileResourcesConfigParser

class KotlinCommonResourcesConfigParser : BaseMobileResourcesConfigParser() {

    @Throws(InvalidConfigException::class)
    override fun platformByType(type: String?, throwIfWrongType: Boolean): BaseResourcesConfig? {
        if (KotlinCommonResourcesConfig.TYPE == type)
            return KotlinCommonResourcesConfig()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
