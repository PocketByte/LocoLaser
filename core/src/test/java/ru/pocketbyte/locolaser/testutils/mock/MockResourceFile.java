package ru.pocketbyte.locolaser.testutils.mock;

import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.entity.ResMap;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;

import java.io.IOException;

public class MockResourceFile implements ResourceFile {

    public ResMap mMap;

    public MockResourceFile(ResMap map) {
        mMap = map;
    }

    @Override
    public ResMap read() {
        return mMap;
    }

    @Override
    public void write(ResMap resMap, WritingConfig writingConfig) throws IOException {
        mMap = resMap;
    }
}
