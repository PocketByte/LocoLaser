/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.gettext.parser

import ru.pocketbyte.locolaser.platform.gettext.GetTextPlatformConfig
import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.*
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.JsonParseUtils
import ru.pocketbyte.locolaser.utils.JsonParseUtils.getString

/**
 * @author Denis Shurygin
 */
class GetTextPlatformConfigParser : PlatformConfigParser<BasePlatformConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
    }

    @Throws(InvalidConfigException::class)
    override fun parse(platformObject: Any, throwIfWrongType: Boolean): BasePlatformConfig? {

        if (platformObject is String) {
            if (checkType(platformObject, throwIfWrongType))
                return GetTextPlatformConfig()
        } else if (platformObject is JSONObject) {

            if (checkType(getString(platformObject, PlatformConfigParser.PLATFORM_TYPE, PLATFORM, true), throwIfWrongType)) {
                val platform = GetTextPlatformConfig()

                platform.resourceName = getString(
                        platformObject, RESOURCE_NAME, PLATFORM, false)

                platform.resourcesDir = JsonParseUtils.getFile(
                        platformObject, RESOURCES_DIR, PLATFORM, false)

                return platform
            }
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"$PLATFORM\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    private fun checkType(type: String?, throwIfWrongType: Boolean): Boolean {
        if (GetTextPlatformConfig.TYPE != type) {
            if (throwIfWrongType)
                throw InvalidConfigException(
                        "Source type is \"$type\", but expected \"${GetTextPlatformConfig.TYPE}\".")

            return false
        }

        return true
    }
}
