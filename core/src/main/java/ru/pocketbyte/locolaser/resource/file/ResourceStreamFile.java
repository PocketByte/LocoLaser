/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Extension of the ResourceFile that allow writing strings into resource file through open OutputStream.
 * Before writing the stream should be open via open(). When writing is completed the stream should be closed via close().
 *
 * @author Denis Shurygin
 */
public abstract class ResourceStreamFile implements ResourceFile {

    /**
     * File of the resource.
     */
    public final File mFile;

    private OutputStream mStream;

    public ResourceStreamFile(File file) {
        super();
        try {
            mFile = new File(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Open stream for writing.
     * @throws IOException
     */
    protected void open() throws IOException {
        if (mStream == null) {
            mFile.getParentFile().mkdirs();
            mStream = new FileOutputStream(mFile);
        }
    }

    /**
     * Close stream.
     * @throws IOException
     */
    protected void close() throws IOException {
        if (mStream != null) {
            mStream.flush();
            mStream.close();
            mStream = null;
        }
    }

    protected boolean isOpen() {
        return mStream != null;
    }

    /**
     * Write string into stream.
     * @param string String for writing.
     * @throws IOException
     */
    protected void writeString(String string) throws IOException {
        if (mStream == null)
            throw new IllegalStateException("You should open file before write");

        mStream.write(string.getBytes("UTF-8"));
    }

    protected void writeStringLn(String string) throws IOException {
        writeString(string);
        writeln();
    }

    /**
     * Write new line symbol into stream.
     * @throws IOException
     */
    protected void writeln() throws IOException {
        if (mStream == null)
            throw new IllegalStateException("You should open file before write");

        mStream.write(0x0D);
        mStream.write(0x0A);
    }

}
