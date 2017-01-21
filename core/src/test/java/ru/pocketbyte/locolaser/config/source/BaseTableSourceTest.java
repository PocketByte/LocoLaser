/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source;

import org.junit.Test;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.testutils.mock.MockDataSet;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Denis Shurygin
 */
public class BaseTableSourceTest {

    @Test
    public void testReadNoQuantity() throws Exception {
        MockDataSet dataSet = new MockDataSet(new String[] {"en", "ru"});
        dataSet.add("key1", null, null, new String[]{"value1_1", "value1_2"});
        dataSet.add("key2", null, "Some comment", new String[]{"value2_1", "value2_2"});

        BaseTableSourceConfig sourceConfig = new BaseTableSourceConfigImpl(dataSet);
        sourceConfig.setKeyColumn("key");
        sourceConfig.setLocaleColumns(new HashSet<>(Arrays.asList("en", "ru")));

        Source source = sourceConfig.open();
        Source.ReadResult result = source.read();
        assertNotNull(result);

        assertEquals(2, result.items.size()); // 2 locales
        assertEquals(0, result.missedValues.size()); // no missed values

        // =============
        // Locale EN
        ResLocale resLocaleEn = result.items.get("en");
        assertNotNull(resLocaleEn);

        ResItem resItem1_1 = resLocaleEn.get("key1");
        assertNotNull(resItem1_1);
        ResValue resValue1_1 = resItem1_1.valueForQuantity(Quantity.OTHER);
        assertEquals("value1_1", resValue1_1.value);
        assertNull(resValue1_1.comment);

        ResItem resItem2_1 = resLocaleEn.get("key2");
        assertNotNull(resItem2_1);
        ResValue resValue2_1 = resItem2_1.valueForQuantity(Quantity.OTHER);
        assertEquals("value2_1", resValue2_1.value);
        assertEquals("Some comment", resValue2_1.comment);

        // =============
        // Locale RU
        ResLocale resLocaleRu = result.items.get("ru");
        assertNotNull(resLocaleRu);

        ResItem resItem1_2 = resLocaleRu.get("key1");
        assertNotNull(resItem1_2);
        ResValue resValue1_2 = resItem1_2.valueForQuantity(Quantity.OTHER);
        assertEquals("value1_2", resValue1_2.value);
        assertNull(resValue1_2.comment);

        ResItem resItem2_2 = resLocaleRu.get("key2");
        assertNotNull(resItem2_2);
        ResValue resValue2_2 = resItem2_2.valueForQuantity(Quantity.OTHER);
        assertEquals("value2_2", resValue2_2.value);
        assertEquals("Some comment", resValue2_2.comment);
    }

    @Test
    public void testReadQuantity() throws Exception {
        MockDataSet dataSet = new MockDataSet(new String[] {"en"});
        dataSet.add("key1", null, null, new String[]{"value1"});
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", new String[]{"value2"});
        dataSet.add("key2", Quantity.FEW.toString(), "Some comment", new String[]{"value"});

        BaseTableSourceConfig sourceConfig = new BaseTableSourceConfigImpl(dataSet);
        sourceConfig.setKeyColumn("key");
        sourceConfig.setLocaleColumns(new HashSet<>(Arrays.asList("en")));

        Source source = sourceConfig.open();
        Source.ReadResult result = source.read();
        assertNotNull(result);

        assertEquals(1, result.items.size()); // 1 locale
        assertEquals(0, result.missedValues.size()); // no missed values

        ResLocale resLocaleEn = result.items.get("en");
        assertNotNull(resLocaleEn);

        ResItem resItem1 = resLocaleEn.get("key1");
        assertNotNull(resItem1);
        assertEquals(2, resItem1.values.size());
        ResValue resValue1_1 = resItem1.valueForQuantity(Quantity.OTHER);
        assertEquals("value1", resValue1_1.value);
        assertNull(resValue1_1.comment);

        ResValue resValue1_2 = resItem1.valueForQuantity(Quantity.ZERO);
        assertEquals("value2", resValue1_2.value);
        assertEquals("Some comment", resValue1_2.comment);
    }

