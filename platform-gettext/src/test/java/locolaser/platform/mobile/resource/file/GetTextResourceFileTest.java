package locolaser.platform.mobile.resource.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.platform.mobile.resource.file.GetTextResourceFile;
import ru.pocketbyte.locolaser.resource.entity.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class GetTextResourceFileTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testReadNotExistsFile() throws IOException {
        File testFile = tempFolder.newFile();
        if (testFile.exists())
            assertTrue(testFile.delete());

        GetTextResourceFile resourceFile = new GetTextResourceFile(testFile, "en");

        assertNull(resourceFile.read());
    }

    @Test
    public void testRead() throws IOException {
        String testLocale = "ru";
        File testFile = prepareTestFile(
                GetTextResourceFile.GENERATED_GETTEXT_COMMENT + "\r\n\r\n" +
                        "# Comment\r\n" +
                        "msgid \"string1\"\r\n" +
                        "msgstr \"Value1\"\r\n" +
                        "msgid \"string2\"\r\n" +
                        "msgstr \"Value2\"\r\n" +
                        "\r\n" +
                        "msgid \"string3\"\r\n" +
                        "msgstr \"Value 3\"");

        GetTextResourceFile resourceFile = new GetTextResourceFile(testFile, testLocale);
        ResMap resMap = resourceFile.read();

        assertNotNull(resMap);

        ResMap expectedMap = new ResMap();
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("string1", new ResValue[]{ new ResValue("Value1", "Comment", Quantity.OTHER) }));
        resLocale.put(prepareResItem("string2", new ResValue[]{ new ResValue("Value2", null, Quantity.OTHER) }));
        resLocale.put(prepareResItem("string3", new ResValue[]{ new ResValue("Value 3", null, Quantity.OTHER) }));
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
        GetTextResourceFile resourceFile = new GetTextResourceFile(testFile, testLocale);
        resourceFile.write(resMap, null);

        String expectedResult = GetTextResourceFile.GENERATED_GETTEXT_COMMENT + "\r\n\r\n" +
                "# Comment\r\n" +
                "msgid \"key1\"\r\n" +
                "msgstr \"value1_1\"\r\n" +
                "\r\n" +
                "# value2_1\r\n" +
                "msgid \"key2\"\r\n" +
                "msgstr \"value2_1\"";

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
        GetTextResourceFile resourceFile = new GetTextResourceFile(testFile, testLocale);
        resourceFile.write(resMap, writingConfig);

        String expectedResult = GetTextResourceFile.GENERATED_GETTEXT_COMMENT + "\r\n\r\n" +
                "# Comment\r\n" +
                "msgid \"key1\"\r\n" +
                "msgstr \"value1_1\"\r\n" +
                "\r\n" +
                "msgid \"key2\"\r\n" +
                "msgstr \"value2_1\"";

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
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), Charset.defaultCharset());
    }

    private ResItem prepareResItem(String key, ResValue[] values) {
        ResItem resItem = new ResItem(key);
        for (ResValue value: values)
            resItem.addValue(value);
        return resItem;
    }
}
