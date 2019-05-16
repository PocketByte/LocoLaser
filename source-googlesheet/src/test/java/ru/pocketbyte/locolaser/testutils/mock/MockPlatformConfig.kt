/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.resource.PlatformResources

import java.io.File

/**
 * @author Denis Shurygin
 */
class MockPlatformConfig : PlatformConfig {

    override fun getType(): String {
        return "mock"
    }

    override fun getResources(): PlatformResources? {
        return null
    }

    override fun getDefaultTempDir(): File? {
        return null
    }
}