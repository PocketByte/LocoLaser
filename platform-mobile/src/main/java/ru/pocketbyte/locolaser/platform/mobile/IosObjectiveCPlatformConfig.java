package ru.pocketbyte.locolaser.platform.mobile;

import ru.pocketbyte.locolaser.platform.mobile.resource.IosObjectiveCResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

public class IosObjectiveCPlatformConfig extends IosBaseClassPlatformConfig {

    public static final String TYPE = "ios_objc";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public PlatformResources getResources() {
        return new IosObjectiveCResources(getResourcesDir(), getResourceName(), getTableName());
    }

}
