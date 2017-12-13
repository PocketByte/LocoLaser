/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource;

import ru.pocketbyte.locolaser.platform.mobile.resource.file.GetTextResourceFile;
import ru.pocketbyte.locolaser.resource.AbsPlatformResources;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.File;
import java.util.Set;

/**
 * @author Denis Shurygin
 */
public class GetTextResources extends AbsPlatformResources {

    public GetTextResources(File resourcesDir, String fileName) {
        super(resourcesDir, fileName);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        String[] localesArray = locales.toArray(new String[locales.size()]);
        ResourceFile[] resourceFiles = new ResourceFile[locales.size()];
        for (int i = 0; i < locales.size(); i++) {
            resourceFiles[i] = new GetTextResourceFile(getFileForLocale(localesArray[i]), localesArray[i]);
        }
        return resourceFiles;
    }

    @Override
    public FileSummary summaryForLocale(String locale) {
        return new FileSummary(getFileForLocale(locale));
    }

    private File getFileForLocale(String locale) {
        File localeFolder = new File(getDirectory(), getLocaleDirName(locale));
        localeFolder.mkdirs();
        return new File(localeFolder, getName() + ".xml");
    }

    private String getLocaleDirName(String locale) {
        StringBuilder builder = new StringBuilder("values");
        if (BASE_LOCALE.equals(locale))
            builder.append("/");
        else
            builder.append("-").append(locale).append("/");
        return builder.toString();
    }
}
