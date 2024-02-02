/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.google.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.BaseTableSourceConfigParser
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.SOURCE
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfigBuilder
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getString

/**
 * @author Denis Shurygin
 */
@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class GoogleSheetConfigParser : BaseTableSourceConfigParser<GoogleSheetResourcesConfigBuilder>() {

    companion object {
        const val SHEET_ID = "id"
        const val SHEET_WORKSHEET_TITLE = "worksheet_title"
        const val SHEET_CREDENTIAL_FILE = "credential_file"
        const val FORMATTING_TYPE = "formatting_type"
    }

    @Throws(InvalidConfigException::class)
    override fun builderByType(type: String?, throwIfWrongType: Boolean): GoogleSheetResourcesConfigBuilder? {
        if (type != null && type != GoogleSheetResourcesConfig.TYPE) {
            if (throwIfWrongType) {
                throw InvalidConfigException("Source type is \"$type\", but expected \"${GoogleSheetResourcesConfig.TYPE}\".")
            } else {
                return null
            }
        }

        return GoogleSheetResourcesConfigBuilder()
    }

    @Throws(InvalidConfigException::class)
    override fun fillFromJSON(builder: GoogleSheetResourcesConfigBuilder, configJson: JSONObject) {
        super.fillFromJSON(builder, configJson)
        builder.id = getString(configJson, SHEET_ID, SOURCE, true)

        builder.worksheetTitle = getString(
                configJson, SHEET_WORKSHEET_TITLE, SOURCE, false)

        builder.credentialFile = getString(
                configJson, SHEET_CREDENTIAL_FILE, SOURCE, false)

        JsonParseUtils.getFormattingType(
            configJson, FORMATTING_TYPE, ConfigParser.PLATFORM, false
        )?.let {
            builder.formattingType = it
        }
    }

    @Throws(InvalidConfigException::class)
    override fun validate(builder: GoogleSheetResourcesConfigBuilder) {
        super.validate(builder)

        if (builder.id?.isEmpty() != false)
            throw InvalidConfigException("\"$SOURCE.$SHEET_ID\" is not set.")

    }
}
