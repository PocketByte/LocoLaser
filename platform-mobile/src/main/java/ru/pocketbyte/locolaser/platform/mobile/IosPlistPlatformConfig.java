package ru.pocketbyte.locolaser.platform.mobile;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.resource.IosPlistResources;
import ru.pocketbyte.locolaser.resource.PlatformResources;

/**
 * iOS Info.plist localisation configuration.
 *
 * @author Denis Shurygin
 */
public class IosPlistPlatformConfig extends BasePlatformConfig {
    public static final String TYPE = "ios_plist";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getDefaultTempDirPath() {
        return "../DerivedData/LocoLaserTemp/";
    }

    @Override
    public String getDefaultResourcesPath() {
        return "./";
    }

    @Override
    protected String getDefaultResourceName() {
        return "InfoPlist";
    }

    @Override
    public PlatformResources getResources() {
        return new IosPlistResources(getResourcesDir(), getResourceName());
    }

}