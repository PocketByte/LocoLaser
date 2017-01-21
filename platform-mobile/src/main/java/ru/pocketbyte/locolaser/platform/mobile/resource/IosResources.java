/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource;

import ru.pocketbyte.locolaser.resource.AbsPlatformResources;
import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosPluralResourceFile;
import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosResourceFile;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.File;
import java.util.Set;

/**
 * Resource implementation for iOS platform.
 *
 * @author Denis Shurygin
 */
public class IosResources extends AbsPlatformResources {

    public IosResources(File resourcesDir, String fileName) {
        super(resourcesDir, fileName);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        String[] localesArray = locales.toArray(new String[locales.size()]);
        ResourceFile[] resourceFiles = new ResourceFile[locales.size() * 2];
        for (int i = 0; i < locales.size(); i++) {
            resourceFiles[i*2]
                    = new IosResourceFile(getFileForLocale(localesArray[i]), localesArray[i]);
            resourceFiles[i*2 + 1]
                    = new IosPluralResourceFile(getPluralFileForLocale(localesArray[i]), localesArray[i]);
        }
        return resourceFiles;
    }

    @Override
    public FileSummary summaryForLocale(String locale) {
        return new FileSummary(new File[] {
                getFileForLocale(locale),
                getPluralFileForLocale(locale)
        });
    }

    private File getFileForLocale(String locale) {
        File localeFolder = new File(getDirectory(), getLocaleDirName(locale));
        localeFolder.mkdirs();
        return new File(localeFolder, getName() + ".strings");
    }

    private File getPluralFileForLocale(String locale) {
        File localeFolder = new File(getDirectory(), getLocaleDirName(locale));
        localeFolder.mkdirs();
        return new File(localeFolder, getName() + ".stringsdict");
    }

    private String getLocaleDirName(String locale) {
        StringBuilder builder = new StringBuilder();
        if (BASE_LOCALE.equals(locale))
            builder.append("Base");
        else
            builder.append(locale);
        builder.append(".lproj");
        return builder.toString();
    }

}
