package ru.pocketbyte.locolaser.platform.mobile.resource;

import ru.pocketbyte.locolaser.resource.AbsPlatformResources;

import java.io.File;

public abstract class IosBaseClassResources extends AbsPlatformResources {

    private String mTableName;

    public IosBaseClassResources(File resourcesDir, String name, String tableName) {
        super(resourcesDir, name);
        mTableName = tableName;
    }

    public void setTableName(String tableName) {
        mTableName = tableName;
    }

    public String getTableName() {
        if (mTableName != null)
            return mTableName;
        return getDefaultTableName();
    }

    protected String getDefaultTableName() {
        return "Localizable";
    }
}
