package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AbsIosStringsResourceFileTest {

    public static final String TEST_STRING =
            "?'test';:<tag>\"value\nsecond line\" %1$s %2$s %s<tagg/>" +
                    " Wrong Formats: %$s $5s";
    public static final String PLATFORM_TEST_STRING =
            "?'test';:<tag>\\\"value\\nsecond line\\\" %1$@ %2$@ %@<tagg/>" +
                    " Wrong Formats: %$s $5s";

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testToPlatformValue() {
        assertEquals(PLATFORM_TEST_STRING, AbsIosStringsResourceFile.toPlatformValue(TEST_STRING));
    }

    @Test
    public void testFromPlatformValue() {
        assertEquals(TEST_STRING, AbsIosStringsResourceFile.fromPlatformValue(PLATFORM_TEST_STRING));
    }

    @Test
    public void testReadNotExistsFile() throws IOException {
        File testFile = tempFolder.newFile();
        if (testFile.exists())
            assertTrue(testFile.delete());

        ResourceFileImpl resourceFile = new ResourceFileImpl(testFile, "en");

        assertNull(resourceFile.read());
    }

    private class ResourceFileImpl extends AbsIosStringsResourceFile {

        public ResourceFileImpl(File file, String locale) {
            super(file, locale);
        }

        @Override
        protected String getKeyValueLinePattern() {
            return "";
        }

        @Override
        protected void writeKeyValueString(String key, String value) throws IOException {

        }
    }
}
