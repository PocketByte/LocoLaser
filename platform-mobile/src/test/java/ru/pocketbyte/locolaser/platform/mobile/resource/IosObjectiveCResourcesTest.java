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

public class IosObjectiveCResourcesTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();


    @Test
    public void testWriteObjcFile() throws IOException {
        ResMap resMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER)
        }));
        resMap.put(PlatformResources.BASE_LOCALE, resLocale);

        String className = "Strings";
        String tableName = "somAnotherTable";
        File sourceDir = tempFolder.newFolder();

        new IosObjectiveCResources(sourceDir, className, tableName)
                .write(resMap, null);

        File objcHFile = new File(sourceDir, className + IosObjectiveCResources.OBJC_H_FILE_EXTENSION);
        File objcMFile = new File(sourceDir, className + IosObjectiveCResources.OBJC_M_FILE_EXTENSION);
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
                        "    return NSLocalizedStringFromTable(@\"key3\", @\"somAnotherTable\", @\"\")\r\n" +
                        "}\r\n" +
                        "\r\n" +
                        "@end";

        assertEquals(expectedMResult, readFile(objcMFile));
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
