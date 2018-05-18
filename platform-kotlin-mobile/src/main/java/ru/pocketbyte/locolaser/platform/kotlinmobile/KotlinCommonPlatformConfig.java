package ru.pocketbyte.locolaser.platform.kotlinmobile;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinCommonResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

public class KotlinCommonPlatformConfig extends BasePlatformConfig {

    public static final String TYPE = "kotlin-common";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getDefaultTempDirPath() {
        return "./build/tmp/";
    }

    @Override
    public String getDefaultResourcesPath() {
        return "./src/main/kotlin/";
    }

    @Override
    protected String getDefaultResourceName() {
        return "ru.pocketbyte.locolaser.StrRepository";
    }

    @Override
    public PlatformResources getResources() {
        return new KotlinCommonResources(getResourcesDir(), getResourceName());
    }
}
