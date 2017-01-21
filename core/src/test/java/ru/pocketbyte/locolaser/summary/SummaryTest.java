/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.summary;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.Config;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.resource.PlatformResources;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Denis Shurygin
 */
public class SummaryTest {

    private static final JSONParser JSON_PARSER = new JSONParser();

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Before
    public void init() throws IOException {
        File rootFolder = tempFolder.newFolder();
        System.setProperty("user.dir", rootFolder.getCanonicalPath());
    }

    @Test
    public void testConstructorFromJson() throws ParseException, IOException {
        String jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + new FileSummary(123, "test").toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":23132123," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + new FileSummary(1233, "test1").toJson() + "," +
                "\"ru\":" + new FileSummary(1263, "test2").toJson() + "}" +
                "}";

        File file = tempFolder.newFile();
        JSONObject json = (JSONObject) JSON_PARSER.parse(jsonString);
        Summary summary = new Summary(file, json);

        assertEquals(file, summary.file);
        assertEquals(new FileSummary(123, "test"), summary.getConfigSummary());
        assertEquals(23132123, summary.getSourceModifiedDate());
        assertEquals(new FileSummary(1233, "test1"), summary.getResourceSummary("en"));
        assertEquals(new FileSummary(1263, "test2"), summary.getResourceSummary("ru"));
        assertNull(summary.getResourceSummary("none"));
    }

    @Test
    public void testConstructorFromBadJson() throws ParseException, IOException {
        Summary summary = new Summary(tempFolder.newFile(), new JSONObject());
        assertNull(summary.getConfigSummary());

        String jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":[1,2,3]," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":1," +
                "\"" + Summary.RESOURCE_FILES + "\":1" +
                "}";
        JSONObject json = (JSONObject) JSON_PARSER.parse(jsonString);
        new Summary(tempFolder.newFile(), json);

        jsonString = "{" +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":[[0], {\"key\": 431231}, [1, \"str\"]]" +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\": 1," +
                "\"ru\": [\"Str\", 1]}" +
                "}";

        json = (JSONObject) JSON_PARSER.parse(jsonString);
        new Summary(tempFolder.newFile(), json);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFile() throws ParseException, IOException {
        new Summary(null, new JSONObject());
    }

    @Test
    public void testSetGetSourceModifiedDate() throws IOException {
        Summary summary = new Summary(tempFolder.newFile(), new JSONObject());
        assertEquals(0, summary.getSourceModifiedDate());

        summary.setSourceModifiedDate(1234567);
        assertEquals(1234567, summary.getSourceModifiedDate());
    }

    @Test
    public void testSetGetResourceSummary() throws IOException {
        Summary summary = new Summary(tempFolder.newFile(), new JSONObject());
        assertNull(summary.getResourceSummary("en"));
        assertNull(summary.getResourceSummary("ru"));

        FileSummary enSummary = new FileSummary(1123, "adqwed");
        FileSummary ruSummary = new FileSummary(1523, "tnetsr");

        summary.setResourceSummary("en", enSummary);
        assertEquals(enSummary, summary.getResourceSummary("en"));
        assertNull(summary.getResourceSummary("ru"));

        summary.setResourceSummary("ru", ruSummary);
        assertEquals(enSummary, summary.getResourceSummary("en"));
        assertEquals(ruSummary, summary.getResourceSummary("ru"));
    }

    @Test
    public void testSetGetConfigSummary() throws IOException {
        Summary summary = new Summary(tempFolder.newFile(), new JSONObject());
        assertNull(summary.getConfigSummary());

        Config config = new Config();
        config.setFile(prepareTestFile("{}"));

        summary.setConfigSummary(config);

        assertEquals(new FileSummary(config.getFile()), summary.getConfigSummary());
    }

    @Test
    public void testSave() throws ParseException, IOException {

        String jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + new FileSummary(4123, "test").toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":23132123," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + new FileSummary(1233, "test1").toJson() + "," +
                "\"ru\":" + new FileSummary(1263, "test2").toJson() + "}" +
                "}";

        File file = tempFolder.newFile();
        JSONObject json = (JSONObject) JSON_PARSER.parse(jsonString);
        Summary summary = new Summary(file, json);

        summary.save();

        assertEquals(json.toJSONString(), new String(Files.readAllBytes(Paths.get(file.toURI()))));
    }

    @Test
    public void testLoad() throws ParseException, IOException {
        Config config = new Config();
        config.setFile(prepareTestFile("{}"));
        config.setPlatform(new PlatformConfig() {
            @Override
            public String getType() {
                return "mock";
            }

            @Override
            public PlatformResources getResources() {
                return null;
            }

            @Override
            public File getTempDir() {
                return new File(System.getProperty("user.dir"), "temp/");
            }
        });

        String jsonString = ((JSONObject) JSON_PARSER.parse("{" +
                "\"" + Summary.CONFIG_FILE + "\":" + new FileSummary(config.getFile()).toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":23132123," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + new FileSummary(1233, "test1").toJson() + "," +
                "\"ru\":" + new FileSummary(1263, "test2").toJson() + "}" +
                "}")).toJSONString();

        prepareTestFile(new File(config.getTempDir(), Summary.SUMMARY_FILE_NAME), jsonString);

        Summary summary = Summary.loadSummary(config);
        assertEquals(jsonString, summary.toJson().toJSONString());

    }

    private File prepareTestFile(String text) throws IOException {
        return prepareTestFile(tempFolder.newFile(), text);
    }

    private File  prepareTestFile(File file, String text) throws IOException {
        file.getParentFile().mkdirs();
        PrintWriter writer = new PrintWriter(file);
        writer.write(text);
        writer.flush();
        writer.close();
        return file;
    }
}
