package ru.pocketbyte.locolaser.config.parser

import ru.pocketbyte.locolaser.config.resources.EmptyResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException

class EmptySourceConfigParser : ResourcesConfigParser<EmptyResourcesConfig> {

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): EmptyResourcesConfig? {
        if (EmptyResourcesConfig.TYPE == resourceObject) {
            return EmptyResourcesConfig()
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Source must equal \"null\"")

        return null
    }
}
