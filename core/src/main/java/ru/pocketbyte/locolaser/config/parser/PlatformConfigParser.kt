/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser

import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException

/**
 * Parser interface that parse PlatformConfig object from some object(JSON or String).
 *
 * @author Denis Shurygin
 */
interface PlatformConfigParser<out PlatformConfigType : PlatformConfig> {

    companion object {
        val PLATFORM_TYPE = "type"
    }

    /**
     * Parse Platform object.
     * @param platformObject An object that represent platform object.
     * @param throwIfWrongType Define that parser should trow exception if object type is not supported.
     * @return Parsed platform object or null if object has wrong type and throwIfWrongType equal false.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     */
    @Throws(InvalidConfigException::class)
    fun parse(platformObject: Any?, throwIfWrongType: Boolean): PlatformConfigType?

}
