/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.platform;

import ru.pocketbyte.locolaser.config.Config;

import java.io.File;
import java.io.IOException;

/**
 * Base implementation of PlatformConfig.
 *
 * @author Denis Shurygin
 */
public abstract class BasePlatformConfig extends Config.Child implements PlatformConfig {

    private String mResourceName;
    private File mResourcesPath;
    private File mDefaultTempDir;

    /**
     * Construct new Platform object.
     */
    public BasePlatformConfig() {}

    // =================================================================================================================
    // ========= Interface methods =====================================================================================
    // =================================================================================================================

    @Override
    public File getDefaultTempDir() {
        if (mDefaultTempDir == null)
            try {
                mDefaultTempDir = new File(new File(getDefaultTempDirPath()).getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
                mDefaultTempDir = new File(getDefaultTempDirPath());
            }
        return mDefaultTempDir;
    }

    // =================================================================================================================
    // ========= Abstract methods ======================================================================================
    // =================================================================================================================

    /**
     * Gets default temporary directory path specified for current platform.
     * @return Default temporary directory path specified for current platform.
     */
    public abstract String getDefaultTempDirPath();

    /**
     * Gets default resource directory path specified for current platform.
     * @return Default resource directory path specified for current platform.
     */
    public abstract String getDefaultResourcesPath();

    /**
     * Gets default resource name specified for current platform.
     * @return Default resource name specified for current platform.
     */
    protected abstract String getDefaultResourceName();

    // =================================================================================================================
    // ============ Setters ============================================================================================
    // =================================================================================================================

    /**
     * Sets resource name.
     * @param name Resource name or null if should be used default name.
     */
    public void setResourceName(String name) {
        this.mResourceName = name;
    }

    /**
     * Sets resource directory.
     * @param file Resource directory.
     */
    public void setResourcesDir(File file) {
        this.mResourcesPath = file;
    }

    // =================================================================================================================
    // ============ Getters ============================================================================================
    // =================================================================================================================

    /**
     * Gets resource name.
     * @return Resource name.
     */
    public String getResourceName() {
        if (mResourceName != null)
            return mResourceName;
        return getDefaultResourceName();
    }

    /**
     * Gets resource directory file.
     * @return Resource directory file.
     */
    public File getResourcesDir() {
        if (mResourcesPath == null) {
            try {
                mResourcesPath = new File(new File(getDefaultResourcesPath()).getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
                mResourcesPath = new File(getDefaultResourcesPath());
            }
        }
        return mResourcesPath;
    }

    // =================================================================================================================
    // ========= Protected methods =====================================================================================
    // =================================================================================================================


    // =================================================================================================================
    // ========= Private methods =======================================================================================
    // =================================================================================================================

}