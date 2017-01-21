/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock;

import ru.pocketbyte.locolaser.config.source.BaseTableSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Denis Shurygin
 */
public class MockDataSet {

    private final List<DataRow> mDataList = new ArrayList<>();
    private final int mLocalesCount;
    private BaseTableSource.ColumnIndexes mColumnIndexes;

    public MockDataSet(String[] locales) {
        mLocalesCount = locales.length;

        Map<String, Integer> localeIndexes = new HashMap<>(locales.length);
        for (int i = 0; i < mLocalesCount; i++)
            localeIndexes.put(locales[i], 4 + i);

        mColumnIndexes = new BaseTableSource.ColumnIndexes(1, 2, 3, localeIndexes);
    }

    public void add(String key, String quantity, String comment, String[] locales) {
        mDataList.add(new DataRow(key, quantity, comment, locales));
    }

    public String get(int col, int row) {
        if (row < mDataList.size() && row >= 0) {
            DataRow dataRow = mDataList.get(row);
            if (dataRow != null) {
                switch (col) {
                    case 1:
                        return dataRow.key;
                    case 2:
                        return dataRow.quantity;
                    case 3:
                        return dataRow.comment;
                    default:
                        if (col >= 4 && col - 4 < dataRow.locales.length) {
                            return dataRow.locales[col - 4];
                        }
                        break;
                }
            }
        }
        return null;
    }

    public int size() {
        return mDataList.size();
    }

    public BaseTableSource.ColumnIndexes getColumnIndexes() {
        return mColumnIndexes;
    }

    private static class DataRow {
        public final String key;
        public final String quantity;
        public final String comment;
        public final String[] locales;

        public DataRow(String key, String quantity, String comment, String[] locales) {
            this.key = key;
            this.quantity = quantity;
            this.comment = comment;
            this.locales = locales;
        }
    }
}
