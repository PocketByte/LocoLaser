/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.resources.ResourcesConfig

import java.io.File

/**
 * @author Denis Shurygin
 */
open class MockResourcesConfig : ResourcesConfig {

    override val type = "mock"

    override val resources = MockResources(File("./"), "mock", null)

    override val defaultTempDir = File("./temp/mock/")
}