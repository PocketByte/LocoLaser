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
import static org.junit.Assert.assertTrue;

public class IosSwiftResourcesTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testWriteSwiftFile() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER)
        }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        String className = "Strings";
        String tableName = "somTable";
        File sourceDir = tempFolder.newFolder();

        new IosSwiftResources(sourceDir, className, tableName)
                .write(resMap, null);

        File swiftFile = new File(sourceDir, className + IosSwiftResources.SWIFT_FILE_EXTENSION);
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
                        "            return NSLocalizedString(\"key3\", tableName:\"somTable\", bundle:Bundle.main," +
                        " value:\"value1_2\", comment: \"\")\r\n" +
                        "        }\r\n" +
                        "    }\r\n" +
                        "\r\n" +
                        "}";

        assertEquals(expectedResult, readFile(swiftFile));
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
