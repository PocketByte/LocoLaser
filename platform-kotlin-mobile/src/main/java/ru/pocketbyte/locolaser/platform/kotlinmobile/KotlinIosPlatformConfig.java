package ru.pocketbyte.locolaser.platform.kotlinmobile;

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinIosResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

public class KotlinIosPlatformConfig extends KotlinBaseImplPlatformConfig {

    public static final String TYPE = "kotlin-ios";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected String getDefaultResourceName() {
        return "ru.pocketbyte.locolaser.StrIos";
    }

    @Override
    public PlatformResources getResources() {
        return new KotlinIosResources(getResourcesDir(), getResourceName(), getInterfaceName());
    }
}