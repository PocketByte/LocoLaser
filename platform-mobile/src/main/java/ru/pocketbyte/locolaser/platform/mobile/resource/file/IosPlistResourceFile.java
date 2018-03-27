package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import java.io.File;
import java.io.IOException;

public class IosPlistResourceFile extends AbsIosStringsResourceFile {

    public IosPlistResourceFile(File file, String locale) {
        super(file, locale);
    }

    @Override
    protected String getKeyValueLinePattern() {
        return "[{26}]((?:[^\"]||\\\\\")+?)\\s*=\\s*\"((?:[^\"]|\\\\\")*)\"\\s*;";
    }

    @Override
    protected void writeKeyValueString(String key, String value) throws IOException {
        writeString(key);
        writeString(" = \"");
        writeString(value);
        writeString("\";");
    }
}