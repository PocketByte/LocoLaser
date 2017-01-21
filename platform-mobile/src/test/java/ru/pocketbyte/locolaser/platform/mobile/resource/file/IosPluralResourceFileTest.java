/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.resource.entity.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Denis Shurygin
 */
public class IosPluralResourceFileTest {

    private static String testString = "?'test';:<tag>\"value\nsecond line\" %s<tagg/>";
    private static String platformTestString = "?\\'test\\';:&lt;tag>\\\"value\\nsecond line\\\" %@&lt;tagg/>";

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testToPlatformValue() {
        assertEquals(platformTestString, IosPluralResourceFile.toPlatformValue(testString));
    }

    @Test
    public void testFromPlatformValue() {
        assertEquals(testString, IosPluralResourceFile.fromPlatformValue(platformTestString));
    }

    @Test
    public void testReadPlurals() throws IOException {
        String testLocale = "en";
        File testFile = prepareTestFile(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<plist version=\"1.0\">\n" +
                "<dict>\n" +
                "    <key>string1</key>\n" +
                "    <dict>\n" +
                "        <key>NSStringLocalizedFormatKey</key>\n" +
                "        <string>%#@value@</string>\n" +
                "        <key>value</key>\n" +
                "        <dict>\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\n" +
                "            <string>NSStringPluralRuleType</string>\n" +
                "            <key>one</key>\n" +
                "            <string>String one</string>\n" +
                "            <key>two</key>\n" +
                "            <string>String two</string>\n" +
                "            <key>other</key>\n" +
                "            <string>String</string>\n" +
                "        </dict>\n" +
                "    </dict>\n" +
                "    <key>string3</key>\n" +
                "    <dict>\n" +
                "        <key>NSStringLocalizedFormatKey</key>\n" +
                "        <string>%#@value@</string>\n" +
                "        <key>value</key>\n" +
                "        <dict>\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\n" +
                "            <string>NSStringPluralRuleType</string>\n" +
                "            <key>zero</key>\n" +
                "            <string>String 3 zero</string>\n" +
                "            <key>few</key>\n" +
                "            <string>String 3 few</string>\n" +
                "            <key>other</key>\n" +
                "            <string>String 3</string>\n" +
                "        </dict>\n" +
                "    </dict>\n" +
                "</dict>\n" +
                "</plist>");

        IosPluralResourceFile resourceFile = new IosPluralResourceFile(testFile, testLocale);
        ResMap resMap = resourceFile.read();

        assertNotNull(resMap);

        ResMap expectedMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{
                new ResValue("String", null, Quantity.OTHER),
                new ResValue("String one", null, Quantity.ONE),
                new ResValue("String two", null, Quantity.TWO)
        }));
        resLocale.put(prepareResItem("string3", new ResValue[]{
                new ResValue("String 3", null, Quantity.OTHER),
                new ResValue("String 3 few", null, Quantity.FEW),
                new ResValue("String 3 zero", null, Quantity.ZERO)
        }));
        expectedMap.put(testLocale, resLocale);

        assertEquals(expectedMap, resMap);
    }

    @Test
    public void testWritePlurals() throws IOException {
        String testLocale = "en";
        String redundantLocale = "base";

        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{
                new ResValue("String", "Comment", Quantity.OTHER),
                new ResValue("String one", null, Quantity.ONE),
                new ResValue("String two", null, Quantity.TWO)
        }));
        resLocale.put(prepareResItem("string3", new ResValue[]{
                new ResValue("String 3", "Comment", Quantity.OTHER),
                new ResValue("String 3 few", null, Quantity.FEW),
                new ResValue("String 3 zero", null, Quantity.ZERO)
        }));

        // Not plural values shouldn't be written into file
        resLocale.put(prepareResItem("string2", new ResValue[]{ new ResValue("Value2", null, Quantity.OTHER) }));
        resMap.put(testLocale, resLocale);

        // Redundant locale. Shouldn't be written into file.
        resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue("value1_2", null, Quantity.OTHER) }));
        resLocale.put(prepareResItem("key3", new ResValue[]{ new ResValue("value3_2", "value2_1", Quantity.OTHER) }));
        resMap.put(redundantLocale, resLocale);

        File testFile = tempFolder.newFile();
        IosPluralResourceFile resourceFile = new IosPluralResourceFile(testFile, testLocale);
        resourceFile.write(resMap, null);

        String expectedResult =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<plist version=\"1.0\">\n" +
                "<dict>\n" +
                "    <key>string1</key>\n" +
                "    <dict>\n" +
                "        <key>NSStringLocalizedFormatKey</key>\n" +
                "        <string>%#@value@</string>\n" +
                "        <key>value</key>\n" +
                "        <dict>\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\n" +
                "            <string>NSStringPluralRuleType</string>\n" +
                "            <key>other</key>\n" +
                "            <string>String</string>\n" +
                "            <key>one</key>\n" +
                "            <string>String one</string>\n" +
                "            <key>two</key>\n" +
                "            <string>String two</string>\n" +
                "        </dict>\n" +
                "    </dict>\n" +
                "    <key>string3</key>\n" +
                "    <dict>\n" +
                "        <key>NSStringLocalizedFormatKey</key>\n" +
                "        <string>%#@value@</string>\n" +
                "        <key>value</key>\n" +
                "        <dict>\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\n" +
                "            <string>NSStringPluralRuleType</string>\n" +
                "            <key>other</key>\n" +
                "            <string>String 3</string>\n" +
                "            <key>few</key>\n" +
                "            <string>String 3 few</string>\n" +
                "            <key>zero</key>\n" +
                "            <string>String 3 zero</string>\n" +
                "        </dict>\n" +
                "    </dict>\n" +
                "</dict>\n" +
                "</plist>";

        assertEquals(expectedResult, readFile(testFile));
    }


    @Test
    public void testValueCorrectionWhenRead() throws IOException {
        String testLocale = "en";
        File testFile = prepareTestFile(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<plist version=\"1.0\">\n" +
                "<dict>\n" +
                "    <key>string1</key>\n" +
                "    <dict>\n" +
                "        <key>NSStringLocalizedFormatKey</key>\n" +
                "        <string>%#@value@</string>\n" +
                "        <key>value</key>\n" +
                "        <dict>\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\n" +
                "            <string>NSStringPluralRuleType</string>\n" +
                "            <key>other</key>\n" +
                "            <string>" + platformTestString + "</string>\n" +
                "        </dict>\n" +
                "    </dict>\n" +
                "</dict>\n" +
                "</plist>");

        IosPluralResourceFile resourceFile = new IosPluralResourceFile(testFile, testLocale);
        ResMap resMap = resourceFile.read();

        assertNotNull(resMap);

        ResMap expectedMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{ new ResValue(testString, null, Quantity.OTHER) }));
        expectedMap.put(testLocale, resLocale);

        assertEquals(expectedMap, resMap);
    }

    @Test
    public void testValueCorrectionWhenWrite() throws IOException {
        String testLocale = "en";

        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{
                new ResValue(testString, null, Quantity.OTHER),
                new ResValue("one", null, Quantity.ONE)
        }));
        resMap.put(testLocale, resLocale);

        File testFile = tempFolder.newFile();
        IosPluralResourceFile resourceFile = new IosPluralResourceFile(testFile, testLocale);
        resourceFile.write(resMap, null);

        String expectedResult =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<plist version=\"1.0\">\n" +
                "<dict>\n" +
                "    <key>string1</key>\n" +
                "    <dict>\n" +
                "        <key>NSStringLocalizedFormatKey</key>\n" +
                "        <string>%#@value@</string>\n" +
                "        <key>value</key>\n" +
                "        <dict>\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\n" +
                "            <string>NSStringPluralRuleType</string>\n" +
                "            <key>other</key>\n" +
                "            <string>" + platformTestString + "</string>\n" +
                "            <key>one</key>\n" +
                "            <string>one</string>\n" +
                "        </dict>\n" +
                "    </dict>\n" +
                "</dict>\n" +
                "</plist>";

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
