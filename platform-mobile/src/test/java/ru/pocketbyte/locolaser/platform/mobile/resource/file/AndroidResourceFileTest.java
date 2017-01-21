/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.entity.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * @author Denis Shurygin
 */
public class AndroidResourceFileTest {

    private static String testString = "?'test';:<tag>\"value\nsecond line\" %s<tagg/>";
    private static String platformTestString = "\\?\\'test\\';:&lt;tag>\\\"value\\nsecond line\\\" %s&lt;tagg/>";

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testToPlatformValue() {
        assertEquals(platformTestString, AndroidResourceFile.toPlatformValue(testString));
    }

    @Test
    public void testFromPlatformValue() {
        assertEquals(testString, AndroidResourceFile.fromPlatformValue(platformTestString));
    }

    @Test
    public void testReadNotExistsFile() throws IOException {
        File testFile = tempFolder.newFile();
        if (testFile.exists())
            assertTrue(testFile.delete());

        AndroidResourceFile resourceFile = new AndroidResourceFile(testFile, "en");

        assertNull(resourceFile.read());
    }

    @Test
    public void testRead() throws IOException {
        String testLocale = "ru";
        File testFile = prepareTestFile(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    /* Comment */\n" +
                "    <string name=\"string1\">Value1</string>\n" +
                "    <string name=\"string2\">Value2</string>\n" +
                "\n" +
                "    <string name=\"string3\">Value 3</string>\n" +
                "</resources>");

        AndroidResourceFile resourceFile = new AndroidResourceFile(testFile, testLocale);
        ResMap resMap = resourceFile.read();

        assertNotNull(resMap);

        ResMap expectedMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{ new ResValue("Value1", null, Quantity.OTHER) }));
        resLocale.put(prepareResItem("string2", new ResValue[]{ new ResValue("Value2", null, Quantity.OTHER) }));
        resLocale.put(prepareResItem("string3", new ResValue[]{ new ResValue("Value 3", null, Quantity.OTHER) }));
        expectedMap.put(testLocale, resLocale);

        assertEquals(expectedMap, resMap);
    }

    @Test
    public void testReadPlurals() throws IOException {
        String testLocale = "en";
        File testFile = prepareTestFile(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    /* Comment */\n" +
                "    <plurals name=\"string1\">\n" +
                "        <item quantity=\"other\">String</item>\n" +
                "        <item quantity=\"one\">String one</item>\n" +
                "        <item quantity=\"two\">String two</item>\n" +
                "    </plurals>\n" +
                "    <string name=\"string2\">Value2</string>\n" +
                "\n" +
                "    <plurals name=\"string3\">\n" +
                "        /* Comment */\n" +
                "        <item quantity=\"other\">String 3</item>\n" +
                "        <item quantity=\"few\">String 3 few</item>\n" +
                "        <item quantity=\"zero\">String 3 zero</item>\n" +
                "    </plurals>\n" +
                "</resources>");

        AndroidResourceFile resourceFile = new AndroidResourceFile(testFile, testLocale);
        ResMap resMap = resourceFile.read();

        assertNotNull(resMap);

        ResMap expectedMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{
                new ResValue("String", null, Quantity.OTHER),
                new ResValue("String one", null, Quantity.ONE),
                new ResValue("String two", null, Quantity.TWO)
        }));
        resLocale.put(prepareResItem("string2", new ResValue[]{ new ResValue("Value2", null, Quantity.OTHER) }));
        resLocale.put(prepareResItem("string3", new ResValue[]{
                new ResValue("String 3", null, Quantity.OTHER),
                new ResValue("String 3 few", null, Quantity.FEW),
                new ResValue("String 3 zero", null, Quantity.ZERO)
        }));
        expectedMap.put(testLocale, resLocale);

        assertEquals(expectedMap, resMap);
    }

    @Test
    public void testWrite() throws IOException {
        String testLocale = "ru";
        String redundantLocale = "base";

        ResMap resMap = new ResMap();

        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue("value1_1", "Comment", Quantity.OTHER) }));
        resLocale.put(prepareResItem("key2", new ResValue[]{ new ResValue("value2_1", "value2_1", Quantity.OTHER) }));
        resMap.put(testLocale, resLocale);

        // Redundant locale. Shouldn't be written into file.
        resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue("value1_2", null, Quantity.OTHER) }));
        resLocale.put(prepareResItem("key3", new ResValue[]{ new ResValue("value3_2", "value2_1", Quantity.OTHER) }));
        resMap.put(redundantLocale, resLocale);

        File testFile = tempFolder.newFile();
        AndroidResourceFile resourceFile = new AndroidResourceFile(testFile, testLocale);
        resourceFile.write(resMap, null);

        String expectedResult =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    /* Comment */\n" +
                "    <string name=\"key1\">value1_1</string>\n" +
                "    /* value2_1 */\n" +
                "    <string name=\"key2\">value2_1</string>\n" +
                "</resources>";

        assertEquals(expectedResult, readFile(testFile));
    }

    @Test
    public void testWriteWithWritingConfig() throws IOException {
        String testLocale = "ru";

        ResMap resMap = new ResMap();

        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue("value1_1", "Comment", Quantity.OTHER) }));
        resLocale.put(prepareResItem("key2", new ResValue[]{ new ResValue("value2_1", "value2_1", Quantity.OTHER) }));
        resMap.put(testLocale, resLocale);

        WritingConfig writingConfig = new WritingConfig();
        writingConfig.setDuplicateComments(false);

        File testFile = tempFolder.newFile();
        AndroidResourceFile resourceFile = new AndroidResourceFile(testFile, testLocale);
        resourceFile.write(resMap, writingConfig);

        String expectedResult =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    /* Comment */\n" +
                "    <string name=\"key1\">value1_1</string>\n" +
                "    <string name=\"key2\">value2_1</string>\n" +
                "</resources>";

        assertEquals(expectedResult, readFile(testFile));
    }

    @Test
    public void testWritePlurals() throws IOException {
        String testLocale = "en";

        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{
                new ResValue("String", "Comment", Quantity.OTHER),
                new ResValue("String one", null, Quantity.ONE),
                new ResValue("String two", null, Quantity.TWO)
        }));
        resLocale.put(prepareResItem("string2", new ResValue[]{ new ResValue("Value2", null, Quantity.OTHER) }));
        resLocale.put(prepareResItem("string3", new ResValue[]{
                new ResValue("String 3", "Comment", Quantity.OTHER),
                new ResValue("String 3 few", null, Quantity.FEW),
                new ResValue("String 3 zero", null, Quantity.ZERO)
        }));
        resMap.put(testLocale, resLocale);

        File testFile = tempFolder.newFile();
        AndroidResourceFile resourceFile = new AndroidResourceFile(testFile, testLocale);
        resourceFile.write(resMap, null);

        String expectedResult =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    <plurals name=\"string1\">\n" +
                "        <item quantity=\"other\">String</item>\n" +
                "        <item quantity=\"one\">String one</item>\n" +
                "        <item quantity=\"two\">String two</item>\n" +
                "    </plurals>\n" +
                "    <string name=\"string2\">Value2</string>\n" +
                "    <plurals name=\"string3\">\n" +
                "        <item quantity=\"other\">String 3</item>\n" +
                "        <item quantity=\"few\">String 3 few</item>\n" +
                "        <item quantity=\"zero\">String 3 zero</item>\n" +
                "    </plurals>\n" +
                "</resources>";

        assertEquals(expectedResult, readFile(testFile));
    }


    @Test
    public void testValueCorrectionWhenRead() throws IOException {
        String testLocale = "en";
        File testFile = prepareTestFile(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    <plurals name=\"string1\">\n" +
                "        <item quantity=\"other\">String\\'</item>\n" +
                "        <item quantity=\"one\">\\?String one</item>\n" +
                "    </plurals>\n" +
                "    <string name=\"string2\">\\\"Value2</string>\n" +
                "</resources>");

        AndroidResourceFile resourceFile = new AndroidResourceFile(testFile, testLocale);
        ResMap resMap = resourceFile.read();

        assertNotNull(resMap);

        ResMap expectedMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{
                new ResValue("String'", null, Quantity.OTHER),
                new ResValue("?String one", null, Quantity.ONE)
        }));
        resLocale.put(prepareResItem("string2", new ResValue[]{ new ResValue("\"Value2", null, Quantity.OTHER) }));
        expectedMap.put(testLocale, resLocale);

        assertEquals(expectedMap, resMap);
    }

    @Test
    public void testValueCorrectionWhenWrite() throws IOException {
        String testLocale = "en";

        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{
                new ResValue("String'", null, Quantity.OTHER),
                new ResValue("?String one", null, Quantity.ONE)
        }));
        resLocale.put(prepareResItem("string2", new ResValue[]{ new ResValue("\"Value2", null, Quantity.OTHER) }));
        resMap.put(testLocale, resLocale);

        File testFile = tempFolder.newFile();
        AndroidResourceFile resourceFile = new AndroidResourceFile(testFile, testLocale);
        resourceFile.write(resMap, null);

        String expectedResult =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<resources>\n" +
                        "    <plurals name=\"string1\">\n" +
                        "        <item quantity=\"other\">String\\'</item>\n" +
                        "        <item quantity=\"one\">\\?String one</item>\n" +
                        "    </plurals>\n" +
                        "    <string name=\"string2\">\\\"Value2</string>\n" +
                        "</resources>";

        assertEquals(expectedResult, readFile(testFile));
    }

    private File prepareTestFile(String text) throws IOException {
        File file = tempFolder.newFile();
        PrintWriter writer = new PrintWriter(file);
        writer.write(text);
        writer.flush();
        writer.close();
        return file;
    }

    private String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), Charset.defaultCharset())
                .replace("\r\n", "\n");
    }

    private ResItem prepareResItem(String key, ResValue[] values) {
        ResItem resItem = new ResItem(key);
        for (ResValue value: values)
            resItem.addValue(value);
        return resItem;
    }
}
