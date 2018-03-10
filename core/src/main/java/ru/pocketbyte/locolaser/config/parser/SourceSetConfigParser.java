package ru.pocketbyte.locolaser.config.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.config.source.SourceConfig;
import ru.pocketbyte.locolaser.config.source.SourceSetConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;

import java.util.LinkedHashSet;
import java.util.Set;


public class SourceSetConfigParser implements SourceConfigParser {

    private Set<SourceConfigParser> mParsers;

    public SourceSetConfigParser(Set<SourceConfigParser> parsers) {
        this.mParsers = parsers;
    }

    @Override
    public SourceConfig parse(Object sourceObject, boolean throwIfWrongType) throws InvalidConfigException {
        if (sourceObject instanceof JSONArray) {
            JSONArray sourceArray = (JSONArray) sourceObject;

            SourceConfig defaultConfig = null;
            Set<SourceConfig> configs = new LinkedHashSet<>(sourceArray.size());
            for (Object object : sourceArray) {

                SourceConfig config = this.parse(object, true);
                configs.add(config);

                if (defaultConfig == null)
                    defaultConfig = config;

            }
            return new SourceSetConfig(configs, defaultConfig);
        }
        else {
            for (SourceConfigParser parser: mParsers) {
                SourceConfig config = parser.parse(sourceObject, false);
                if (config != null)
                    return config;
            }
        }

        if (throwIfWrongType) {
            if (sourceObject instanceof String) {
                throw new InvalidConfigException("Unknown source: " + sourceObject);
            }
            else if (sourceObject instanceof JSONObject) {
                JSONObject sourceJSON = (JSONObject) sourceObject;
                String type = JsonParseUtils.getString(sourceJSON, SOURCE_TYPE, ConfigParser.SOURCE, false);
                throw new InvalidConfigException("Unknown source: " + type);
            }
            else {
                throw new InvalidConfigException("Property \"" + ConfigParser.SOURCE + "\" must be a String, JSON object or Array of Strings and JSON objects.");
            }
        }

        return null;
    }
}
