/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.parser;

import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.config.parser.BaseTableSourceConfigParser;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.source.google.sheet.GoogleSheetConfig;
import ru.pocketbyte.locolaser.config.parser.ConfigParser;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;
import ru.pocketbyte.locolaser.utils.TextUtils;

import java.net.MalformedURLException;

/**
 * @author Denis Shurygin
 */
public class GoogleSheetConfigParser extends BaseTableSourceConfigParser<GoogleSheetConfig> {

    public static final String SHEET_ID = "id";
    public static final String SHEET_WORKSHEET_TITLE = "worksheet_title";
    public static final String SHEET_CREDENTIAL_FILE = "credential_file";

    protected GoogleSheetConfig sourceByType(String type) throws InvalidConfigException {
        if (type != null && !type.equals(GoogleSheetConfig.TYPE))
            throw new InvalidConfigException("Source type is \"" + type + "\", but expected \"" + GoogleSheetConfig.TYPE + "\".");

        return new GoogleSheetConfig();
    }

    protected void fillFromJSON(GoogleSheetConfig source, JSONObject configJson) throws InvalidConfigException {
        super.fillFromJSON(source, configJson);
        source.setId(JsonParseUtils.getString(configJson, SHEET_ID, ConfigParser.SOURCE, true));

        source.setWorksheetTitle(JsonParseUtils.getString(
                                        configJson, SHEET_WORKSHEET_TITLE, ConfigParser.SOURCE, false));

        source.setCredentialFile(JsonParseUtils.getFile(
                                        configJson, SHEET_CREDENTIAL_FILE, ConfigParser.SOURCE, false));
    }

    @Override
    protected void validate(GoogleSheetConfig source) throws InvalidConfigException {
        super.validate(source);

        if (TextUtils.isEmpty(source.getId()))
            throw new InvalidConfigException("\"" + ConfigParser.SOURCE + "." + SHEET_ID + "\" is not set.");
        try {
            source.getUrl();
        } catch (MalformedURLException e) {
            throw new InvalidConfigException("\"" + ConfigParser.SOURCE + "." + SHEET_ID + "\" is invalid.");
        }
    }
}
