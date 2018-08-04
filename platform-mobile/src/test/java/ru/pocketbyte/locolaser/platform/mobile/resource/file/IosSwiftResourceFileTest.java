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
import ru.pocketbyte.locolaser.utils.TextUtils;

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
public class IosSwiftResourceFileTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testRead() throws IOException {
        IosSwiftResourceFile resourceFile = new IosSwiftResourceFile(tempFolder.newFile(), "Strings", "Strings");
        assertNull(resourceFile.read());
    }

    @Test
    public void testWriteOneItem() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue("value1_1", "Comment", Quantity.OTHER) }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        File testFile = tempFolder.newFile();
        IosSwiftResourceFile resourceFile = new IosSwiftResourceFile(testFile, "Strings", "Strings");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "import Foundation\r\n" +
                "\r\n" +
                "public class Strings {\r\n" +
                "\r\n" +
                "    /// value1_1\r\n" +
                "    public static var key1 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                                                                  " value:\"value1_1\", comment: \"Comment\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
                "\r\n" +
                "}";

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
        IosSwiftResourceFile resourceFile = new IosSwiftResourceFile(testFile, "Strings", "Strings");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "import Foundation\r\n" +
                "\r\n" +
                "public class Strings {\r\n" +
                "\r\n" +
                "    /// value1_2\r\n" +
                "    public static var key1 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                                                                  " value:\"value1_2\", comment: \"\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
                "\r\n" +
                "    /// value3_2\r\n" +
                "    public static var key3 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key3\", tableName:\"Strings\", bundle:Bundle.main," +
                                                                  " value:\"value3_2\", comment: \"value2_1\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
                "\r\n" +
                "}";

        assertEquals(expectedResult, readFile(testFile));
    }

    @Test
    public void testWriteLongPropertyComment() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue(
                "Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery " +
                "Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Long Comment",
                null,
                Quantity.OTHER) }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        File testFile = tempFolder.newFile();
        IosSwiftResourceFile resourceFile = new IosSwiftResourceFile(testFile, "Strings", "Strings");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "import Foundation\r\n" +
                "\r\n" +
                "public class Strings {\r\n" +
                "\r\n" +
                "    /// Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery " +
                                                                            "Wery Wery Wery Wery Wery Wery\r\n" +
                "    /// Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Long " +
                                                                                                "Comment\r\n" +
                "    public static var key1 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                        " value:\"Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery" +
                        " Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery" +
                        " Wery Wery Wery Long Comment\", comment: \"\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
                "\r\n" +
                "}";

        assertEquals(expectedResult, readFile(testFile));
    }

    @Test
    public void testWriteSpecialCharacterInValue() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue(
                "\\{\"ssdsd\": 333} ; ';k.,/?@'' \n {\"ssdsd\": 333} ; ';k.,/?@''",
                "Some Comment",
                Quantity.OTHER) }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);


        File testFile = tempFolder.newFile();
        IosSwiftResourceFile resourceFile = new IosSwiftResourceFile(testFile, "Strings", "Strings");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "import Foundation\r\n" +
                        "\r\n" +
                        "public class Strings {\r\n" +
                        "\r\n" +
                        "    /// \\{\"ssdsd\": 333} ; ';k.,/?@'' \\n {\"ssdsd\": 333} ; ';k.,/?@''\r\n" +
                        "    public static var key1 : String {\r\n" +
                        "        get {\r\n" +
                        "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                        " value:\"\\\\{\\\"ssdsd\\\": 333} ; ';k.,/?@'' \\n {\\\"ssdsd\\\": 333} ; ';k.,/?@''\", comment: \"Some Comment\")\r\n" +
                        "        }\r\n" +
                        "    }\r\n" +
                        "\r\n" +
                        "}";

        assertEquals(expectedResult, readFile(testFile));
    }

    @Test
    public void testWriteSpecialCharacterInComment() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue(
                "Some Value",
                "\\{\"ssdsd\": 333} ; ';k.,/?@'' \n {\"ssdsd\": 333} ; ';k.,/?@''",
                Quantity.OTHER) }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);


        File testFile = tempFolder.newFile();
        IosSwiftResourceFile resourceFile = new IosSwiftResourceFile(testFile, "Strings", "Strings");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "import Foundation\r\n" +
                        "\r\n" +
                        "public class Strings {\r\n" +
                        "\r\n" +
                        "    /// Some Value\r\n" +
                        "    public static var key1 : String {\r\n" +
                        "        get {\r\n" +
                        "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                        " value:\"Some Value\", comment: \"\\\\{\\\"ssdsd\\\": 333} ; ';k.,/?@'' \\n {\\\"ssdsd\\\": 333} ; ';k.,/?@''\")\r\n" +
                        "        }\r\n" +
                        "    }\r\n" +
                        "\r\n" +
                        "}";

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
