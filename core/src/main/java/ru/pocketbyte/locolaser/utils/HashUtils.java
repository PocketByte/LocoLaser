/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utils class that calculate hash sum of the file.
 *
 * @author Denis Shurygin
 */
public class HashUtils {

    /**
     * Gets MD5 Checksum of the file.
     * @param file File.
     * @return MD5 Checksum of the file.
     * @throws IOException
     */
    public static String getMD5Checksum(File file) throws IOException {
        if (file == null || !file.exists())
            return null;

        byte[] checksum = createChecksum(file);
        if (checksum != null) {
            StringBuilder resultBuilder = new StringBuilder();

            for (byte aChecksum : checksum) {
                resultBuilder.append(Integer.toString((aChecksum & 0xff) + 0x100, 16).substring(1));
            }
            return resultBuilder.toString();
        }
        return null;
    }

    private static byte[] createChecksum(File file) throws IOException {
        InputStream fileInputStream =  new FileInputStream(file);

        byte[] buffer = new byte[1024];
        MessageDigest complete;
        try {
            complete = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        int numRead;

        do {
            numRead = fileInputStream.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fileInputStream.close();
        return complete.digest();
    }
}
