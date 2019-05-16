package ru.pocketbyte.locolaser.config.parser

import ru.pocketbyte.locolaser.config.source.EmptySourceConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException

class EmptySourceConfigParser : SourceConfigParser<EmptySourceConfig> {

    @Throws(InvalidConfigException::class)
    override fun parse(sourceObject: Any, throwIfWrongType: Boolean): EmptySourceConfig? {
        if (EmptySourceConfig.TYPE == sourceObject) {
            return EmptySourceConfig()
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Source must equal \"null\"")

        return null
    }
}
