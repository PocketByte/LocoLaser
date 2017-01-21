/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Denis Shurygin
 */
public class AbsPlatformResourcesTest {

    private static final String NAME = "name";

    private static class MockPlatformResources extends AbsPlatformResources {

        public MockPlatformResources(File resourcesDir, String name) {
            super(resourcesDir, name);
        }

        @Override
        protected ResourceFile[] getResourceFiles(Set<String> locales) {
            return null;
        }

        @Override
        public FileSummary summaryForLocale(String locale) {
            return null;
        }
    }

    private static class MockResourceFile implements ResourceFile {

        private ResMap mMap;

        MockResourceFile(ResMap map) {
            mMap = map;
        }

        @Override
        public ResMap read() {
            return mMap;
        }

        @Override
        public void write(ResMap resMap, WritingConfig writingConfig) throws IOException {
            mMap = resMap;
        }
    }

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    private File resFolder;

    @Before
    public void init() throws IOException {
        resFolder = tempFolder.newFolder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullDir() {
        new MockPlatformResources(null, NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
        new MockPlatformResources(resFolder, null);
    }

    @Test
    public void testConstructor() {
        AbsPlatformResources resources = new MockPlatformResources(resFolder, NAME);

        assertEquals(NAME, resources.getName());
        assertEquals(resFolder, resources.getDirectory());
    }

    @Test
    public void testRead() {
        final MockResourceFile res1 = new MockResourceFile(prepareResMap1());
        final MockResourceFile res2 = new MockResourceFile(prepareResMap2());

        AbsPlatformResources resources = new MockPlatformResources(resFolder, NAME) {
            @Override
            protected ResourceFile[] getResourceFiles(Set<String> locales) {
                return new ResourceFile[] {res1, res2};
            }
        };

        ResMap result = resources.read(
                new HashSet<>(
                        Arrays.asList("locale1", "locale2", "locale3", "locale4")
                ));

        ResMap expectedMap = prepareResMap1().merge(prepareResMap2());

        assertEquals(expectedMap, result);
    }

    @Test
    public void testWrite() throws IOException {
        ResMap map = new ResMap();
        final MockResourceFile res1 = new MockResourceFile(null);
        final MockResourceFile res2 = new MockResourceFile(null);

        AbsPlatformResources resources = new MockPlatformResources(resFolder, NAME) {
            @Override
            protected ResourceFile[] getResourceFiles(Set<String> locales) {
                return new ResourceFile[] {res1, res2};
            }
        };

        resources.write(map, null);
        assertTrue(map == res1.mMap);
        assertTrue(map == res2.mMap);
    }

    private ResMap prepareResMap1() {
        ResMap resMap = new ResMap();
        resMap.put("locale1", prepareResLocale1());
        resMap.put("locale2", prepareResLocale2());
        resMap.put("locale3", prepareResLocale3());
        return resMap;
    }

    private ResMap prepareResMap2() {
        ResMap resMap = new ResMap();
        resMap.put("locale1", prepareResLocale2());
        resMap.put("locale2", prepareResLocale3());
        resMap.put("locale4", prepareResLocale1());
        return resMap;
    }

    private ResLocale prepareResLocale1() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER),
                new ResValue("value2_1", null, Quantity.MANY),
                new ResValue("value2_1", null, Quantity.FEW)
        }));
        resLocale.put(prepareResItem("key2", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER),
                new ResValue("value2_1", null, Quantity.MANY),
                new ResValue("value3_1", "Comment", Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResLocale prepareResLocale2() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER),
                new ResValue("value2_2", null, Quantity.MANY),
                new ResValue("value3_2", "Comment", Quantity.TWO)
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER),
                new ResValue("value2_2", null, Quantity.MANY),
                new ResValue("value3_2", "Comment", Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResLocale prepareResLocale3() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_3", null, Quantity.OTHER),
                new ResValue("value2_3", null, Quantity.FEW),
                new ResValue("value3_3", "Comment", Quantity.TWO),
                new ResValue("value4_3", "Comment", Quantity.ZERO)
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value1_3", null, Quantity.OTHER),
                new ResValue("value3_3", "Comment", Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResItem prepareResItem(String key, ResValue[] values) {
        ResItem resItem = new ResItem(key);
        for (ResValue value: values)
            resItem.addValue(value);
        return resItem;
    }
}
