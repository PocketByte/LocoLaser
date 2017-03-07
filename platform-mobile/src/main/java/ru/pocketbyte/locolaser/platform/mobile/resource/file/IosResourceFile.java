/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.platform.mobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.entity.ResItem;
import ru.pocketbyte.locolaser.resource.entity.ResLocale;
import ru.pocketbyte.locolaser.resource.entity.ResMap;
import ru.pocketbyte.locolaser.resource.entity.ResValue;
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Denis Shurygin
 */
public class IosResourceFile extends ResourceStreamFile {
    private static final String COMMENT_MULTILINE_START_1 = "/**";
    private static final String COMMENT_MULTILINE_START_2 = "/*";
    private static final String COMMENT_MULTILINE_END = "*/";
    private static final String COMMENT_SINGLE_LINE = "//";
    private static final String KEY_VALUE_LINE_PATTERN = "\"((?:[^\"]|\\\\\")+)\"\\s*=\\s*\"((?:[^\"]|\\\\\")*)\"\\s*;";

    private final String mLocale;

    public IosResourceFile(File file, String locale) {
        super(file);
        mLocale = locale;
    }

    @Override
    public ResMap read() {
        if (mFile.exists()) {
            ResLocale result = new ResLocale();

            Pattern keyValuePattern = Pattern.compile(KEY_VALUE_LINE_PATTERN);
            Matcher keyValueMatcher = keyValuePattern.matcher("");

            Path path = Paths.get(mFile.toURI());
            try (
                    BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                    LineNumberReader lineReader = new LineNumberReader(reader)
            ) {
                String line;
                boolean isComment = false;
                boolean isMultilineComment = false;
                StringBuilder comment = null;
                while ((line = lineReader.readLine()) != null) {

                    if (!isComment) {
                        int commentSignLength = 0;
                        isMultilineComment = false;
                        if (line.startsWith(COMMENT_MULTILINE_START_1)) {
                            isMultilineComment = true;
                            commentSignLength = COMMENT_MULTILINE_START_1.length();
                        }
                        else if (line.startsWith(COMMENT_MULTILINE_START_2)) {
                            isMultilineComment = true;
                            commentSignLength = COMMENT_MULTILINE_START_2.length();
                        }
                        else if (line.startsWith(COMMENT_SINGLE_LINE)) {
                            commentSignLength = COMMENT_SINGLE_LINE.length();
                        }
                        if (commentSignLength > 0) {
                            comment = new StringBuilder(line.substring(commentSignLength));
                            isComment = true;
                        }
                    }
                    else if (isMultilineComment) {
                        comment.append(line);
                    }

                    if (isMultilineComment && line.endsWith(COMMENT_MULTILINE_END)) {
                        comment.delete(comment.length() - COMMENT_MULTILINE_END.length() - 1, comment.length());
                        isMultilineComment = false;
                    }

                    if (!isComment) {
                        keyValueMatcher.reset(line); //reset the input
                        if (keyValueMatcher.find() && keyValueMatcher.groupCount() == 2) {
                            String key = keyValueMatcher.group(1);
                            String value = keyValueMatcher.group(2);

                            ResItem item = new ResItem(key);
                            item.addValue(new ResValue(fromPlatformValue(value), comment != null ? comment.toString().trim() : null, null));
                            result.put(item);
                        }
                    }

                    if (!isMultilineComment) {
                        if (isComment)
                            isComment = false;
                        else
                            comment = null;
                    }
                }
            } catch (IOException e) {
                // Do nothing
                e.printStackTrace();
            }


            ResMap resMap = new ResMap();
            resMap.put(mLocale, result);
            return resMap;
        }
        return null;
    }

    @Override
    public void write(ResMap resMap, WritingConfig writingConfig) throws IOException {
        open();

        writeStringLn(TemplateStr.GENERATED_KEY_VALUE_PAIR_COMMENT);
        writeln();

        ResLocale items = resMap.get(mLocale);

        boolean isFirst = true;
        for (String key: items.keySet()) {
            ResItem resItem = items.get(key);
            if (resItem != null) {
                if (!resItem.isHasQuantities()) {

                    if (!isFirst) {
                        writeln();
                        writeln();
                    }
                    else {
                        isFirst = false;
                    }

                    String comment = resItem.values.get(0).comment;
                    String value = resItem.values.get(0).value;

                    if (comment != null && (
                            (writingConfig == null || writingConfig.isDuplicateComments()) || !comment.equals(value))) {
                        writeString("/* ");
                        writeString(comment);
                        writeString(" */");
                        writeln();
                    }
                    writeString("\"");
                    writeString(resItem.key);
                    writeString("\" = \"");
                    writeString(toPlatformValue(value));
                    writeString("\";");
                }
                else {
                    // TODO write plurals https://developer.apple.com/library/ios/documentation/MacOSX/Conceptual/BPInternational/StringsdictFileFormat/StringsdictFileFormat.html
                }
            }
        }

        close();
    }

    static String toPlatformValue(String string) {
        string = string
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replaceAll("%([0-9]{0,})s", "%$1@");
        return string;
    }

    static String fromPlatformValue(String string) {
        string = string
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replaceAll("%([0-9]{0,})@", "%$1s");
        return string;
    }
}