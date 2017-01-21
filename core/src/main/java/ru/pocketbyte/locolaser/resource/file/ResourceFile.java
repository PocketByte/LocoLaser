/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.file;

import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.entity.ResMap;

import java.io.*;

/**
 * Represent resource file.
 *
 * @author Denis Shurygin
 */
public interface ResourceFile {

    /**
     * Read resources map from the resource file.
     * @return Map with resources.
     */
    public ResMap read();

    /**
     * Write resources map into resource files.
     * @param resMap Map with resources.
     * @throws IOException
     */
    public void write(ResMap resMap, WritingConfig writingConfig) throws IOException;

}