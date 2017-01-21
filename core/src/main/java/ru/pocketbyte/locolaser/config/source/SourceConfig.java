/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source;

import java.util.Set;

/**
 * Configuration object that contains information about source.
 *
 * @author Denis Shurygin
 */
public interface SourceConfig {

    public String getType();

    /**
     * Open source that represents by this configuration.
     * @return Source that represents by this configuration or null if configuration is invalid.
     */
    public abstract Source open();

    /**
     * Gets list of the locales that contain in the source.
     * @return List of the locales that contain in the source.
     */
    public Set<String> getLocales();

}
