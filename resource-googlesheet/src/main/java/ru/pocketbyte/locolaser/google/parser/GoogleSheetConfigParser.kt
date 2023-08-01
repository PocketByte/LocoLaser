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
import ru.pocketbyte.locolaser.google.sheet.GoogleSheetConfig
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.getString

/**
 * @author Denis Shurygin
 */
@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
class GoogleSheetConfigParser : BaseTableSourceConfigParser<GoogleSheetConfig>() {

    companion object {
        const val SHEET_ID = "id"
        const val SHEET_WORKSHEET_TITLE = "worksheet_title"
        const val SHEET_CREDENTIAL_FILE = "credential_file"
        const val FORMATTING_TYPE = "formatting_type"
    }

    @Throws(InvalidConfigException::class)
    override fun sourceByType(type: String?, throwIfWrongType: Boolean): GoogleSheetConfig? {
        if (type != null && type != GoogleSheetConfig.TYPE) {
            if (throwIfWrongType) {
                throw InvalidConfigException("Source type is \"$type\", but expected \"${GoogleSheetConfig.TYPE}\".")
            } else {
                return null
            }
        }

        return GoogleSheetConfig()
    }

    @Throws(InvalidConfigException::class)
    override fun fillFromJSON(config: GoogleSheetConfig, configJson: JSONObject) {
        super.fillFromJSON(config, configJson)
        config.id = getString(configJson, SHEET_ID, SOURCE, true)

        config.worksheetTitle = getString(
                configJson, SHEET_WORKSHEET_TITLE, SOURCE, false)

        config.credentialFile = getString(
                configJson, SHEET_CREDENTIAL_FILE, SOURCE, false)

        JsonParseUtils.getFormattingType(
            configJson, FORMATTING_TYPE, ConfigParser.PLATFORM, false
        )?.let {
            config.formattingType = it
        }
    }

    @Throws(InvalidConfigException::class)
    override fun validate(source: GoogleSheetConfig) {
        super.validate(source)

        if (source.id?.isEmpty() != false)
            throw InvalidConfigException("\"$SOURCE.$SHEET_ID\" is not set.")

    }
}
