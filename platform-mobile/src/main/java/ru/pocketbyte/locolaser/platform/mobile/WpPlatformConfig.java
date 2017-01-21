/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.platform.mobile.resource.WpResources;

/**
 * Windows Phone platform configuration.
 *
 * @author Denis Shurygin
 */
public class WpPlatformConfig extends BasePlatformConfig {
    public static final String TYPE = "wp";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getDefaultTempDirPath() {
        return "../tmp/";
    }

    @Override
    public String getDefaultResourcesPath() {
        return "./Strings/";
    }

    @Override
    protected String getDefaultResourceName() {
        return "Resources.resw";
    }

    @Override
    public PlatformResources getResources() {
        return new WpResources(getResourcesDir(), getResourceName());
    }
}