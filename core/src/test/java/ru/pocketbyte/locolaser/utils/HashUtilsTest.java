/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * @author Denis Shurygin
 */
public class HashUtilsTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testMD5ChecksumForSameFiles() throws IOException {
        String fileContent = "text1 text2 text3 tex4 text5 te6xt te7xt";

        File file_1 = prepareTestFile(fileContent);
        assertEquals(HashUtils.getMD5Checksum(file_1), HashUtils.getMD5Checksum(file_1));

        File file_2 = prepareTestFile(fileContent);
        assertEquals(HashUtils.getMD5Checksum(file_1), HashUtils.getMD5Checksum(file_2));
    }

    @Test
    public void testMD5ChecksumForNotSameFiles() throws IOException {
        String fileContent1 = "text1 text2 text3 tex4 text5 te6xt te7xt";
        String fileContent2 = "text1 text2 text3 tex4 text5 te6xt te7xt another";

        assertNotEquals(fileContent1, fileContent2);

        File file_1 = prepareTestFile(fileContent1);
        File file_2 = prepareTestFile(fileContent2);

        assertNotEquals(HashUtils.getMD5Checksum(file_1), HashUtils.getMD5Checksum(file_2));
    }

    @Test
    public void testMD5ChecksumForNotExistsFile() throws IOException {
        File file = tempFolder.newFile();
        assertTrue(file.delete());

        assertNull(HashUtils.getMD5Checksum(file));
    }

    @Test
    public void testMD5ChecksumForNullFile() throws IOException {
        assertNull(HashUtils.getMD5Checksum(null));
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
