package ru.pocketbyte.locolaser.config.source;

import ru.pocketbyte.locolaser.resource.entity.ResMap;

public class EmptySource extends Source {

    public EmptySource(SourceConfig sourceConfig) {
        super(sourceConfig);
    }

    @Override
    public long getModifiedDate() {
        return 0;
    }

    @Override
    public ReadResult read() {
        return new ReadResult(new ResMap(), null);
    }

    @Override
    public void write(ResMap resMap) {
        // do nothing
    }

    @Override
    public void close() {
        // do nothing
    }
}
