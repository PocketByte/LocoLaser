package ru.pocketbyte.locolaser.platform.kotlinmobile;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinInterfaceResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

public class KotlinInterfacePlatformConfig extends BasePlatformConfig {

    public static final String TYPE = "kotlin-interface";

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
        return "ru.pocketbyte.locolaser.StrInterface";
    }

    @Override
    public PlatformResources getResources() {
        return new KotlinInterfaceResources(getResourcesDir(), getResourceName());
    }
}
