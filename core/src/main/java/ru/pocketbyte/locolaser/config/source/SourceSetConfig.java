/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source;

import ru.pocketbyte.locolaser.utils.LogUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Denis Shurygin
 */
public class SourceSetConfig implements SourceConfig {

    private Set<SourceConfig> mConfigs;
    private SourceConfig mDefaultSourceConfig;
    private Set<String> mLocales;

    public SourceSetConfig(Set<SourceConfig> configs, SourceConfig defaultSourceConfig) {
        mConfigs = configs;
        mDefaultSourceConfig = defaultSourceConfig;
        mLocales = new HashSet<>();
        for (SourceConfig config: mConfigs)
            mLocales.addAll(config.getLocales());
    }

    @Override
    public String getType() {
        return "set";
    }

    @Override
    public SourceSet open() {
        Source defaultSource = null;
        Set<Source> sources = new HashSet<>(mConfigs.size());
        for (SourceConfig config: mConfigs) {
            Source source = config.open();
            if (source == null) {
                LogUtils.err("Failed to open source. Source: " + config.toString());
                for (Source sourceToClose: sources)
                    sourceToClose.close();
                return null;
            }
            sources.add(source);

            if (config == mDefaultSourceConfig)
                defaultSource = source;
        }
        return new SourceSet(this, sources, defaultSource);
    }

    @Override
    public Set<String> getLocales() {
        return mLocales;
    }
}
