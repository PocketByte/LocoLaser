/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.BaseTableSourceConfigParser
import ru.pocketbyte.locolaser.config.parser.ConfigParser.SOURCE
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.source.google.sheet.GoogleSheetConfig
import ru.pocketbyte.locolaser.utils.JsonParseUtils
import ru.pocketbyte.locolaser.utils.TextUtils
import java.net.MalformedURLException

/**
 * @author Denis Shurygin
 */
class GoogleSheetConfigParser : BaseTableSourceConfigParser<GoogleSheetConfig>() {

    companion object {
        const val SHEET_ID = "id"
        const val SHEET_WORKSHEET_TITLE = "worksheet_title"
        const val SHEET_CREDENTIAL_FILE = "credential_file"
    }

    @Throws(InvalidConfigException::class)
    override fun sourceByType(type: String?): GoogleSheetConfig {
        if (type != null && type != GoogleSheetConfig.TYPE)
            throw InvalidConfigException("Source type is \"$type\", but expected \"${GoogleSheetConfig.TYPE}\".")

        return GoogleSheetConfig()
    }

    @Throws(InvalidConfigException::class)
    override fun fillFromJSON(source: GoogleSheetConfig, configJson: JSONObject) {
        super.fillFromJSON(source, configJson)
        source.id = JsonParseUtils.getString(configJson, SHEET_ID, SOURCE, true)

        source.worksheetTitle = JsonParseUtils.getString(
                configJson, SHEET_WORKSHEET_TITLE, SOURCE, false)

        source.credentialFile = JsonParseUtils.getFile(
                configJson, SHEET_CREDENTIAL_FILE, SOURCE, false)
    }

    @Throws(InvalidConfigException::class)
    override fun validate(source: GoogleSheetConfig) {
        super.validate(source)

        if (TextUtils.isEmpty(source.id))
            throw InvalidConfigException("\"$SOURCE.$SHEET_ID\" is not set.")
        try {
            source.url
        } catch (e: MalformedURLException) {
            throw InvalidConfigException("\"$SOURCE.$SHEET_ID\" is invalid.")
        }

    }
}
