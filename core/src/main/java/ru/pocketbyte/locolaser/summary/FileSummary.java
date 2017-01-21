/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.summary;

import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.utils.HashUtils;

import java.io.File;
import java.io.IOException;

/**
 * Summary for single file.
 *
 * @author Denis Shurygin
 */
public class FileSummary {

    static final String BYTES = "BYTES";
    static final String HASH = "HASH";

    public final long bytes;
    public final String hash;

    public FileSummary(long bytesCount, String hash) {
        this.bytes = bytesCount;
        this.hash = hash;
    }

    public FileSummary(JSONObject json) {
        Object object = json.get(BYTES);
        if (object instanceof Long)
            bytes = (long) object;
        else
            bytes = 0;

        object = json.get(HASH);
        if (object instanceof String)
            hash = (String) object;
        else
            hash = null;
    }

    public FileSummary(File file) {
        if (file != null) {
            bytes = file.length();
            hash = getHashFromFile(file);
        }
        else {
            bytes = 0;
            hash = null;
        }
    }

    public FileSummary(File[] files) {
        long bytes = 0;
        StringBuilder hash = new StringBuilder();
        if (files != null && files.length > 0) {
            for (File file : files)
                if (file != null) {
                    bytes += file.length();
                    try {
                        String fileHash = HashUtils.getMD5Checksum(file);
                        hash.append(fileHash);
                    } catch (IOException e) {
                        // failed to get hash
                        hash.append("null");
                    }
                }
        }
        this.bytes = bytes;
        this.hash = hash.toString();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(BYTES, bytes);
        json.put(HASH, hash);
        return json;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        FileSummary summary = (FileSummary) obj;
        return bytes != 0
                && bytes == summary.bytes
                && hash != null
                && hash.equals(summary.hash);
    }

    /**
     * Check if summary of the specified file is equal this summary.
     * @param file File for check.
     * @return {@code true} if summary of the argument file equal this summary; {@code false} otherwise.
     */
    public boolean equalsToFile(File file) {
        if (file == null)
            return false;

        long fileLen = file.length();
        String fileHash = getHashFromFile(file);
        return bytes != 0
                && bytes == fileLen
                && hash != null
                && hash.equals(fileHash);
    }

    private String getHashFromFile(File file) {
        try {
            return HashUtils.getMD5Checksum(file);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return toJson().toJSONString();
    }
}