    @Test
    public void testReadMissedValues() throws Exception {
        MockDataSet dataSet = new MockDataSet(new String[] {"en", "ru"});
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", new String[]{"value2", null});
        dataSet.add("key1", null, null, new String[]{"value1", "value2"});
        dataSet.add("key2", Quantity.FEW.toString(), "Some comment", new String[]{null, "value"});

        BaseTableSourceConfig sourceConfig = new BaseTableSourceConfigImpl(dataSet);
        sourceConfig.setKeyColumn("key");
        sourceConfig.setLocaleColumns(new HashSet<>(Arrays.asList("en", "ru")));

        Source source = sourceConfig.open();
        Source.ReadResult result = source.read();
        assertNotNull(result);

        assertEquals(2, result.missedValues.size()); // 2 missed values

        for (Source.MissedValue missedValue: result.missedValues) {
            switch (missedValue.key) {
                case "key1":
                    assertEquals("ru", missedValue.locale);
                    assertEquals(Quantity.ZERO, missedValue.quantity);
                    assertNotNull(missedValue.location);
                    assert missedValue.location instanceof BaseTableSource.CellLocation;
                    BaseTableSource.CellLocation location = (BaseTableSource.CellLocation) missedValue.location;
                    assertEquals(dataSet.getColumnIndexes().locales.get("ru").intValue(), location.col);
                    assertEquals(0, location.row);
                    break;
                case "key2":
                    assertEquals("en", missedValue.locale);
                    assertEquals(Quantity.FEW, missedValue.quantity);
                    assertNotNull(missedValue.location);
                    assert missedValue.location instanceof BaseTableSource.CellLocation;
                    location = (BaseTableSource.CellLocation) missedValue.location;
                    assertEquals(dataSet.getColumnIndexes().locales.get("en").intValue(), location.col);
                    assertEquals(2, location.row);
                    break;
                default:
                    throw new Exception();
            }
        }
    }

    @Test
    public void testReadCellLocations() throws Exception {
        MockDataSet dataSet = new MockDataSet(new String[] {"en", "ru"});
        dataSet.add("key1", null, null, new String[]{"value1_1", "value1_2"});
        dataSet.add("key1", Quantity.ZERO.toString(), "Some comment", new String[]{"value2", "value1_2"});
        dataSet.add("key2", null, "Some comment", new String[]{"value2_1", "value2_2"});

        BaseTableSourceConfig sourceConfig = new BaseTableSourceConfigImpl(dataSet);
        sourceConfig.setKeyColumn("key");
        sourceConfig.setLocaleColumns(new HashSet<>(Arrays.asList("en", "ru")));

        Source source = sourceConfig.open();
        Source.ReadResult result = source.read();

        // =============
        // Locale EN
        ResLocale resLocaleEn = result.items.get("en");
        int expectedCol = dataSet.getColumnIndexes().locales.get("en");

        assertLocation(resLocaleEn.get("key1").valueForQuantity(Quantity.OTHER), expectedCol, 0);
        assertLocation(resLocaleEn.get("key1").valueForQuantity(Quantity.ZERO), expectedCol, 1);
        assertLocation(resLocaleEn.get("key2").valueForQuantity(Quantity.OTHER), expectedCol, 2);

        // =============
        // Locale RU
        ResLocale resLocaleRu = result.items.get("ru");
        expectedCol = dataSet.getColumnIndexes().locales.get("ru");

        assertLocation(resLocaleRu.get("key1").valueForQuantity(Quantity.OTHER), expectedCol, 0);
        assertLocation(resLocaleRu.get("key1").valueForQuantity(Quantity.ZERO), expectedCol, 1);
        assertLocation(resLocaleRu.get("key2").valueForQuantity(Quantity.OTHER), expectedCol, 2);
    }

    private void assertLocation(ResValue resValue, int col, int row) throws Exception {
        assertNotNull(resValue.getLocation());
        assert resValue.getLocation() instanceof BaseTableSource.CellLocation;
        assertEquals(col, ((BaseTableSource.CellLocation) resValue.getLocation()).col);
        assertEquals(row, ((BaseTableSource.CellLocation) resValue.getLocation()).row);
    }

    private static class TableSourceImpl extends BaseTableSource {

        private MockDataSet mDataSet;

        TableSourceImpl(BaseTableSourceConfig sourceConfig, MockDataSet dataSet) {
            super(sourceConfig);
            mDataSet = dataSet;
        }

        @Override
        public int getFirstRow() {
            return 0;
        }

        @Override
        public int getRowsCount() {
            return mDataSet.size();
        }

        @Override
        public String getValue(int col, int row) {
            return mDataSet.get(col, row);
        }

        @Override
        public ColumnIndexes getColumnIndexes() {
            return mDataSet.getColumnIndexes();
        }

        @Override
        public long getModifiedDate() {
            return 0;
        }

        @Override
        public void write(ResMap values) {
            // Do nothing
        }

        @Override
        public void close() {
            // Do nothing
        }
    }

    private static class BaseTableSourceConfigImpl extends BaseTableSourceConfig {

        private MockDataSet mDataSet;

        BaseTableSourceConfigImpl(MockDataSet dataSet) {
            super();
            mDataSet = dataSet;
        }

        @Override
        public String getType() {
            return "mock";
        }

        @Override
        public Source open() {
            return new TableSourceImpl(this, mDataSet);
        }
    }
}
