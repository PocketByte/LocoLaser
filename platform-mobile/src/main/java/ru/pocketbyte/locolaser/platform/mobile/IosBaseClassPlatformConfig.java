package ru.pocketbyte.locolaser.platform.mobile;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;

public abstract class IosBaseClassPlatformConfig extends BasePlatformConfig {

    private String mTableName;

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
        return "Str";
    }

    public String getTableName() {
        return mTableName;
    }

    public void setTableName(String tableName) {
        this.mTableName = tableName;
    }
}
