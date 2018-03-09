package ru.pocketbyte.locolaser.config.platform;

import ru.pocketbyte.locolaser.config.Config;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.resource.PlatformSetResources;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

public class PlatformSetConfig extends Config.Child implements PlatformConfig {

    private Set<PlatformConfig> mConfigs;

    public PlatformSetConfig(Set<PlatformConfig> configs) {
        mConfigs = configs;
    }

    @Override
    public String getType() {
        return "set";
    }

    @Override
    public PlatformResources getResources() {
        Set<PlatformResources> resources = new LinkedHashSet<>(mConfigs.size());
        for (PlatformConfig config: mConfigs) {
            resources.add(config.getResources());
        }
        return new PlatformSetResources(resources);
    }

    @Override
    public File getDefaultTempDir() {
        return mConfigs.iterator().next().getDefaultTempDir();
    }
}
