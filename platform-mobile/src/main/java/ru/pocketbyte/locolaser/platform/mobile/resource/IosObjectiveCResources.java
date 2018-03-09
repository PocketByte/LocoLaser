package ru.pocketbyte.locolaser.platform.mobile.resource;

import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosObjectiveCHResourceFile;
import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosObjectiveCMResourceFile;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.File;
import java.util.Set;

public class IosObjectiveCResources extends IosBaseClassResources {

    public static final String OBJC_H_FILE_EXTENSION = ".h";
    public static final String OBJC_M_FILE_EXTENSION = ".m";

    public IosObjectiveCResources(File resourcesDir, String name, String tableName) {
        super(resourcesDir, name, tableName);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        return new ResourceFile[] {
                new IosObjectiveCHResourceFile(getObjcHFile(), getName()),
                new IosObjectiveCMResourceFile(getObjcMFile(), getName(), getTableName())
        };
    }

    @Override
    public FileSummary summaryForLocale(String locale) {
        return new FileSummary(new File[] {
                getObjcHFile(),
                getObjcMFile()
        });
    }

    private File getObjcHFile() {
        File sourceDir = getDirectory();
        sourceDir.mkdirs();
        return new File(sourceDir, getName() + OBJC_H_FILE_EXTENSION);
    }

    private File getObjcMFile() {
        File sourceDir = getDirectory();
        sourceDir.mkdirs();
        return new File(sourceDir, getName() + OBJC_M_FILE_EXTENSION);
    }
}