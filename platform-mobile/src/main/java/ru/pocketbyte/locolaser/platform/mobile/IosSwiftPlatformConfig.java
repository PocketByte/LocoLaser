package ru.pocketbyte.locolaser.platform.mobile;

import ru.pocketbyte.locolaser.platform.mobile.resource.IosSwiftResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

public class IosSwiftPlatformConfig extends IosBaseClassPlatformConfig {

    public static final String TYPE = "ios_swift";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public PlatformResources getResources() {
        return new IosSwiftResources(getResourcesDir(), getResourceName(), getTableName());
    }

}
