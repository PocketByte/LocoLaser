/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser;

import org.json.simple.parser.ParseException;
import ru.pocketbyte.locolaser.config.parser.*;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;

import java.io.*;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Denis Shurygin
 */
public class Main {

    public static void main(String[] args) {
        try {
            ConfigParser parser = buildParser();
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

    private static ConfigParser buildParser() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        Set<PlatformConfigParser> platformConfigParsers = new LinkedHashSet<>();
        Set<SourceConfigParser> sourceConfigParsers = new LinkedHashSet<>();

        for (URL url: getJarUrls()) {
            List<String> list;

            list = readPlatformConfigParsers(url);
            for (String className: list)
                platformConfigParsers.add((PlatformConfigParser) Class.forName(className).newInstance());

            list = readSourceConfigParsers(url);
            for (String className: list)
                sourceConfigParsers.add((SourceConfigParser) Class.forName(className).newInstance());
        }

        if (platformConfigParsers.size() == 0)
            throw new RuntimeException("Platform Config parser not found");

        sourceConfigParsers.add(new EmptySourceConfigParser());

        return new ConfigParser(
                new SourceSetConfigParser(sourceConfigParsers),
                new PlatformSetConfigParser(platformConfigParsers));
    }

    private static URL[] getJarUrls() {
        ClassLoader classLoader = Main.class.getClassLoader();
        URLClassLoader uc = null;
        if (classLoader instanceof URLClassLoader) {
            uc = (URLClassLoader)classLoader;
        }
        else {
            throw new RuntimeException("classLoader is not an instanceof URLClassLoader");
        }

        return uc.getURLs();
    }

    private static List<String> readPlatformConfigParsers(URL jarUrl) {
        return readLines(jarUrl, "/META-INF/platform_configs");
    }

    private static List<String> readSourceConfigParsers(URL jarUrl) {
        return readLines(jarUrl, "/META-INF/source_configs");
    }

    private static List<String> readLines(URL jarUrl, String filePath) {
        BufferedReader reader;
        List<String> lines = new ArrayList<>();

        String inputFilePath = "jar:file:" + jarUrl.getPath() + "!" + filePath;

        try {
            URL inputURL = new URL(inputFilePath);
            JarURLConnection conn = (JarURLConnection) inputURL.openConnection();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (MalformedURLException e) {
            System.err.println("Malformed input URL: "+ inputFilePath);
        } catch (IOException e) {
            // Do nothing
        }

        return lines;
    }
}
