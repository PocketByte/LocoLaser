/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.parser

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.platform.mobile.AndroidPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.IosPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.IosPlistPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.WpPlatformConfig

/**
 * @author Denis Shurygin
 */
class MobilePlatformConfigParser : BaseMobilePlatformConfigParser() {

    @Throws(InvalidConfigException::class)
    override fun platformByType(type: String?, throwIfWrongType: Boolean): BasePlatformConfig? {
        if (AndroidPlatformConfig.TYPE == type)
            return AndroidPlatformConfig()
        if (IosPlatformConfig.TYPE == type)
            return IosPlatformConfig()
        if (IosPlistPlatformConfig.TYPE == type)
            return IosPlistPlatformConfig()
        if (WpPlatformConfig.TYPE == type)
            return WpPlatformConfig()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
