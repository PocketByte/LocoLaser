package ru.pocketbyte.locolaser.config.platform;

import org.junit.Test;
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.config.parser.PlatformSetConfigParser;
import ru.pocketbyte.locolaser.config.parser.PlatformSetConfigParserTest;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformConfig;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class PlatformSetConfigTest {

    @Test
    public void testTempFolder() throws InvalidConfigException {
        final File testFolder1 = new File("./temp/test_folder1/");
        final File testFolder2 = new File("./temp/test_folder2/");

        PlatformConfig config1 = new MockPlatformConfig() {
            @Override
            public File getDefaultTempDir() {
                return testFolder1;
            }
        };

        PlatformConfig config2 = new MockPlatformConfig() {
            @Override
            public File getDefaultTempDir() {
                return testFolder2;
            }
        };

        PlatformSetConfig configSet = prepareConfig(config1, config2);

        assertSame(testFolder1, configSet.getDefaultTempDir());
        assertNotSame(testFolder2, configSet.getDefaultTempDir());

        configSet = prepareConfig(config2, config1);

        assertNotSame(testFolder1, configSet.getDefaultTempDir());
        assertSame(testFolder2, configSet.getDefaultTempDir());
    }

    private PlatformSetConfig prepareConfig(PlatformConfig config1, PlatformConfig config2) {
        Set<PlatformConfig> set = new LinkedHashSet<>(2);
        set.add(config1);
        set.add(config2);
        return new PlatformSetConfig(set);
    }
}
