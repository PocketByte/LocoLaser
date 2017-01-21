/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.resource.AndroidResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

/**
 * Android platform configuration.
 *
 * @author Denis Shurygin
 */
public class AndroidPlatformConfig extends BasePlatformConfig {
    public static final String TYPE = "android";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getDefaultTempDirPath() {
        return "./build/tmp/";
    }

    @Override
    public String getDefaultResourcesPath() {
        return "./src/main/res/";
    }

    @Override
    protected String getDefaultResourceName() {
        return "strings";
    }

    @Override
    public PlatformResources getResources() {
        return new AndroidResources(getResourcesDir(), getResourceName());
    }

}