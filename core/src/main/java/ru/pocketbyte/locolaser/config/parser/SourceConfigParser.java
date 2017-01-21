/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser;

import ru.pocketbyte.locolaser.config.source.SourceConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;

/**
 * Helper interface that parse Source config object from JSON.
 *
 * @param <SourceConfigType> Source config class.
 *
 * @author Denis Shurygin
 */
public interface SourceConfigParser<SourceConfigType extends SourceConfig> {
    /**
     * Parse Source from JSON object.
     * @param sourceObject JSON object that contain source config properties.
     * @return Parsed source object.
     * @throws InvalidConfigException
     */
    SourceConfigType parse(Object sourceObject) throws InvalidConfigException;
}
