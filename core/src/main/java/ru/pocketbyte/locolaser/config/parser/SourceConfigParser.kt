/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser

import ru.pocketbyte.locolaser.config.source.SourceConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException

/**
 * Helper interface that parse Source config object from JSON.
 *
 * @param <SourceConfigType> Source config class.
 *
 * @author Denis Shurygin
</SourceConfigType> */
interface SourceConfigParser<out SourceConfigType : SourceConfig> {

    companion object {
        val SOURCE_TYPE = "type"
    }

    /**
     * Parse Source from JSON object.
     * @param sourceObject JSON object that contain source config properties.
     * @return Parsed source object.
     * @throws InvalidConfigException
     */
    @Throws(InvalidConfigException::class)
    fun parse(sourceObject: Any?, throwIfWrongType: Boolean): SourceConfigType?

}
