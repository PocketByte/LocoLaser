/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.resource.IosResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

import java.io.File;

/**
 * iOS platform configuration.
 *
 * @author Denis Shurygin
 */
public class IosPlatformConfig extends BasePlatformConfig {
    public static final String TYPE = "ios";

    private File mSourceDir;
    private String mSwiftClassName;
    private String mObjcClassName;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getDefaultTempDirPath() {
        return "../DerivedData/LocoLaserTemp/";
    }

    @Override
    public String getDefaultResourcesPath() {
        return "./";
    }

    @Override
    protected String getDefaultResourceName() {
        return "Localizable";
    }

    @Override
    public PlatformResources getResources() {
        IosResources resources = new IosResources(getResourcesDir(), getResourceName());
        resources.setSourceDir(mSourceDir);
        resources.setSwiftClassName(mSwiftClassName);
        resources.setObjcClassName(mObjcClassName);
        return resources;
    }

    public void setSourceDir(File dir) {
        mSourceDir = dir;
    }

    public void setSwiftClassName(String className) {
        mSwiftClassName = className;
    }

    public void setObjcClassName(String className) {
        mObjcClassName = className;
    }

    public String getSwiftClassName() {
        return mSwiftClassName;
    }

    public String getObjcClassName() {
        return mObjcClassName;
    }

    public File getSourceDir() {
        return mSourceDir;
    }
}