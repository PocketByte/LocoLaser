/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source;

import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.utils.LogUtils;
import ru.pocketbyte.locolaser.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Denis Shurygin
 */
public abstract class BaseTableSource extends Source {

    public abstract int getFirstRow();
    public abstract int getRowsCount();
    public abstract String getValue(int col, int row);
    public abstract ColumnIndexes getColumnIndexes();

    public BaseTableSource(BaseTableSourceConfig sourceConfig) {
        super(sourceConfig);
    }

    @Override
    public BaseTableSourceConfig getSourceConfig() {
        return (BaseTableSourceConfig) super.getSourceConfig();
    }

    public ReadResult read() {

        ResMap items = new ResMap();
        List<MissedValue> missedValues = new ArrayList<>();

        int row = getFirstRow();
        while (getRowsCount() >= row) {
            String key = getValue(getColumnIndexes().key, row);
            if (!TextUtils.isEmpty(key)) {
                String comment = null;
                if (getColumnIndexes().comment > 0)
                    comment = getValue(getColumnIndexes().comment, row);

                for (String locale: getSourceConfig().getLocales()) {

                    int localeCol = getColumnIndexes().locales.get(locale);

                    if (localeCol >= 0) {
                        if (!items.containsKey(locale))
                            items.put(locale, new ResLocale());

                        ResLocale itemMap = items.get(locale);

                        String value = getValue(localeCol, row);

                        Quantity quantity = Quantity.OTHER;
                        if (getColumnIndexes().quantity > 0)
                            quantity = Quantity.fromString(getValue(getColumnIndexes().quantity, row));

                        if (!TextUtils.isEmpty(value)) {

                            ResItem item = itemMap.get(key);
                            if (item == null) {
                                item = new ResItem(key);
                                itemMap.put(item);
                            }

                            ResValue resValue = new ResValue(sourceValueToValue(value), comment, quantity);
                            resValue.setLocation(
                                    new CellLocation(this, localeCol, row));
                            item.addValue(resValue);
                        } else {
                            LogUtils.warn("\rValue not found! Locale= " + locale + ", key= " + key + ".");
                            missedValues.add(new MissedValue(key, locale, quantity,
                                    new CellLocation(this, localeCol, row)));
                        }
                    }
                }
            }
            row++;
        }

        return new ReadResult(items, missedValues);
    }


    /**
     * Converts common value to source value format
     * @param value Value that should be converted
     * @return Source value format
     */
    public String valueToSourceValue(String value) {
        return value;
    }

    /**
     * Converts source value to common value format
     * @param sourceValue Source value that should be converted
     * @return Value in common format
     */
    public String sourceValueToValue(String sourceValue) {
        return sourceValue;
    }

    public static class CellLocation extends ValueLocation {

        public final int col;
        public final int row;

        public CellLocation(Source source, int col, int row) {
            super(source);
            this.col = col;
            this.row = row;
        }
    }

    public static class ColumnIndexes {
        public final int key;
        public final int quantity;
        public final int comment;
        public final Map<String, Integer> locales;

        public final int max;
        public final int min;

        public ColumnIndexes(int key, int quantity, int comment, Map<String, Integer> locales) {
            this.key = key;
            this.quantity = quantity;
            this.comment = comment;
            this.locales = locales;

            int max = -1;
            int min = -1;
            for (int index : locales.values()) {
                max = Math.max(max, index);
                min = min(min, index);
            }
            this.max = Math.max(Math.max(max, Math.max(key, comment)), 1);
            this.min = Math.max(min(min, min(key, comment)), 1);
        }

        private static int min(int a, int b) {
            if (b >= 0) {
                if (a == -1)
                    return b;
                else
                    return Math.min(a, b);
            }
            return a;
        }
    }
}
