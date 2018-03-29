/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.gettext;

import ru.pocketbyte.locolaser.platform.gettext.resource.GetTextResources;
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.resource.PlatformResources;

/**
 * Android platform configuration.
 *
 * @author Denis Shurygin
 */
public class GetTextPlatformConfig extends BasePlatformConfig {
    public static final String TYPE = "gettext";

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
        return "./";
    }

    @Override
    protected String getDefaultResourceName() {
        return "messages";
    }

    @Override
    public PlatformResources getResources() {
        return new GetTextResources(getResourcesDir(), getResourceName());
    }

}