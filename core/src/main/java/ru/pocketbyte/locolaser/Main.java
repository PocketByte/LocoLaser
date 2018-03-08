/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser;

import org.json.simple.parser.ParseException;
import ru.pocketbyte.locolaser.config.parser.ConfigParser;
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.config.parser.SourceConfigParser;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;

import java.io.IOException;

/**
 * @author Denis Shurygin
 */
public class Main {

    public static void main(String[] args) {
        try {
            SourceConfigParser<?> sourceConfigParser = (SourceConfigParser<?>)
                    Class.forName("ru.pocketbyte.locolaser.SourceConfigParserImpl").newInstance();
            PlatformConfigParser<?> platformConfigParser = (PlatformConfigParser<?>)
                    Class.forName("ru.pocketbyte.locolaser.PlatformConfigParserImpl").newInstance();

            ConfigParser parser = new ConfigParser(sourceConfigParser, platformConfigParser);
            if(!LocoLaser.localize(parser.fromArguments(args)))
                System.exit(1);
        } catch (InvalidConfigException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(1);
        } catch (IOException | ParseException | IllegalAccessException | InstantiationException
                | ClassNotFoundException e) {
            System.err.print("ERROR: ");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
