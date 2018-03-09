package ru.pocketbyte.locolaser.config.parser;

import org.junit.Test;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformConfig;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertSame;

public class PlatformSetConfigParserTest {

    @Test
    public void testParseCorrectData() throws InvalidConfigException {
        PlatformConfig config1 = new MockPlatformConfig();
        PlatformConfig config2 = new MockPlatformConfig();

        PlatformSetConfigParser parser = prepareParser(config1, config2);

        assertSame(config1, parser.parse("type_1", true));
        assertSame(config2, parser.parse("type_2", true));
    }

    @Test
    public void testNullForWrongType() throws InvalidConfigException {
        assertSame(null, prepareParser().parse("wrong", false));
    }

    @Test(expected = InvalidConfigException.class)
    public void testThrowForWrongType() throws InvalidConfigException {
        assertSame(null, prepareParser().parse("wrong", true));
    }

    private PlatformSetConfigParser prepareParser() {
        return prepareParser(new MockPlatformConfig(), new MockPlatformConfig());
    }

    private PlatformSetConfigParser prepareParser(PlatformConfig config1, PlatformConfig config2) {
        Set<PlatformConfigParser> parsersSet = new LinkedHashSet<>(2);
        parsersSet.add(new TestParser("type_1", config1));
        parsersSet.add(new TestParser("type_2", config2));

        return new PlatformSetConfigParser(parsersSet);
    }

    private class TestParser implements PlatformConfigParser {

        private String mType;
        private PlatformConfig mConfig;

        TestParser(String type, PlatformConfig config) {
            mType = type;
            mConfig = config;
        }

        @Override
        public PlatformConfig parse(Object platformObject, boolean throwIfWrongType) throws InvalidConfigException {
            if (mType.equals(platformObject))
                return mConfig;

            if (throwIfWrongType)
                throw new InvalidConfigException("Invalid!");

            return null;
        }
    }
}
