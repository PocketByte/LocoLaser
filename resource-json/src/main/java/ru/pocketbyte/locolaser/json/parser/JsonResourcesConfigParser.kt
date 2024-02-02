/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.json.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.PLATFORM
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.json.JsonResourcesConfig
import ru.pocketbyte.locolaser.json.JsonResourcesConfigBuilder
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getLong
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getString

/**
 * @author Denis Shurygin
 */
@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class JsonResourcesConfigParser : ResourcesConfigParser<BaseResourcesConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
        const val INDENT = "indent"
        const val FILTER = "filter"
        const val KEY_PLURALIZATION_RULE = "key_pluralization_rule"
    }

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): BaseResourcesConfig? {

        if (resourceObject is String) {
            if (checkType(resourceObject, throwIfWrongType))
                return JsonResourcesConfigBuilder().build()
        } else if (resourceObject is JSONObject) {

            if (checkType(getString(resourceObject, ResourcesConfigParser.RESOURCE_TYPE, PLATFORM, true), throwIfWrongType)) {
                val platform = JsonResourcesConfigBuilder()

                getString(resourceObject, RESOURCE_NAME, PLATFORM, false)?.let {
                    platform.resourceName = it
                }

                getString(resourceObject, RESOURCES_DIR, PLATFORM, false)?.let {
                    platform.resourcesDir = it
                }

                platform.filter = BaseResourcesConfig.regExFilter(
                        getString(resourceObject, FILTER, PLATFORM, false))

                getLong(resourceObject, INDENT, PLATFORM, false)?.let {
                    platform.indent = it.toInt()
                }

                return platform.build()
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
