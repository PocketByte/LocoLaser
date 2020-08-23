/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

/**
 * @author Denis Shurygin
 */
class MockResourcesConfig : ResourcesConfig {

    override val type =  "mock"

    override val defaultTempDir = File("./")

    override val resources = object : Resources {
        override val formattingType = NoFormattingType
        override fun read(locales: Set<String>?, extraParams: ExtraParams?) = ResMap()
        override fun write(map: ResMap, extraParams: ExtraParams?) { }
        override fun summaryForLocale(locale: String) = FileSummary(0, null)
    }

}