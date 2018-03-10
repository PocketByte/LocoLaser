package ru.pocketbyte.locolaser.config.parser;

import ru.pocketbyte.locolaser.config.source.EmptySourceConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;

public class EmptySourceConfigParser implements SourceConfigParser<EmptySourceConfig> {

    @Override
    public EmptySourceConfig parse(Object sourceObject, boolean throwIfWrongType) throws InvalidConfigException {
        if (EmptySourceConfig.TYPE.equals(sourceObject)) {
            return new EmptySourceConfig();
        }

        if (throwIfWrongType)
            throw new InvalidConfigException("Source must equal \"null\"");

        return null;
    }
}
