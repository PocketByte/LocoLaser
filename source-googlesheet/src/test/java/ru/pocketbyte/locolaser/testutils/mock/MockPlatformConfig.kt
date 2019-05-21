/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

/**
 * @author Denis Shurygin
 */
class MockPlatformConfig : PlatformConfig {

    override val type =  "mock"

    override val defaultTempDir = File("./")

    override val resources = object : PlatformResources {
        override fun read(locales: Set<String>) = ResMap()
        override fun write(map: ResMap, writingConfig: WritingConfig?) { }
        override fun summaryForLocale(locale: String) = FileSummary(0, null)
    }

}