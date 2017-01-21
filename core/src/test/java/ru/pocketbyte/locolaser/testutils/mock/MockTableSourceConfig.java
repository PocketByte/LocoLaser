/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock;

import ru.pocketbyte.locolaser.config.source.BaseTableSourceConfig;
import ru.pocketbyte.locolaser.config.source.Source;

/**
 * @author Denis Shurygin
 */
public class MockTableSourceConfig extends BaseTableSourceConfig {

    @Override
    public String getType() {
        return "mock";
    }

    @Override
    public Source open() {
        return null;
    }
}