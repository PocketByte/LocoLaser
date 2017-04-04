/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import org.apache.commons.lang3.text.WordUtils;
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
 * Swift class file generator. For generation used Swift version 3.0.
 */
public class IosSwiftResourceFile extends ResourceStreamFile {

    private static String CLASS_HEADER_TEMPLATE = "import Foundation\r\n"
                                                + "\r\n"
                                                + "public class %s {\r\n";

    private static String CLASS_FOOTER_TEMPLATE = "}";

    private static String PROPERTY_TEMPLATE = "    public static var %s : String {\r\n"
                                            + "        get {\r\n"
                                            + "            return NSLocalizedString(\"%s\", tableName:\"%s\"," +
                                                            " bundle:Bundle.main, value:\"%s\", comment: \"%s\")\r\n"
                                            + "        }\r\n"
                                            + "    }\r\n";

    private static int MAX_LINE_SIZE = 120;

    private String mClassName;
    private String mTableName;

    public IosSwiftResourceFile(File file, String className, String tableName) {
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
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName));

            Set<String> keysSet = new HashSet<>();
            for (ResItem item : locale.values()) {
                String propertyName = keyToProperty(item.key);
                if (!keysSet.contains(propertyName)) {

                    keysSet.add(propertyName);

                    ResValue valueOther = item.valueForQuantity(Quantity.OTHER);
                    if (valueOther != null && valueOther.value != null) {
                        String commentLinePrefix = "    /// ";
                        writeString(commentLinePrefix);
                        writeStringLn(WordUtils.wrap(valueOther.value, MAX_LINE_SIZE - commentLinePrefix.length()
                                , "\r\n" + commentLinePrefix, true));
                    }

                    String comment = valueOther != null && valueOther.comment != null ? valueOther.comment : "";

                    writeStringLn(String.format(PROPERTY_TEMPLATE, propertyName, item.key, mTableName,
                            valueOther.value, comment));
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
