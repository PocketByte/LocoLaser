/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import java.io.File;
import java.io.IOException;

/**
 * @author Denis Shurygin
 */
public class IosResourceFile extends AbsIosStringsResourceFile {

    public IosResourceFile(File file, String locale) {
        super(file, locale);
    }

    @Override
    public String getKeyValueLinePattern() {
        return "\"((?:[^\"]|\\\\\")+)\"\\s*=\\s*\"((?:[^\"]|\\\\\")*)\"\\s*;";
    }

    @Override
    protected void writeKeyValueString(String key, String value) throws IOException {
        writeString("\"");
        writeString(key);
        writeString("\" = \"");
        writeString(value);
        writeString("\";");
    }
}