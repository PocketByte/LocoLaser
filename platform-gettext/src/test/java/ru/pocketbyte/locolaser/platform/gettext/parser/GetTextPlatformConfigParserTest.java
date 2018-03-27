package ru.pocketbyte.locolaser.platform.gettext.parser;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.platform.gettext.GetTextPlatformConfig;
import ru.pocketbyte.locolaser.platform.gettext.parser.GetTextPlatformConfigParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetTextPlatformConfigParserTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    private GetTextPlatformConfigParser parser;

    @Before
    public void init() throws IOException {
        parser = new GetTextPlatformConfigParser();

        File workDir = tempFolder.newFolder();
        System.setProperty("user.dir", workDir.getCanonicalPath());
    }

    @Test
    public void testFromString() throws InvalidConfigException {
        PlatformConfig config = parser.parse(GetTextPlatformConfig.TYPE, true);

        assertNotNull(config);
        assertEquals(GetTextPlatformConfig.class, config.getClass());
    }

    @Test(expected = InvalidConfigException.class)
    public void testUnknownFromString() throws InvalidConfigException {
        parser.parse("invalid_platform", true);
    }

    @Test
    public void testFromJson() throws InvalidConfigException, IOException {
        BasePlatformConfig config = parser.parse(prepareTestPlatformJson(), true);

        assertNotNull(config);
        assertEquals(GetTextPlatformConfig.class, config.getClass());

        assertEquals("test_res", config.getResourceName());
        assertEquals(new File("test_res_dir").getCanonicalPath(),
                config.getResourcesDir().getCanonicalPath());
    }

    @Test(expected = InvalidConfigException.class)
    public void testFromJsonNoType() throws InvalidConfigException, IOException {
        JSONObject json = prepareTestPlatformJson();
        json.remove(PlatformConfigParser.PLATFORM_TYPE);

        parser.parse(json, true);
    }

    @Test
    public void testFromJsonOnlyType() throws InvalidConfigException {
        JSONObject json = new JSONObject();
        json.put(PlatformConfigParser.PLATFORM_TYPE, GetTextPlatformConfig.TYPE);
        BasePlatformConfig config = parser.parse(json, true);

        assertNotNull(config);
        assertEquals(GetTextPlatformConfig.class, config.getClass());
    }

    @Test(expected = InvalidConfigException.class)
    public void testFromInvalidClass() throws InvalidConfigException {
        parser.parse(new ArrayList<String>(), true);
    }

    private JSONObject prepareTestPlatformJson() {
        JSONObject json = new JSONObject();
        json.put(PlatformConfigParser.PLATFORM_TYPE, GetTextPlatformConfig.TYPE);
        json.put(GetTextPlatformConfigParser.RESOURCE_NAME, "test_res");
        json.put(GetTextPlatformConfigParser.RESOURCES_DIR, "test_res_dir");
        return json;
    }
}
