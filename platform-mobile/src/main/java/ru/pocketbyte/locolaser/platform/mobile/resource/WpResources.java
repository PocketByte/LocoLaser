/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource;

import ru.pocketbyte.locolaser.resource.AbsPlatformResources;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.File;
import java.util.Set;

/**
 * @author Denis Shurygin
 */
public class WpResources extends AbsPlatformResources {

    public WpResources(File resourcesDir, String fileName) {
        super(resourcesDir, fileName);
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
