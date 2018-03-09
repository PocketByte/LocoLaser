package ru.pocketbyte.locolaser.platform.mobile.resource;

import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosSwiftResourceFile;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.File;
import java.util.Set;

public class IosSwiftResources extends IosBaseClassResources {

    public static final String SWIFT_FILE_EXTENSION = ".swift";

    public IosSwiftResources(File resourcesDir, String name, String tableName) {
        super(resourcesDir, name, tableName);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        return new ResourceFile[] {
                new IosSwiftResourceFile(getSwiftFile(), getName(), getTableName())
        };
    }

    @Override
    public FileSummary summaryForLocale(String locale) {
        return new FileSummary(getSwiftFile());
    }

    private File getSwiftFile() {
        File sourceDir = getDirectory();
        sourceDir.mkdirs();
        return new File(sourceDir, getName() + SWIFT_FILE_EXTENSION);
    }
}
