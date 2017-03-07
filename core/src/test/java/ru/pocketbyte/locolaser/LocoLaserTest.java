/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.Config;
import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.config.source.Source;
import ru.pocketbyte.locolaser.config.source.SourceConfig;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.summary.FileSummary;
import ru.pocketbyte.locolaser.summary.Summary;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Denis Shurygin
 */
public class LocoLaserTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    private Config config;
    private MockPlatformConfig platformConfig;
    private MockPlatformResources platformResources;

    private MockSource source;
    private MockSourceConfig sourceConfig;

    private ResMap mResMap;

    @Before
    public void init() throws IOException {
        File workDir = tempFolder.newFolder();
        System.setProperty("user.dir", workDir.getCanonicalPath());

        Summary.setFactory(new Summary.Factory() {
            @Override
            public Summary loadSummary(Config config) {
                return null;
            }
        });

        ResLocale localeEn = new ResLocale();
        localeEn.put(buildItem("key1", "valueEn1"));
        localeEn.put(buildItem("key2", "valueEn2"));

        ResLocale localeRu = new ResLocale();
        localeRu.put(buildItem("key1", "valueRu1"));
        localeRu.put(buildItem("key2", "valueRu2"));

        mResMap = new ResMap();
        mResMap.put("en", localeEn);
        mResMap.put("ru", localeRu);

        platformResources = new MockPlatformResources(null, null);
        platformConfig = new MockPlatformConfig("mockPlatform", platformResources);

        source = new MockSource(new ResMap(mResMap), System.currentTimeMillis());
        sourceConfig = new MockSourceConfig("mockSource", source, new HashSet<>(Arrays.asList("en", "ru")));

        config = new Config();
        config.setFile(new File(workDir, "config.json"));
        config.setPlatform(platformConfig);
        config.setSourceConfig(sourceConfig);

        // Write config file to make it not empty
        PrintWriter writer = new PrintWriter(config.getFile());
        writer.write("{}");
        writer.flush();
        writer.close();
    }

    @After
    public void deInit() {
        Summary.setFactory(null);
    }

    // ====================================================
    // Test invalid config

    @Test(expected = IllegalArgumentException.class)
    public void testNullConfig() {
        LocoLaser.localize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPlatform() {
        config.setPlatform(null);
        LocoLaser.localize(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSources() {
        config.setSourceConfig(null);
        LocoLaser.localize(config);
    }

    // ====================================================
    // Test conflict strategy

    @Test
    public void testConflictStrategyRemoveLocal() {
        config.setConflictStrategy(Config.ConflictStrategy.REMOVE_PLATFORM);

        String locale = "en";
        String newKey = "newKey";
        // Check if original map doesn't contain new value
        assertFalse(mResMap.get(locale).containsKey(newKey));

        ResLocale resLocale = new ResLocale();
        resLocale.put(buildItem(newKey, "value"));

        platformResources.mMap = new ResMap();
        platformResources.mMap.put(locale, resLocale);

        assertTrue(LocoLaser.localize(config));
        assertEquals(platformResources.mMap, source.mMap);
    }

    @Test
    public void testConflictStrategyKeepNewLocal() {
        config.setConflictStrategy(Config.ConflictStrategy.KEEP_NEW_PLATFORM);

        String locale = "en";
        String newKey = "newKey";
        // Check if original map doesn't contain new value
        assertFalse(mResMap.get(locale).containsKey(newKey));

        ResLocale resLocale = new ResLocale();
        resLocale.put(buildItem(newKey, "value"));

        platformResources.mMap = new ResMap();
        platformResources.mMap.put(locale, resLocale);

        assertTrue(LocoLaser.localize(config));
        assertNotEquals(platformResources.mMap, source.mMap);

        platformResources.mMap.get(locale).remove(newKey);
        assertEquals(platformResources.mMap, source.mMap);
    }

    @Test
    public void testConflictStrategyExportNewLocal() {
        config.setConflictStrategy(Config.ConflictStrategy.EXPORT_NEW_PLATFORM);

        String locale = "en";
        String newKey = "newKey";
        // Check if original map doesn't contain new value
        assertFalse(mResMap.get(locale).containsKey(newKey));

        ResLocale resLocale = new ResLocale();
        resLocale.put(buildItem(newKey, "value"));

        platformResources.mMap = new ResMap();
        platformResources.mMap.put(locale, resLocale);

        assertTrue(LocoLaser.localize(config));
        assertEquals(platformResources.mMap, source.mMap);

        assertTrue(source.mMap.get(locale).containsKey(newKey));
    }


    @Test
    public void testConflictStrategyExportMissedValues() {
        config.setConflictStrategy(Config.ConflictStrategy.EXPORT_NEW_PLATFORM);

        String missedKey = "key1";
        String locale = "ru";
        Source.ValueLocation location = new Source.ValueLocation(source) {};

        platformResources.mMap = new ResMap(mResMap);
        assertNotNull(source.mMap.get(locale).remove(missedKey));

        source.mMissedValues = Collections.singletonList(
                new Source.MissedValue(
                        missedKey, locale,
                        Quantity.OTHER, location));

        assertTrue(LocoLaser.localize(config));
        assertEquals(platformResources.mMap, source.mMap);

        ResItem missedItem = source.mMap.get(locale).get(missedKey);
        assertNotNull(missedItem);
        assertTrue(missedItem.valueForQuantity(Quantity.OTHER).getLocation() == location);
    }

    // ====================================================
    // Other

    @Test
    public void testSimpleReadAndWrite() {
        assertNull(platformResources.mMap);
        assertNotNull(source.mMap);

        assertTrue(LocoLaser.localize(config));

        assertEquals(platformResources.mMap, source.mMap);
    }

    @Test
    public void testWritingConfigUsage() {
        assertNull(platformResources.mWritingConfig);
        assertNotNull(config.writingConfig);

        assertTrue(LocoLaser.localize(config));

        assertTrue(platformResources.mWritingConfig == config.writingConfig);
    }

    @Test
    public void testLocalizationNotNeeded() throws ParseException, IOException {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges();

        assertTrue(LocoLaser.localize(config));
        assertNull(platformResources.mMap);
    }

    @Test
    public void testForceImport() throws ParseException, IOException {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges();

        config.setForceImport(true);
        assertTrue(LocoLaser.localize(config));
        assertNotNull(platformResources.mMap);
        assertEquals(platformResources.mMap, source.mMap);
    }

    @Test
    public void testConfigChanged() throws ParseException, IOException {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges();

        // Override config file
        PrintWriter writer = new PrintWriter(config.getFile());
        writer.write("{\"key\":\"wrong\"}");
        writer.flush();
        writer.close();

        assertTrue(LocoLaser.localize(config));
        assertNotNull(platformResources.mMap);
        assertEquals(platformResources.mMap, source.mMap);
    }

    @Test
    public void testSourceChanged() throws ParseException, IOException {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges();

        source.mModifiedDate++;

        assertTrue(LocoLaser.localize(config));
        assertNotNull(platformResources.mMap);
        assertEquals(platformResources.mMap, source.mMap);
    }

    @Test
    public void testLocalResourceChanged() throws ParseException, IOException {
        prepareStateWhenLocalizationNotNeededBecauseNoChanges();

        String changedLocale = "en";
        FileSummary localeSummary = platformResources.mSummaryMap.get(changedLocale);

        platformResources.mSummaryMap.put(changedLocale, new FileSummary(localeSummary.bytes + 1, "hash_en"));

        assertTrue(LocoLaser.localize(config));
        assertNotNull(platformResources.mMap);
        assertNotEquals(platformResources.mMap, source.mMap);
        assertEquals(1, platformResources.mMap.size());
        assertEquals(platformResources.mMap.get(changedLocale), source.mMap.get(changedLocale));
    }

    @Test
    public void testSourceClose() {
        assertTrue(LocoLaser.localize(config));
        assertTrue(source.isClosed);
    }

    @Test
    public void testSaveSummary() throws ParseException, IOException {
        platformResources.mSummaryMap = new HashMap<>(2);
        platformResources.mSummaryMap.put("en", new FileSummary(2300000, "hash_en"));
        platformResources.mSummaryMap.put("ru", new FileSummary(2123123, "hash_ru"));

        String jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + new FileSummary(12312, "old_config").toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":"+ (source.getModifiedDate() + 1) +"," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + new FileSummary(23213, "old_en").toJson() + "," +
                "\"ru\":" + new FileSummary(24563, "old_ru").toJson() + "}" +
                "}";

        File tempDir = config.getTempDir();
        if (!tempDir.exists())
            assertTrue(tempDir.mkdirs());
        File summaryFile = new File(tempDir, Summary.SUMMARY_FILE_NAME);

        PrintWriter writer = new PrintWriter(summaryFile);
        writer.write(jsonString);
        writer.flush();
        writer.close();

        Summary.setFactory(null);

        //Check if summary saved
        Summary summary = Summary.loadSummary(config);
        assertEquals(new FileSummary(12312, "old_config"), summary.getConfigSummary());
        assertEquals(new FileSummary(23213, "old_en"), summary.getResourceSummary("en"));
        assertEquals(new FileSummary(24563, "old_ru"), summary.getResourceSummary("ru"));
        assertEquals(source.getModifiedDate() + 1, summary.getSourceModifiedDate());

        assertTrue(LocoLaser.localize(config));

        //Check if summary changed
        summary = Summary.loadSummary(config);
        assertEquals(new FileSummary(config.getFile()), summary.getConfigSummary());
        assertEquals(new FileSummary(2300000, "hash_en"), summary.getResourceSummary("en"));
        assertEquals(new FileSummary(2123123, "hash_ru"), summary.getResourceSummary("ru"));
        assertEquals(source.getModifiedDate(), summary.getSourceModifiedDate());
    }

    // ====================================================
    // Private classes and methods

    private ResItem buildItem(String key, String value) {
        ResItem item = new ResItem(key);
        item.addValue(new ResValue(value, null));
        return item;
    }

    private Summary prepareStateWhenLocalizationNotNeededBecauseNoChanges() throws IOException, ParseException {
        platformResources.mSummaryMap = new HashMap<>(2);
        platformResources.mSummaryMap.put("en", new FileSummary(23, "hash_en"));
        platformResources.mSummaryMap.put("ru", new FileSummary(2123123, "hash_ru"));

        String jsonString = "{" +
                "\"" + Summary.CONFIG_FILE + "\":" + new FileSummary(config.getFile()).toJson() + "," +
                "\"" + Summary.SOURCE_MODIFIED_DATE + "\":"+ source.getModifiedDate() +"," +
                "\"" + Summary.RESOURCE_FILES + "\":{" +
                "\"en\":" + platformResources.mSummaryMap.get("en").toJson() + "," +
                "\"ru\":" + platformResources.mSummaryMap.get("ru").toJson() + "}" +
                "}";

        final Summary summary = new Summary(tempFolder.newFile(),
                (JSONObject) JsonParseUtils.JSON_PARSER.parse(jsonString));

        Summary.setFactory(new Summary.Factory() {
            @Override
            public Summary loadSummary(Config config) {
                return summary;
            }
        });

        return summary;
    }

    private class MockPlatformConfig implements PlatformConfig {

        private String mName;
        private PlatformResources mResources;

        MockPlatformConfig(String name, PlatformResources resources) {
            mName = name;
            mResources = resources;
        }

        @Override
        public String getType() {
            return mName;
        }

        @Override
        public PlatformResources getResources() {
            return mResources;
        }

        @Override
        public File getTempDir() {
            return new File(System.getProperty("user.dir"), "./temp/");
        }
    }

    private class MockPlatformResources implements PlatformResources {

        private ResMap mMap;
        private Map<String, FileSummary> mSummaryMap;
        private WritingConfig mWritingConfig;

        MockPlatformResources(ResMap resMap, Map<String, FileSummary> summaryMap) {
            mMap = resMap;
            mSummaryMap = summaryMap;
        }

        @Override
        public ResMap read(Set<String> locales) {
            ResMap resMap = new ResMap();
            if (mMap != null) {
                for (String locale : locales) {
                    resMap.put(locale, new ResLocale(mMap.get(locale)));
                }
            }
            return resMap;
        }

        @Override
        public void write(ResMap map, WritingConfig writingConfig) throws IOException {
            mMap = new ResMap(map);
            mWritingConfig = writingConfig;
        }

        @Override
        public FileSummary summaryForLocale(String locale) {
            if (mSummaryMap != null)
                return mSummaryMap.get(locale);
            return null;
        }
    }

    private static class MockSourceConfig implements SourceConfig {

        private String mType;
        private Source mSource;
        private Set<String> mLocales;

        MockSourceConfig(String type, Source source, Set<String> locales) {
            mType = type;
            mSource = source;
            mLocales = locales;
        }

        @Override
        public String getType() {
            return mType;
        }

        @Override
        public Source open() {
            return mSource;
        }

        @Override
        public Set<String> getLocales() {
            return mLocales;
        }
    }

    private class MockSource extends Source {

        private boolean isClosed = false;
        private ResMap mMap;
        private List<MissedValue> mMissedValues;
        private long mModifiedDate;

        public MockSource(ResMap resMap, long modifiedDate) {
            super(null);
            mMap = resMap;
            mModifiedDate = modifiedDate;
        }

        @Override
        public long getModifiedDate() {
            return mModifiedDate;
        }

        @Override
        public ReadResult read() {
            return new ReadResult(new ResMap(mMap), mMissedValues);
        }

        @Override
        public void write(ResMap resMap) {
            if (mMap == null)
                mMap = new ResMap(resMap);
            else
                mMap.merge(resMap);
        }

        @Override
        public void close() {
            if (isClosed)
                throw new IllegalStateException("Source should be closed only once");
            isClosed = true;
        }
    }
}
