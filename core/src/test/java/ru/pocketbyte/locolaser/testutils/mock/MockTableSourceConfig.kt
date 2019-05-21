/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.source.BaseTableSourceConfig
import ru.pocketbyte.locolaser.config.source.Source

/**
 * @author Denis Shurygin
 */
class MockTableSourceConfig : BaseTableSourceConfig() {

    override val type = "mock"

    override fun open(): Source? {
        return null
    }
}