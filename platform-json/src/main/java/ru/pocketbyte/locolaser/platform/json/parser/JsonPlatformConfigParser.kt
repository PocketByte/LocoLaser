/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.json.parser

import ru.pocketbyte.locolaser.platform.json.JsonPlatformConfig
import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.PLATFORM
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getLong
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getObject
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getString

/**
 * @author Denis Shurygin
 */
class JsonPlatformConfigParser : PlatformConfigParser<BasePlatformConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
        const val INDENT = "indent"
        const val FILTER = "filter"
    }

    @Throws(InvalidConfigException::class)
    override fun parse(platformObject: Any?, throwIfWrongType: Boolean): BasePlatformConfig? {

        if (platformObject is String) {
            if (checkType(platformObject, throwIfWrongType))
                return JsonPlatformConfig()
        } else if (platformObject is JSONObject) {

            if (checkType(getString(platformObject, PlatformConfigParser.PLATFORM_TYPE, PLATFORM, true), throwIfWrongType)) {
                val platform = JsonPlatformConfig()

                platform.resourceName = getString(
                        platformObject, RESOURCE_NAME, PLATFORM, false)

                platform.resourcesDir = JsonParseUtils.getFile(
                        platformObject, RESOURCES_DIR, PLATFORM, false)

                platform.filter = BasePlatformConfig.regExFilter(
                        getString(platformObject, FILTER, PLATFORM, false))

                if (platformObject[INDENT] != null) {
                    platform.indent = getLong(platformObject, INDENT, PLATFORM, false).toInt()
                }

                return platform
            }
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"$PLATFORM\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    private fun checkType(type: String?, throwIfWrongType: Boolean): Boolean {
        if (JsonPlatformConfig.TYPE != type) {
            if (throwIfWrongType)
                throw InvalidConfigException(
                        "Source type is \"$type\", but expected \"${JsonPlatformConfig.TYPE}\".")

            return false
        }

        return true
    }
}
