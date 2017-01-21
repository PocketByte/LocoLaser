/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource;

import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.entity.ResMap;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.IOException;
import java.util.Set;

/**
 * Represent resources for specified platform.
 *
 * @author Denis Shurygin
 */
public interface PlatformResources {

    String BASE_LOCALE = "base";

    /**
     * Read resources map from the resource files. Keys from the result map duplicate resource locale's.
     * @return Resources map. Keys from the map duplicate resource locale's.
     */
    ResMap read(Set<String> locales);
    void write(ResMap map, WritingConfig writingConfig) throws IOException;

    FileSummary summaryForLocale(String locale);
}
