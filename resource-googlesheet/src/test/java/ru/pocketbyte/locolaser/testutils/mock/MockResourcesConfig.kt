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

import java.io.File

/**
 * @author Denis Shurygin
 */
class MockResourcesConfig : ResourcesConfig {

    override val type =  "mock"

    override val defaultTempDirPath = "./"

    override val resources = object : Resources {
        override val formattingType = NoFormattingType
        override fun read(locales: Set<String>?, extraParams: ExtraParams?) = ResMap()
        override fun write(resMap: ResMap, extraParams: ExtraParams?) { }
        override fun allFiles(locales: Set<String>): List<File> = emptyList()
    }

}