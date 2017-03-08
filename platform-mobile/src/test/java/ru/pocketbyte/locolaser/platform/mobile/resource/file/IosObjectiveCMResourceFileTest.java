/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.platform.mobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.resource.entity.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Denis Shurygin
 */
public class IosObjectiveCMResourceFileTest {
    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testRead() throws IOException {
        IosObjectiveCMResourceFile resourceFile = new IosObjectiveCMResourceFile(tempFolder.newFile(), "Strings");
        assertNull(resourceFile.read());
    }

    @Test
    public void testWriteOneItem() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue("value1_1", "Comment", Quantity.OTHER) }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        File testFile = tempFolder.newFile();
        IosObjectiveCMResourceFile resourceFile = new IosObjectiveCMResourceFile(testFile, "Strings");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "#import <Strings.h>\r\n" +
                        "\r\n" +
                        "@implementation Strings\r\n" +
                        "\r\n" +
                        "+(NSString*)key1 {\r\n" +
                        "    return NSLocalizedString(@\"key1\", @\"Comment\")\r\n" +
                        "}\r\n" +
                        "\r\n" +
                        "@end";

        assertEquals(expectedResult, readFile(testFile));
    }

    @Test
    public void testWrite() throws IOException {
        ResMap resMap = new ResMap();

        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue("value1_1", "Comment", Quantity.OTHER) }));
        resLocale.put(prepareResItem("key2", new ResValue[]{ new ResValue("value2_1", "value2_1", Quantity.OTHER) }));
        resMap.put("ru", resLocale);

        resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue("value1_2", null, Quantity.OTHER) }));
        resLocale.put(prepareResItem("key3", new ResValue[]{ new ResValue("value3_2", "value2_1", Quantity.OTHER) }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        File testFile = tempFolder.newFile();
        IosObjectiveCMResourceFile resourceFile = new IosObjectiveCMResourceFile(testFile, "Strings");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "#import <Strings.h>\r\n" +
                        "\r\n" +
                        "@implementation Strings\r\n" +
                        "\r\n" +
                        "+(NSString*)key1 {\r\n" +
                        "    return NSLocalizedString(@\"key1\", @\"\")\r\n" +
                        "}\r\n" +
                        "\r\n" +
                        "+(NSString*)key3 {\r\n" +
                        "    return NSLocalizedString(@\"key3\", @\"value2_1\")\r\n" +
                        "}\r\n" +
                        "\r\n" +
                        "@end";

        assertEquals(expectedResult, readFile(testFile));
    }

    private String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), Charset.defaultCharset());
    }

    private ResItem prepareResItem(String key, ResValue[] values) {
        ResItem resItem = new ResItem(key);
        for (ResValue value: values)
            resItem.addValue(value);
        return resItem;
    }
}
