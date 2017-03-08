/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Denis Shurygin
 */
public class IosResourcesTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testWriteAndRead() throws IOException {
        ResMap resMap1 = prepareResMap();

        IosResources resources = new IosResources(tempFolder.newFolder(), "test");
        resources.write(resMap1, null);

        ResMap resMap2 = resources.read(resMap1.keySet());

        assertNotNull(resMap2);
        assertEquals(resMap1, resMap2);
    }

    @Test
    public void testWriteSwiftFile() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER)
        }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        String className = "Strings";
        File sourceDir = tempFolder.newFolder();

        IosResources resources = new IosResources(tempFolder.newFolder(), "test");
        resources.setSourceDir(sourceDir);
        resources.setSwiftClassName(className);
        resources.write(resMap, null);

        File swiftFile = new File(sourceDir, className + IosResources.SWIFT_FILE_EXTENSION);
        assertTrue(swiftFile.exists());

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "import Foundation\r\n" +
                        "\r\n" +
                        "public class Strings {\r\n" +
                        "\r\n" +
                        "    /// value1_2\r\n" +
                        "    public static var key3 : String {\r\n" +
                        "        get {\r\n" +
                        "            return NSLocalizedString(\"key3\", comment: \"\")\r\n" +
                        "        }\r\n" +
                        "    }\r\n" +
                        "\r\n" +
                        "}";

        assertEquals(expectedResult, readFile(swiftFile));
    }

    @Test
    public void testWriteSwiftFileWithNullDir() throws IOException {
        ResMap resMap = new ResMap();
        resMap.put(PlatformResources.BASE_LOCALE, prepareResLocale1());

        String className = "Str";

        IosResources resources = new IosResources(tempFolder.newFolder(), "test");
        resources.setSourceDir(null);
        resources.setSwiftClassName(className);
        resources.write(resMap, null);

        File swiftFile = new File(System.getProperty("user.dir"), className + IosResources.SWIFT_FILE_EXTENSION);
        assertTrue(swiftFile.exists());
    }

    @Test
    public void testWriteObjcFile() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER)
        }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        String className = "Strings";
        File sourceDir = tempFolder.newFolder();

        IosResources resources = new IosResources(tempFolder.newFolder(), "test");
        resources.setSourceDir(sourceDir);
        resources.setObjcClassName(className);
        resources.write(resMap, null);

        File objcHFile = new File(sourceDir, className + IosResources.OBJC_H_FILE_EXTENSION);
        File objcMFile = new File(sourceDir, className + IosResources.OBJC_M_FILE_EXTENSION);
        assertTrue(objcHFile.exists());
        assertTrue(objcMFile.exists());

        String expectedHResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "#import <Foundation/Foundation.h>\r\n" +
                        "\r\n" +
                        "@interface Strings : NSObject\r\n" +
                        "\r\n" +
                        "/// value1_2\r\n" +
                        "@property (class, readonly) NSString* key3;\r\n" +
                        "\r\n" +
                        "@end";

        assertEquals(expectedHResult, readFile(objcHFile));

        String expectedMResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "#import <Strings.h>\r\n" +
                        "\r\n" +
                        "@implementation Strings\r\n" +
                        "\r\n" +
                        "+(NSString*)key3 {\r\n" +
                        "    return NSLocalizedString(@\"key3\", @\"\")\r\n" +
                        "}\r\n" +
                        "\r\n" +
                        "@end";

        assertEquals(expectedMResult, readFile(objcMFile));
    }

    @Test
    public void testWriteObjCFileWithNullDir() throws IOException {
        ResMap resMap = new ResMap();
        resMap.put(PlatformResources.BASE_LOCALE, prepareResLocale1());

        String className = "Str";

        IosResources resources = new IosResources(tempFolder.newFolder(), "test");
        resources.setSourceDir(null);
        resources.setObjcClassName(className);
        resources.write(resMap, null);

        File objcHFile = new File(System.getProperty("user.dir"), className + IosResources.OBJC_H_FILE_EXTENSION);
        File objcMFile = new File(System.getProperty("user.dir"), className + IosResources.OBJC_M_FILE_EXTENSION);
        assertTrue(objcHFile.exists());
        assertTrue(objcMFile.exists());
    }

    private ResMap prepareResMap() {
        ResMap resMap = new ResMap();
        resMap.put("locale1", prepareResLocale1());
        resMap.put("locale2", prepareResLocale2());
        resMap.put("locale3", prepareResLocale3());
        return resMap;
    }

    private ResLocale prepareResLocale1() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER),
                new ResValue("value2_1", null, Quantity.MANY),
                new ResValue("value2_1", null, Quantity.FEW)
        }));
        resLocale.put(prepareResItem("key2", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER),
                new ResValue("value2_1", null, Quantity.MANY),
                new ResValue("value3_1", null, Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResLocale prepareResLocale2() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER),
                new ResValue("value2_2", null, Quantity.MANY),
                new ResValue("value3_2", null, Quantity.TWO)
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER),
                new ResValue("value2_2", null, Quantity.MANY),
                new ResValue("value3_2", null, Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResLocale prepareResLocale3() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_3", null, Quantity.OTHER),
                new ResValue("value2_3", null, Quantity.FEW),
                new ResValue("value3_3", null, Quantity.TWO),
                new ResValue("value4_3", null, Quantity.ZERO)
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value1_3", null, Quantity.OTHER),
                new ResValue("value3_3", null, Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResItem prepareResItem(String key, ResValue[] values) {
        ResItem resItem = new ResItem(key);
        for (ResValue value: values)
            resItem.addValue(value);
        return resItem;
    }

    private String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), Charset.defaultCharset());
    }
}
