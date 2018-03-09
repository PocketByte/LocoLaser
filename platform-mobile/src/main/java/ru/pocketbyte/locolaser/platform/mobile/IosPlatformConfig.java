/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.resource.IosResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

import java.io.File;

/**
 * iOS platform configuration.
 *
 * @author Denis Shurygin
 */
public class IosPlatformConfig extends BasePlatformConfig {
    public static final String TYPE = "ios";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getDefaultTempDirPath() {
        return "../DerivedData/LocoLaserTemp/";
    }

    @Override
    public String getDefaultResourcesPath() {
        return "./";
    }

    @Override
    protected String getDefaultResourceName() {
        return "Localizable";
    }

    @Override
    public PlatformResources getResources() {
        return new IosResources(getResourcesDir(), getResourceName());
    }

}