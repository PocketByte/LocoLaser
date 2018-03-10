package ru.pocketbyte.locolaser.platform.kotlinmobile.resource;

import ru.pocketbyte.locolaser.resource.AbsPlatformResources;
import ru.pocketbyte.locolaser.summary.FileSummary;


import java.io.File;

public abstract class AbsKotlinPlatformResources extends AbsPlatformResources {

    public static final String KOTLIN_FILE_EXTENSION = ".kt";

    public final String className;
    public final String classPackage;
    public final String classPackagePath;

    public AbsKotlinPlatformResources(File dir, String name) {
        super(dir, name);

        String[] nameParts = name.split("\\.");

        if (nameParts.length < 2)
            throw new IllegalArgumentException("Invalid resource name");

        this.className = nameParts[nameParts.length - 1];

        StringBuilder packageBuilder = new StringBuilder();
        StringBuilder packagePathBuilder = new StringBuilder();

        for (int i = 0; i < nameParts.length - 1; i++) {
            if (i != 0) {
                packageBuilder.append(".");
                packagePathBuilder.append("/");
            }

            packageBuilder.append(nameParts[i]);
            packagePathBuilder.append(nameParts[i]);
        }

        this.classPackage = packageBuilder.toString();
        this.classPackagePath = packagePathBuilder.toString();
    }

    @Override
    public FileSummary summaryForLocale(String locale) {
        return new FileSummary(getFile());
    }

    protected File getFile() {
        File sourceDir = getDirectory();
        sourceDir.mkdirs();
        return new File(sourceDir, this.classPackagePath + "/" + this.className + KOTLIN_FILE_EXTENSION);
    }
}
