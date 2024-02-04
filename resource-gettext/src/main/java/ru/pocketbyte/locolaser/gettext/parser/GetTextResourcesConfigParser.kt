/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.gettext.parser

import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfig
import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.PLATFORM
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfigBuilder
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getString
import java.io.File

/**
 * @author Denis Shurygin
 */
@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class GetTextResourcesConfigParser : ResourcesConfigParser<BaseResourcesConfig> {

    companion object {
        const val RESOURCE_NAME = "res_name"
        const val RESOURCES_DIR = "res_dir"
        const val FILTER = "filter"
    }

    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, workDir: File?, throwIfWrongType: Boolean): BaseResourcesConfig? {

        if (resourceObject is String) {
            if (checkType(resourceObject, throwIfWrongType))
                return GetTextResourcesConfigBuilder().build(workDir)
        } else if (resourceObject is JSONObject) {

            if (checkType(getString(resourceObject, ResourcesConfigParser.RESOURCE_TYPE, PLATFORM, true), throwIfWrongType)) {
                val config = GetTextResourcesConfigBuilder()

                getString(resourceObject, RESOURCE_NAME, PLATFORM, false)?.let {
                    config.resourceName = it
                }

                getString(resourceObject, RESOURCES_DIR, PLATFORM, false)?.let {
                    config.resourcesDir = it
                }

                config.filter = BaseResourcesConfig.regExFilter(
                        getString(resourceObject, FILTER, PLATFORM, false))

                return config.build(workDir)
            }
        }

        if (throwIfWrongType)
            throw InvalidConfigException("Property \"$PLATFORM\" must be a String or JSON object.")

        return null
    }

    @Throws(InvalidConfigException::class)
    private fun checkType(type: String?, throwIfWrongType: Boolean): Boolean {
        if (GetTextResourcesConfig.TYPE != type) {
            if (throwIfWrongType)
                throw InvalidConfigException(
                        "Source type is \"$type\", but expected \"${GetTextResourcesConfig.TYPE}\".")

            return false
        }

        return true
    }
}
