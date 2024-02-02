package ru.pocketbyte.locolaser.kotlinmpp.parser

import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.parser.BaseMobileResourcesConfigParser

@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class KotlinCommonResourcesConfigParser
    : BaseMobileResourcesConfigParser<KotlinCommonResourcesConfigBuilder>() {

    @Throws(InvalidConfigException::class)
    override fun builderByType(
        type: String?, throwIfWrongType: Boolean
    ): KotlinCommonResourcesConfigBuilder? {
        if (KotlinCommonResourcesConfig.TYPE == type)
            return KotlinCommonResourcesConfigBuilder()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
