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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class KotlinIosResourceFileTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testRead() throws IOException {
        KotlinIosResourceFile resourceFile = new KotlinIosResourceFile(tempFolder.newFile(),
                "Str", "com.package", null, null);
        assertNull(resourceFile.read());
    }

    @Test
    public void testWriteOneItem() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_1", "Comment", Quantity.OTHER) }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        File testFile = tempFolder.newFile();
        KotlinIosResourceFile resourceFile = new KotlinIosResourceFile(testFile,
                "Str", "com.package",
                null, null);
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "package com.package\r\n" +
                        "\r\n" +
                        "import kotlinx.cinterop.*\r\n" +
                        "import platform.Foundation.*\r\n" +
                        "\r\n" +
                        "public class Str(private val bundle: NSBundle, private val tableName: String) {\r\n" +
                        "\r\n" +
                        "    constructor(bundle: NSBundle) : this(bundle, \"Localizable\")\r\n" +
                        "    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)\r\n" +
                        "    constructor() : this(NSBundle.mainBundle(), \"Localizable\")\r\n" +
                        "\r\n" +
                        "    /**\r\n" +
                        "    * value1_1\r\n" +
                        "    */\r\n" +
                        "    public val key1: String\r\n" +
                        "        get() = this.bundle.localizedStringForKey(\"key1\", \"\", this.tableName)\r\n" +
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
        KotlinIosResourceFile resourceFile = new KotlinIosResourceFile(testFile,
                "StrImpl", "com.some.package" ,
                null, null);
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "package com.some.package\r\n" +
                        "\r\n" +
                        "import kotlinx.cinterop.*\r\n" +
                        "import platform.Foundation.*\r\n" +
                        "\r\n" +
                        "public class StrImpl(private val bundle: NSBundle, private val tableName: String) {\r\n" +
                        "\r\n" +
                        "    constructor(bundle: NSBundle) : this(bundle, \"Localizable\")\r\n" +
                        "    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)\r\n" +
                        "    constructor() : this(NSBundle.mainBundle(), \"Localizable\")\r\n" +
                        "\r\n" +
                        "    /**\r\n" +
                        "    * value1_2\r\n" +
                        "    */\r\n" +
                        "    public val key1: String\r\n" +
                        "        get() = this.bundle.localizedStringForKey(\"key1\", \"\", this.tableName)\r\n" +
                        "\r\n" +
                        "    /**\r\n" +
                        "    * value3_2\r\n" +
                        "    */\r\n" +
                        "    public val key3: String\r\n" +
                        "        get() = this.bundle.localizedStringForKey(\"key3\", \"\", this.tableName)\r\n" +
                        "\r\n" +
                        "}";

        assertEquals(expectedResult, readFile(testFile));
    }

    @Test
    public void testWriteWithInterface() throws IOException {
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
        KotlinIosResourceFile resourceFile = new KotlinIosResourceFile(testFile,
                "StrImpl", "com.some.package" ,
                "StrInterface", "com.interface");
        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "package com.some.package\r\n" +
                        "\r\n" +
                        "import kotlinx.cinterop.*\r\n" +
                        "import platform.Foundation.*\r\n" +
                        "import com.interface.StrInterface\r\n" +
                        "\r\n" +
                        "public class StrImpl(private val bundle: NSBundle, private val tableName: String): StrInterface {\r\n" +
                        "\r\n" +
                        "    constructor(bundle: NSBundle) : this(bundle, \"Localizable\")\r\n" +
                        "    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)\r\n" +
                        "    constructor() : this(NSBundle.mainBundle(), \"Localizable\")\r\n" +
                        "\r\n" +
                        "    override public val key1: String\r\n" +
                        "        get() = this.bundle.localizedStringForKey(\"key1\", \"\", this.tableName)\r\n" +
                        "\r\n" +
                        "    override public val key3: String\r\n" +
                        "        get() = this.bundle.localizedStringForKey(\"key3\", \"\", this.tableName)\r\n" +
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
        KotlinIosResourceFile resourceFile = new KotlinIosResourceFile(
                testFile, "Strings", "ru.pocketbyte", null, null);

        resourceFile.write(resMap, null);

        String expectedResult =
                TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                        "package ru.pocketbyte\r\n" +
                        "\r\n" +
                        "import kotlinx.cinterop.*\r\n" +
                        "import platform.Foundation.*\r\n" +
                        "\r\n" +
                        "public class Strings(private val bundle: NSBundle, private val tableName: String) {\r\n" +
                        "\r\n" +
                        "    constructor(bundle: NSBundle) : this(bundle, \"Localizable\")\r\n" +
                        "    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)\r\n" +
                        "    constructor() : this(NSBundle.mainBundle(), \"Localizable\")\r\n" +
                        "\r\n" +
                        "    /**\r\n" +
                        "    * Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery" +
                            " Wery Wery Wery Wery Wery Wery\r\n" +
                        "    * Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Long Comment\r\n" +
                        "    */\r\n" +
                        "    public val key1: String\r\n" +
                        "        get() = this.bundle.localizedStringForKey(\"key1\", \"\", this.tableName)\r\n" +
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
