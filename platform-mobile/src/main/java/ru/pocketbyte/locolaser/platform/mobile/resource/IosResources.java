/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource;

import ru.pocketbyte.locolaser.platform.mobile.resource.file.*;
import ru.pocketbyte.locolaser.resource.AbsPlatformResources;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.File;
import java.util.Set;

/**
 * Resource implementation for iOS platform.
 *
 * @author Denis Shurygin
 */
public class IosResources extends AbsPlatformResources {

    public static final String RES_FILE_EXTENSION = ".strings";
    public static final String PLURAL_RES_FILE_EXTENSION = ".stringsdict";
    public static final String SWIFT_FILE_EXTENSION = ".swift";
    public static final String OBJC_H_FILE_EXTENSION = ".h";
    public static final String OBJC_M_FILE_EXTENSION = ".m";

    private File mSourceDir;
    private String mSwiftClassName;
    private String mObjcClassName;

    public IosResources(File resourcesDir, String name) {
        super(resourcesDir, name);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        String[] localesArray = locales.toArray(new String[locales.size()]);
        ResourceFile[] resourceFiles = new ResourceFile[locales.size() * 2
                + (isHasSwiftClass() ? 1 : 0)
                + (isHasObjcClass() ? 2 : 0)];
        for (int i = 0; i < locales.size(); i++) {
            resourceFiles[i*2]
                    = new IosResourceFile(getFileForLocale(localesArray[i]), localesArray[i]);
            resourceFiles[i*2 + 1]
                    = new IosPluralResourceFile(getPluralFileForLocale(localesArray[i]), localesArray[i]);
        }
        int totalFiles = locales.size() * 2;
        if (isHasSwiftClass()) {
            resourceFiles[totalFiles]
                    = new IosSwiftResourceFile(getSwiftFile(mSwiftClassName), mSwiftClassName, getName());
            totalFiles++;
        }
        if (isHasObjcClass()) {
            resourceFiles[totalFiles]
                    = new IosObjectiveCHResourceFile(getObjcHFile(mObjcClassName), mObjcClassName);
            resourceFiles[totalFiles + 1]
                    = new IosObjectiveCMResourceFile(getObjcMFile(mObjcClassName), mObjcClassName, getName());
        }
        return resourceFiles;
    }

    @Override
    public FileSummary summaryForLocale(String locale) {
        return new FileSummary(new File[] {
                getFileForLocale(locale),
                getPluralFileForLocale(locale)
        });
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

    private File getFileForLocale(String locale) {
        File localeFolder = new File(getDirectory(), getLocaleDirName(locale));
        localeFolder.mkdirs();
        return new File(localeFolder, getName() + RES_FILE_EXTENSION);
    }

    private File getPluralFileForLocale(String locale) {
        File localeFolder = new File(getDirectory(), getLocaleDirName(locale));
        localeFolder.mkdirs();
        return new File(localeFolder, getName() + PLURAL_RES_FILE_EXTENSION);
    }

    private File getSwiftFile(String className) {
        if (mSourceDir != null)
            mSourceDir.mkdirs();
        return new File(mSourceDir, className + SWIFT_FILE_EXTENSION);
    }

    private File getObjcHFile(String className) {
        if (mSourceDir != null)
            mSourceDir.mkdirs();
        return new File(mSourceDir, className + OBJC_H_FILE_EXTENSION);
    }

    private File getObjcMFile(String className) {
        if (mSourceDir != null)
            mSourceDir.mkdirs();
        return new File(mSourceDir, className + OBJC_M_FILE_EXTENSION);
    }

    private String getLocaleDirName(String locale) {
        StringBuilder builder = new StringBuilder();
        if (BASE_LOCALE.equals(locale))
            builder.append("Base");
        else
            builder.append(locale);
        builder.append(".lproj");
        return builder.toString();
    }

    private boolean isHasSwiftClass() {
        return mSwiftClassName != null;
    }

    private boolean isHasObjcClass() {
        return mObjcClassName != null;
    }
}
