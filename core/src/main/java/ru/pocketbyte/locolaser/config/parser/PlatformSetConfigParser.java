package ru.pocketbyte.locolaser.config.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.config.platform.PlatformSetConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;

import java.util.LinkedHashSet;
import java.util.Set;

public class PlatformSetConfigParser implements PlatformConfigParser {

    private Set<PlatformConfigParser> mParsers;

    public PlatformSetConfigParser(Set<PlatformConfigParser> parsers) {
        this.mParsers = parsers;
    }

    @Override
    public PlatformConfig parse(Object platformObject, boolean throwIfWrongType) throws InvalidConfigException {
        if (platformObject instanceof JSONArray) {
            JSONArray platformArray = (JSONArray) platformObject;
            Set<PlatformConfig> configs = new LinkedHashSet<>(platformArray.size());

            for (Object object : platformArray)
                configs.add(this.parse(object, true));

            return new PlatformSetConfig(configs);
        }
        else {
            for (PlatformConfigParser parser: mParsers) {
                PlatformConfig config = parser.parse(platformObject, false);
                if (config != null)
                    return config;
            }
        }

        if (throwIfWrongType) {
            if (platformObject instanceof String) {
                throw new InvalidConfigException("Unknown platform: " + platformObject);
            }
            else if (platformObject instanceof JSONObject) {
                JSONObject platformJSON = (JSONObject) platformObject;
                String type = JsonParseUtils.getString(platformJSON, PLATFORM_TYPE, ConfigParser.PLATFORM, false);
                throw new InvalidConfigException("Unknown platform: " + type);
            }
            else {
                throw new InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String, JSON object or Array of Strings and JSON objects.");
            }
        }

        return null;
    }
}
