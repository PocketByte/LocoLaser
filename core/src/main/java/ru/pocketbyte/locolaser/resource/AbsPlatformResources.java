/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource;

import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.entity.ResMap;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Denis Shurygin
 */
public abstract class AbsPlatformResources implements PlatformResources {

    private final File mDir;
    private final String mName;

    public AbsPlatformResources(File dir, String name) {
        if (dir == null)
            throw new IllegalArgumentException("Resources dir must be not null");
        if (name == null)
            throw new IllegalArgumentException("Resources name must be not null");
        mDir = dir;
        mName = name;
    }

    protected abstract ResourceFile[] getResourceFiles(Set<String> locales);

    /**
     * Gets resource directory path.
     * @return Resource directory path.
     */
    public File getDirectory() {
        return mDir;
    }

    /**
     * Gets resource name.
     * @return Resource name.
     */
    protected String getName() {
        return mName;
    }

    @Override
    public ResMap read(Set<String> locales) {
        ResMap resMap = new ResMap();
        ResourceFile[] resourceFiles = getResourceFiles(locales);
        if (resourceFiles != null && resourceFiles.length > 0) {
            for (ResourceFile resourceFile: resourceFiles) {
                if (resourceFile != null) {
                    resMap.merge(resourceFile.read());
                }
            }
        }
        return resMap;
    }

    @Override
    public void write(ResMap map, WritingConfig writingConfig) throws IOException {
        ResourceFile[] resourceFiles = getResourceFiles(map.keySet());
        if (resourceFiles != null && resourceFiles.length > 0) {
            for (ResourceFile resourceFile: resourceFiles) {
                if (resourceFile != null)
                    resourceFile.write(map, writingConfig);
            }
        }
    }
}
