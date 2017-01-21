/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.platform;

import ru.pocketbyte.locolaser.resource.PlatformResources;

import java.io.File;

/**
 * Configuration object that contains information about localization rules for specified platform.
 *
 * @author Denis Shurygin
 */
public interface PlatformConfig {

    /**
     * Gets name of the platform.
     * @return Name of the platform.
     */
    public String getType();

    // TODO docs
    public PlatformResources getResources();

    /**
     * Gets temporary directory.
     * @return Temporary directory.
     */
    public File getTempDir();
}
