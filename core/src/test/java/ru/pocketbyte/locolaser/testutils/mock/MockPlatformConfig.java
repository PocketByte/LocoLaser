/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock;

import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.resource.PlatformResources;

import java.io.File;

/**
 * @author Denis Shurygin
 */
public class MockPlatformConfig implements PlatformConfig {

    @Override
    public String getType() {
        return "mock";
    }

    @Override
    public PlatformResources getResources() {
        return null;
    }

    @Override
    public File getTempDir() {
        return null;
    }
}