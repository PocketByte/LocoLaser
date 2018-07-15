/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import org.apache.commons.lang3.text.WordUtils;
import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.platform.mobile.utils.ObjectiveCUtils;
import ru.pocketbyte.locolaser.platform.mobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile;
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Objective-C .h file generator. Part of Objective-C class generation.
 * @author Denis Shurygin
 */
public class IosObjectiveCHResourceFile extends BaseClassResourceFile {

    private static String CLASS_HEADER_TEMPLATE = "#import <Foundation/Foundation.h>\r\n"
            + "\r\n"
            + "@interface %s : NSObject\r\n";

    private static String CLASS_FOOTER_TEMPLATE = "@end";

    private static String PROPERTY_TEMPLATE = "@property (class, readonly) NSString* %s;\r\n";

    private static int MAX_LINE_SIZE = 120;

    private String mClassName;

    public IosObjectiveCHResourceFile(File file, String className) {
        super(file);
        mClassName = className;
    }

    @Override
    public ResMap read() {
        return null;
    }

    @Override
    protected void writeHeaderComment() throws IOException {
        writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT);
    }

    @Override
    protected void writeClassHeader() throws IOException {
        writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName));
    }

    @Override
    protected void writeComment(String comment) throws IOException {
        String commentLinePrefix = "/// ";
        writeString(commentLinePrefix);
        writeStringLn(WordUtils.wrap(ObjectiveCUtils.escapeComment(comment), MAX_LINE_SIZE - commentLinePrefix.length()
                , "\r\n" + commentLinePrefix, true));
    }

    @Override
    protected void writeProperty(String propertyName, ResItem item) throws IOException {
        writeStringLn(String.format(PROPERTY_TEMPLATE, propertyName));
    }

    @Override
    protected void writeClassFooter() throws IOException {
        writeString(CLASS_FOOTER_TEMPLATE);
    }

}