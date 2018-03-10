package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.resource.entity.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class KotlinInterfaceResourceFileTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testRead() throws IOException {
        KotlinInterfaceResourceFile resourceFile = new KotlinInterfaceResourceFile(tempFolder.newFile(),"Str", "com.package");
        assertNull(resourceFile.read());
    }

    @Test
    public void testWriteOneItem() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{ new ResValue("value1_1", "Comment", Quantity.OTHER) }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        File testFile = tempFolder.newFile();
        KotlinInterfaceResourceFile resourceFile = new KotlinInterfaceResourceFile(testFile, "Str", "com.package");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "package com.package\r\n" +
                        "\r\n" +
                        "interface Str {\r\n" +
                        "\r\n" +
                        "    /**\r\n" +
                        "    * value1_1\r\n" +
                        "    */\r\n" +
                        "    val key1: String\r\n" +
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
        KotlinInterfaceResourceFile resourceFile = new KotlinInterfaceResourceFile(testFile, "StrInterface", "com.some.package");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "package com.some.package\r\n" +
                        "\r\n" +
                        "interface StrInterface {\r\n" +
                        "\r\n" +
                        "    /**\r\n" +
                        "    * value1_2\r\n" +
                        "    */\r\n" +
                        "    val key1: String\r\n" +
                        "\r\n" +
                        "    /**\r\n" +
                        "    * value3_2\r\n" +
                        "    */\r\n" +
                        "    val key3: String\r\n" +
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
        KotlinInterfaceResourceFile resourceFile = new KotlinInterfaceResourceFile(
                testFile, "Strings", "ru.pocketbyte");

        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "package ru.pocketbyte\r\n" +
                        "\r\n" +
                        "interface Strings {\r\n" +
                        "\r\n" +
                        "    /**\r\n" +
                        "    * Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery" +
                        " Wery Wery Wery Wery Wery Wery\r\n" +
                        "    * Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Long Comment\r\n" +
                        "    */\r\n" +
                        "    val key1: String\r\n" +
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
