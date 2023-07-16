/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser

import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException

/**
 * Parser interface that parse ResourcesConfig object from some object(JSON or String).
 *
 * @author Denis Shurygin
 */
interface ResourcesConfigParser<out ConfigType : ResourcesConfig> {

    companion object {
        const val RESOURCE_TYPE = "type"
    }

    /**
     * Parse Resource Config.
     * @param resourceObject An object that represent Resource Config object.
     * @param throwIfWrongType Defines that parser should trow exception if object type is not supported.
     * @return Parsed Config object or null if object has wrong type and throwIfWrongType equal false.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     */
    @Throws(InvalidConfigException::class)
    fun parse(resourceObject: Any?, throwIfWrongType: Boolean): ConfigType?

}
