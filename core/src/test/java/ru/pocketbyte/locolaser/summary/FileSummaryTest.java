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
import ru.pocketbyte.locolaser.utils.HashUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * @author Denis Shurygin
 */
public class FileSummaryTest {

    private static final JSONParser JSON_PARSER = new JSONParser();

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    private long mBytes;
    private String mHash;

    @Before
    public void init() {
        mBytes = 100;
        mHash = "testtesttest";
    }

    @Test
    public void testMockJson() throws ParseException {
        Object jsonObject = JSON_PARSER.parse(buildMockJson(mBytes, mHash));
        assertTrue(jsonObject instanceof JSONObject);
    }


    @Test
    public void testConstructorFromParams() throws ParseException {
        FileSummary fileSummary = new FileSummary(mBytes, mHash);
        assertEquals(mBytes, fileSummary.bytes);
        assertEquals(mHash, fileSummary.hash);
    }

    @Test
    public void testConstructorFromJson() throws ParseException {
        FileSummary fileSummary = new FileSummary((JSONObject) JSON_PARSER.parse(buildMockJson(mBytes, mHash)));
        assertEquals(mBytes, fileSummary.bytes);
        assertEquals(mHash, fileSummary.hash);
    }

    @Test
    public void testConstructorFromBadJson() throws ParseException, IOException {
        JSONObject json = new JSONObject();
        FileSummary fileSummary = new FileSummary(json);
        assertEquals(0, fileSummary.bytes);
        assertNull(fileSummary.hash);

        json.put(FileSummary.BYTES, "string");
        fileSummary = new FileSummary(json);
        assertEquals(0, fileSummary.bytes);

        json.put(FileSummary.HASH, new JSONObject());
        fileSummary = new FileSummary(json);
        assertNull(fileSummary.hash);

        fileSummary = new FileSummary(json);
        assertEquals(0, fileSummary.bytes);
        assertNull(fileSummary.hash);
    }

    @Test
    public void testConstructorFromFile() throws ParseException, IOException {
        File file = prepareTestFile("test text");
        FileSummary fileSummary = new FileSummary(file);
        assertEquals(file.length(), fileSummary.bytes);
        assertEquals(HashUtils.getMD5Checksum(file), fileSummary.hash);
    }

    @Test
    public void testConstructorFromNotExistsFile() throws ParseException, IOException {
        File file = tempFolder.newFile();
        assertTrue(file.delete());

        FileSummary fileSummary = new FileSummary(file);
        assertEquals(0, fileSummary.bytes);
        assertNull(fileSummary.hash);
    }

    @Test
    public void testConstructorFromFilesArray() throws ParseException, IOException {
        File file1 = prepareTestFile("test text");
        File file2 = prepareTestFile("test text2");

        FileSummary fileSummary = new FileSummary(new File[] {file1, file2});
        assertEquals(file1.length() + file2.length(), fileSummary.bytes);
        assertEquals(HashUtils.getMD5Checksum(file1) + HashUtils.getMD5Checksum(file2), fileSummary.hash);

        fileSummary = new FileSummary(new File[] {file1, null, file2});
        assertEquals(file1.length() + file2.length(), fileSummary.bytes);
        assertEquals(HashUtils.getMD5Checksum(file1) + HashUtils.getMD5Checksum(file2), fileSummary.hash);

        assertTrue(file2.delete());
        fileSummary = new FileSummary(new File[] {file1, null, file2});
        assertEquals(file1.length(), fileSummary.bytes);
        assertEquals(HashUtils.getMD5Checksum(file1) + "null", fileSummary.hash);
    }

    @Test
    public void testEqualsToFile() throws IOException {
        File file = prepareTestFile("Test text");
        FileSummary fileSummary = new FileSummary(file);
        assertTrue(fileSummary.equalsToFile(file));
    }

    @Test
    public void testEquals() throws IOException {
        FileSummary fileSummary1 = new FileSummary(1, "hash");
        FileSummary fileSummary2 = new FileSummary(1, "hash");

        assertEquals(fileSummary1, fileSummary2);
        assertEquals(fileSummary2, fileSummary1);

        fileSummary2 = new FileSummary(2, "hash");
        assertNotEquals(fileSummary1, fileSummary2);
        assertNotEquals(fileSummary2, fileSummary1);

        fileSummary2 = new FileSummary(1, "wrong");
        assertNotEquals(fileSummary1, fileSummary2);
        assertNotEquals(fileSummary2, fileSummary1);

        // Summary with 0 bytes not equals
        fileSummary1 = new FileSummary(0, "hash");
        fileSummary2 = new FileSummary(0, "hash");
        assertNotEquals(fileSummary1, fileSummary2);
        assertNotEquals(fileSummary2, fileSummary1);

        // Summary with null hash not equals
        fileSummary1 = new FileSummary(1, null);
        fileSummary2 = new FileSummary(1, null);
        assertNotEquals(fileSummary1, fileSummary2);
        assertNotEquals(fileSummary2, fileSummary1);
    }

    @Test
    public void testToJson() {
        FileSummary summary = new FileSummary(23, "test_hash");
        JSONObject json = summary.toJson();

        assertEquals(summary.bytes, json.get(FileSummary.BYTES));
        assertEquals(summary.hash, json.get(FileSummary.HASH));
    }

    private String buildMockJson(long bytes, String hash) {
        String json = "{\"" + FileSummary.BYTES + "\":" + bytes + ",\"" + FileSummary.HASH + "\":\""+ hash + "\"}";
        System.out.print(json);
        return json;
    }

    private File prepareTestFile(String text) throws IOException {
        File file = tempFolder.newFile();
        PrintWriter writer = new PrintWriter(file);
        writer.write(text);
        writer.flush();
        writer.close();
        return file;
    }
}
