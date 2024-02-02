/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.parser

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.IosPlistResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosPlistResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.IosResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosResourcesConfigBuilder

/**
 * @author Denis Shurygin
 */
@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class MobileResourcesConfigParser
    : BaseMobileResourcesConfigParser<BaseResourcesConfigBuilder<*>>() {

    @Throws(InvalidConfigException::class)
    override fun builderByType(
        type: String?, throwIfWrongType: Boolean
    ): BaseResourcesConfigBuilder<*>? {
        if (AndroidResourcesConfig.TYPE == type)
            return AndroidResourcesConfigBuilder()
        if (IosResourcesConfig.TYPE == type)
            return IosResourcesConfigBuilder()
        if (IosPlistResourcesConfig.TYPE == type)
            return IosPlistResourcesConfigBuilder()

        if (throwIfWrongType)
            throw InvalidConfigException("Unknown platform: $type")

        return null
    }
}
