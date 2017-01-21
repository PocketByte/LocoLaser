/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.file;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.entity.ResMap;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Denis Shurygin
 */
public class ResourceStreamFileTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    private ResourceStreamFile streamFile;

    @Before
    public void init() throws IOException {
        streamFile = new ResourceStreamFile(tempFolder.newFile()) {
            @Override
            public ResMap read() {
                return null;
            }

            @Override
            public void write(ResMap resMap, WritingConfig writingConfig) throws IOException {

            }
        };
    }

    @After
    public void deInit() throws IOException {
        streamFile.close();
    }

    @Test
    public void testOpen() throws IOException {
        assertFalse(streamFile.isOpen());

        streamFile.open();
        assertTrue(streamFile.isOpen());
    }

    @Test
    public void testOpenNoFile() throws IOException {
        assertTrue(streamFile.mFile.delete());

        streamFile.open();
        assertTrue(streamFile.isOpen());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteStringWithoutOpen() throws IOException {
        streamFile.writeString("str");
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteStringLnWithoutOpen() throws IOException {
        streamFile.writeStringLn("str");
    }

    @Test(expected = IllegalStateException.class)
    public void testWritelnWithoutOpen() throws IOException {
        streamFile.writeln();
    }

    @Test
    public void testWriteString() throws IOException {
        String testString = "test string";
        streamFile.open();
        streamFile.writeString(testString);

        String result = new String(Files.readAllBytes(Paths.get(streamFile.mFile.toURI())));
        assertEquals(testString, result);
    }

    @Test
    public void testWriteStringLn() throws IOException {
        streamFile.open();
        streamFile.writeStringLn("1");
        streamFile.writeString("2");

        List<String> result = Files.readAllLines(Paths.get(streamFile.mFile.toURI()), Charset.defaultCharset());
        assertEquals(2, result.size());
        assertEquals("1", result.get(0));
        assertEquals("2", result.get(1));
    }

    @Test
    public void testWriteln() throws IOException {
        streamFile.open();
        streamFile.writeString("1");
        streamFile.writeln();
        streamFile.writeString("2");

        List<String> result = Files.readAllLines(Paths.get(streamFile.mFile.toURI()), Charset.defaultCharset());
        assertEquals(2, result.size());
        assertEquals("1", result.get(0));
        assertEquals("2", result.get(1));
    }
}
