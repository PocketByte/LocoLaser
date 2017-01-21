/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source;

import ru.pocketbyte.locolaser.resource.entity.Quantity;
import ru.pocketbyte.locolaser.resource.entity.ResMap;

import java.util.List;

/**
 *
 * @author Denis Shurygin
 */
public abstract class Source {

    public abstract long getModifiedDate();

    public abstract ReadResult read();
    public abstract void write(ResMap resMap);

    public abstract void close();

    private final SourceConfig mSourceConfig;

    public Source(SourceConfig sourceConfig) {
        this.mSourceConfig = sourceConfig;
    }

    public SourceConfig getSourceConfig() {
        return mSourceConfig;
    }

    public static class ReadResult {
        public final ResMap items;
        public final List<MissedValue> missedValues;

        public ReadResult(ResMap items, List<MissedValue> missedValues) {
            this.items = items;
            this.missedValues = missedValues;
        }
    }

    public static class MissedValue {

        public final String key;
        public final String locale;
        public final Quantity quantity;
        public final ValueLocation location;

        public MissedValue(String key, String locale, Quantity quantity, ValueLocation location) {
            this.key = key;
            this.locale = locale;
            this.quantity = quantity;
            this.location = location;
        }
    }

    public static abstract class ValueLocation {

        public final Source source;

        public ValueLocation(Source source) {
            this.source = source;
        }

    }
}