/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.platform.mobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Objective-C .m file generator. Part of Objective-C class generation.
 * @author Denis Shurygin
 */
public class IosObjectiveCMResourceFile extends ResourceStreamFile {

    private static String CLASS_HEADER_TEMPLATE = "#import <%s.h>\r\n"
            + "\r\n"
            + "@implementation %s\r\n";

    private static String CLASS_FOOTER_TEMPLATE = "@end";

    private static String PROPERTY_TEMPLATE =
            "+(NSString*)%s {\r\n" +
            "    return NSLocalizedStringFromTable(@\"%s\", @\"%s\", @\"%s\")\r\n" +
            "}";

    private static int MAX_LINE_SIZE = 120;

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
    public void write(ResMap resMap, WritingConfig writingConfig) throws IOException {
        ResLocale locale = resMap.get(PlatformResources.BASE_LOCALE);
        if (locale != null) {
            open();
            writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT);
            writeln();
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName, mClassName));

            Set<String> keysSet = new HashSet<>();
            for (ResItem item : locale.values()) {
                String propertyName = keyToProperty(item.key);
                if (!keysSet.contains(propertyName)) {

                    keysSet.add(propertyName);

                    ResValue valueOther = item.valueForQuantity(Quantity.OTHER);
                    String comment = valueOther != null && valueOther.comment != null ? valueOther.comment : "";

                    writeStringLn(String.format(PROPERTY_TEMPLATE, propertyName, item.key, mTableName, comment));
                    writeln();
                }
            }

            writeString(CLASS_FOOTER_TEMPLATE);
            close();
        }
    }

    static String keyToProperty(String key) {
        return key.replaceAll("[^0-9|A-Z|a-z]{1,}", "_")
                .replaceAll("(^[0-9])", "_$1")
                .replaceAll("(_$){1,}", "").toLowerCase();
    }
}