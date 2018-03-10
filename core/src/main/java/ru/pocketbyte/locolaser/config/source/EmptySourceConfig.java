package ru.pocketbyte.locolaser.config.source;

import ru.pocketbyte.locolaser.resource.PlatformResources;

import java.util.HashSet;
import java.util.Set;

public class EmptySourceConfig implements SourceConfig {

    public static final String TYPE = "null";

    private Set<String> mLocales;

    public EmptySourceConfig() {
        mLocales = new HashSet<>();
        mLocales.add(PlatformResources.BASE_LOCALE);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Source open() {
        return new EmptySource(this);
    }

    @Override
    public Set<String> getLocales() {
        return mLocales;
    }
}
