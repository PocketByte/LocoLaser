package ru.pocketbyte.locolaser.resource;

import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.entity.ResMap;
import ru.pocketbyte.locolaser.summary.FileSummary;

import java.io.IOException;
import java.util.Set;

public class PlatformSetResources implements PlatformResources {

    private Set<PlatformResources> mResources;

    public PlatformSetResources(Set<PlatformResources> resources) {
        mResources = resources;
    }

    @Override
    public ResMap read(Set<String> locales) {
        ResMap map = new ResMap();

        for (PlatformResources resource: mResources)
            map.merge(resource.read(locales));

        return map;
    }

    @Override
    public void write(ResMap map, WritingConfig writingConfig) throws IOException {
        for (PlatformResources resource: mResources)
            resource.write(map, writingConfig);
    }

    @Override
    public FileSummary summaryForLocale(String locale) {
        long bytes = 0;
        StringBuilder hash = new StringBuilder();

        for (PlatformResources resource: mResources) {
            FileSummary summary = resource.summaryForLocale(locale);
            bytes += summary.bytes;
            hash.append(summary.hash);
        }

        return new FileSummary(bytes, hash.toString());
    }
}
