package ru.pocketbyte.locolaser.platform.kotlinmobile;

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinAndroidResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

public class KotlinAndroidPlatformConfig extends KotlinBaseImplPlatformConfig {

    public static final String TYPE = "kotlin-android";

    private String mAppPackage;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected String getDefaultResourceName() {
        return "ru.pocketbyte.locolaser.StrAndroid";
    }

    @Override
    public PlatformResources getResources() {
        return new KotlinAndroidResources(getResourcesDir(), getResourceName(), getInterfaceName(), getAppPackage());
    }

    public void setAppPackage(String appPackage) {
        this.mAppPackage = appPackage;
    }

    public String getAppPackage() {
        return mAppPackage;
    }
}