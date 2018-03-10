package ru.pocketbyte.locolaser.platform.kotlinmobile;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;

public abstract class KotlinBaseImplementationPlatformConfig extends BasePlatformConfig {

    private String mInterfaceName;

    @Override
    public String getDefaultTempDirPath() {
        return "./build/tmp/";
    }

    @Override
    public String getDefaultResourcesPath() {
        return "./src/main/kotlin/";
    }

    public String getInterfaceName() {
        return mInterfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.mInterfaceName = interfaceName;
    }
}
