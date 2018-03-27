package ru.pocketbyte.locolaser.platform.mobile.resource;

import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosPlistResourceFile;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.File;
import java.util.Set;

public class IosPlistResources extends AbsIosStringsResources {

    public IosPlistResources(File resourcesDir, String name) {
        super(resourcesDir, name);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        String[] localesArray = locales.toArray(new String[locales.size()]);
        ResourceFile[] resourceFiles = new ResourceFile[locales.size()];
        for (int i = 0; i < locales.size(); i++) {
            resourceFiles[i]
                    = new IosPlistResourceFile(getFileForLocale(localesArray[i]), localesArray[i]);
        }
        return resourceFiles;
    }

    @Override
    public FileSummary summaryForLocale(String locale) {
        return new FileSummary(getFileForLocale(locale));
    }
}