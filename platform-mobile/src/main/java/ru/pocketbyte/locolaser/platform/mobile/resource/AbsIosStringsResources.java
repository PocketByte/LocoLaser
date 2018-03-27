package ru.pocketbyte.locolaser.platform.mobile.resource;

import ru.pocketbyte.locolaser.resource.AbsPlatformResources;

import java.io.File;

public abstract class AbsIosStringsResources extends AbsPlatformResources {

    public static final String RES_FILE_EXTENSION = ".strings";

    public AbsIosStringsResources(File resourcesDir, String name) {
        super(resourcesDir, name);
    }

    protected File getFileForLocale(String locale) {
        File localeFolder = new File(getDirectory(), getLocaleDirName(locale));
        localeFolder.mkdirs();
        return new File(localeFolder, getName() + RES_FILE_EXTENSION);
    }

    protected String getLocaleDirName(String locale) {
        StringBuilder builder = new StringBuilder();
        if (BASE_LOCALE.equals(locale))
            builder.append("Base");
        else
            builder.append(locale);
        builder.append(".lproj");
        return builder.toString();
    }
}