package ru.pocketbyte.locolaser.testutils.mock;

import ru.pocketbyte.locolaser.resource.AbsPlatformResources;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.File;
import java.util.Set;

public class MockPlatformResources extends AbsPlatformResources {

    public MockPlatformResources(File resourcesDir, String name) {
        super(resourcesDir, name);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        return null;
    }

    @Override
    public FileSummary summaryForLocale(String locale) {
        return null;
    }
}
