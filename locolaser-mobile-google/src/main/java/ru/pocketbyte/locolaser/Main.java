/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser;

import org.json.simple.parser.ParseException;
import ru.pocketbyte.locolaser.config.parser.ConfigParser;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;

import java.io.IOException;

/**
 * @author Denis Shurygin
 */
public class Main {

    public static void main(String[] args) {
        try {
            ConfigParser parser = new ConfigParser(new SourceConfigParserImpl(), new PlatformConfigParserImpl());
            if(!LocoLaser.localize(parser.fromArguments(args)))
                System.exit(1);
        } catch (InvalidConfigException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(1);
        } catch (IOException | ParseException e) {
            System.err.print("ERROR: ");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
