/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.parser

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosPlistResourcesConfig

/**
 * @author Denis Shurygin
 */
class MobileResourcesConfigParser : BaseMobileResourcesConfigParser() {

    @Throws(InvalidConfigException::class)
    override fun platformByType(type: String?, throwIfWrongType: Boolean): BaseResourcesConfig? {
        if (AndroidResourcesConfig.TYPE == type)
            return AndroidResourcesConfig()
        if (IosResourcesConfig.TYPE == type)
            return IosResourcesConfig()
        if (IosPlistResourcesConfig.TYPE == type)
            return IosPlistResourcesConfig()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
