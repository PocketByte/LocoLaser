/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.platform.mobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile;

import java.io.File;
import java.io.IOException;

/**
 * Objective-C .m file generator. Part of Objective-C class generation.
 * @author Denis Shurygin
 */
public class IosObjectiveCMResourceFile extends BaseClassResourceFile {

    private static String CLASS_HEADER_TEMPLATE = "#import <%s.h>\r\n"
            + "\r\n"
            + "@implementation %s\r\n";

    private static String CLASS_FOOTER_TEMPLATE = "@end";

    private static String PROPERTY_TEMPLATE =
            "+(NSString*)%s {\r\n" +
            "    return NSLocalizedStringFromTable(@\"%s\", @\"%s\", @\"%s\")\r\n" +
            "}";

    private String mClassName;
    private String mTableName;

    public IosObjectiveCMResourceFile(File file, String className, String tableName) {
        super(file);
        mClassName = className;
        mTableName = tableName;
    }

    @Override
    public ResMap read() {
        return null;
    }

    @Override
    protected void writeHeaderComment(ResMap resMap, WritingConfig writingConfig) throws IOException {
        writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT);
    }

    @Override
    protected void writeClassHeader(ResMap resMap, WritingConfig writingConfig) throws IOException {
        writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName, mClassName));
    }

    @Override
    protected void writeComment(WritingConfig writingConfig, String comment) throws IOException {
        // Do not write comment for .m file
    }

    @Override
    protected void writeProperty(WritingConfig writingConfig, String propertyName, ResItem item) throws IOException {
        ResValue valueOther = item.valueForQuantity(Quantity.OTHER);
        String comment = valueOther != null && valueOther.comment != null ? valueOther.comment : "";

        writeStringLn(String.format(PROPERTY_TEMPLATE, propertyName, item.key, mTableName, escapeString(comment)));
        writeln();
    }

    @Override
    protected void writeClassFooter(ResMap resMap, WritingConfig writingConfig) throws IOException {
        writeString(CLASS_FOOTER_TEMPLATE);
    }

    private static String escapeString(String string) {
        return string
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

}