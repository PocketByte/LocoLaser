/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.json.parser

import ru.pocketbyte.locolaser.json.JsonResourcesConfig
import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.PLATFORM
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getLong
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getString

/**
 * @author Denis Shurygin
 */
class JsonResourcesConfigParser : ResourcesConfigParser<BaseResourcesConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
        const val INDENT = "indent"
        const val FILTER = "filter"
    }

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): BaseResourcesConfig? {

        if (resourceObject is String) {
            if (checkType(resourceObject, throwIfWrongType))
                return JsonResourcesConfig()
        } else if (resourceObject is JSONObject) {

            if (checkType(getString(resourceObject, ResourcesConfigParser.RESOURCE_TYPE, PLATFORM, true), throwIfWrongType)) {
                val platform = JsonResourcesConfig()

                platform.resourceName = getString(
                        resourceObject, RESOURCE_NAME, PLATFORM, false)

                platform.resourcesDir = JsonParseUtils.getFile(
                        resourceObject, RESOURCES_DIR, PLATFORM, false)

                platform.filter = BaseResourcesConfig.regExFilter(
                        getString(resourceObject, FILTER, PLATFORM, false))

                if (resourceObject[INDENT] != null) {
                    platform.indent = getLong(resourceObject, INDENT, PLATFORM, false).toInt()
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
        if (JsonResourcesConfig.TYPE != type) {
            if (throwIfWrongType)
                throw InvalidConfigException(
                        "Source type is \"$type\", but expected \"${JsonResourcesConfig.TYPE}\".")

            return false
        }

        return true
    }
}
