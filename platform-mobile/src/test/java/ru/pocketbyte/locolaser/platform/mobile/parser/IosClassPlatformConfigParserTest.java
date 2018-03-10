package ru.pocketbyte.locolaser.platform.mobile.parser;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.platform.mobile.IosSwiftPlatformConfig;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IosClassPlatformConfigParserTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    private IosClassPlatformConfigParser parser;

    @Before
    public void init() throws IOException {
        parser = new IosClassPlatformConfigParser();

        File workDir = tempFolder.newFolder();
        System.setProperty("user.dir", workDir.getCanonicalPath());
    }

    @Test(expected = InvalidConfigException.class)
    public void testUnknownFromString() throws InvalidConfigException {
        parser.parse("invalid_platform", true);
    }

    @Test
    public void testFromJson() throws InvalidConfigException, IOException {
        IosSwiftPlatformConfig config = (IosSwiftPlatformConfig)
                parser.parse(prepareTestPlatformJson(IosSwiftPlatformConfig.TYPE), true);

        assertNotNull(config);

        assertEquals("test_res", config.getResourceName());
        assertEquals(new File("test_res_dir").getCanonicalPath(),
                config.getResourcesDir().getCanonicalPath());
        assertEquals("test_table", config.getTableName());
    }

    private JSONObject prepareTestPlatformJson(String platform) {
        JSONObject json = new JSONObject();
        json.put(PlatformConfigParser.PLATFORM_TYPE, platform);
        json.put(IosClassPlatformConfigParser.RESOURCE_NAME, "test_res");
        json.put(IosClassPlatformConfigParser.RESOURCES_DIR, "test_res_dir");
        json.put(IosClassPlatformConfigParser.TABLE_NAME, "test_table");
        return json;
    }
}
