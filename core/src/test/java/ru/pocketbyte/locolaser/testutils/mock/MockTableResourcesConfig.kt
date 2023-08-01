/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfig
import ru.pocketbyte.locolaser.resource.Resources
import java.io.File

/**
 * @author Denis Shurygin
 */
class MockTableResourcesConfig : BaseTableResourcesConfig() {

    override val type = "mock"
    override val resources: Resources = MockResources(File(""), "mock", null)
    override val defaultTempDirPath = ""

}