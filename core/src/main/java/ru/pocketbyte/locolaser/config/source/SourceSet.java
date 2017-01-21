/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source;

import ru.pocketbyte.locolaser.resource.entity.ResMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Denis Shurygin
 */
public class SourceSet extends Source {

    private Set<Source> mSources;
    private Source mDeafaultSource;

    public SourceSet(SourceConfig sourceConfig, Set<Source> sources, Source defaultSource) {
        super(sourceConfig);
        mSources = sources;
        mDeafaultSource = defaultSource;
    }

    @Override
    public long getModifiedDate() {
        long date = 0;
        for (Source source: mSources)
            date = Math.max(date, source.getModifiedDate());
        return date;
    }

    @Override
    public ReadResult read() {
        List<MissedValue> missedValues = null;
        ResMap resMap = null;
        for (Source source : mSources) {
            Source.ReadResult result = source.read();
            if (result.missedValues != null) {
                if (missedValues == null)
                    missedValues = new ArrayList<>();
                missedValues.addAll(result.missedValues);
            }
            if (resMap != null)
                resMap = resMap.merge(result.items);
            else
                resMap = new ResMap(result.items);
        }
        return new ReadResult(resMap, missedValues);
    }

    @Override
    public void write(ResMap resMap) {
        mDeafaultSource.write(resMap);
    }

    @Override
    public void close() {
        for (Source source: mSources)
            source.close();
    }
}
